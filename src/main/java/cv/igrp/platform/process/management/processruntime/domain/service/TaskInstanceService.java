package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessArtifactEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskInstanceService {

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceRepository processInstanceRepository;
  private final ProcessArtifactEntityRepository processArtifactEntityRepository;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessArtifactEntityRepository processArtifactEntityRepository) {

    this.taskInstanceRepository = taskInstanceRepository;
    this.taskInstanceEventRepository = taskInstanceEventRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceRepository = processInstanceRepository;
    this.processArtifactEntityRepository = processArtifactEntityRepository;
  }


  public void createTaskInstancesByProcess(ProcessInstance processInstance) {

    this.createNextTaskInstances(processInstance, Code.create(processInstance.getStartedBy()));
  }


  public TaskInstance getTaskById(Identifier id) {
    var taskInstance = getByIdWihEvents(id);
    // add Process Variables
    addVariables(taskInstance);
    return taskInstance;
  }


  public TaskInstance getByIdWihEvents(Identifier id) {
    return taskInstanceRepository.findByIdWithEvents(id.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  private void addVariables(TaskInstance taskInstance) {
    taskInstance.addVariables(runtimeProcessEngineRepository.getProcessVariables(taskInstance.getEngineProcessNumber()));
  }


  public void claimTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.claim(data);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.claimTask(
        taskInstance.getExternalId().getValue(),
        taskInstance.getAssignedBy().getValue()
    );
  }


  public void assignTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.assign(data);
    this.save(taskInstance);
    // Call the process engine to assign a task
    runtimeProcessEngineRepository.assignTask(
        taskInstance.getExternalId().getValue(),
        taskInstance.getAssignedBy().getValue(),
        data.getNote()
    );
  }


  public void unClaimTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.unClaim(data);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.unClaimTask(
        taskInstance.getExternalId().getValue()
    );
  }


  public TaskInstance completeTask(TaskOperationData data) {

    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.complete(data);
    data.validateSubmitedVariablesAndForms();
    var completedTask = save(taskInstance);
    // Call the process engine to complete a task
    runtimeProcessEngineRepository.completeTask(
        taskInstance.getExternalId().getValue(),
        data.getForms(),
        data.getVariables()
    );

    var processInstance  = processInstanceRepository
        .findById(taskInstance.getProcessInstanceId().getValue()).orElseThrow(
            () -> IgrpResponseStatusException.notFound("No Process Instance found with id: " + taskInstance.getProcessInstanceId().getValue()));

    var activityProcess = runtimeProcessEngineRepository
        .getProcessInstanceById(processInstance.getEngineProcessNumber().getValue());

    this.createNextTaskInstances(processInstance, data.getCurrentUser());

    if(activityProcess.getStatus() == ProcessInstanceStatus.COMPLETED){
      processInstance.complete(
          activityProcess.getEndedAt(),
          activityProcess.getEndedBy() != null ? activityProcess.getEndedBy() : data.getCurrentUser().getValue()
      );
      processInstanceRepository.save(processInstance);
    }

    return completedTask;
  }


  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
    final var pageableTask = taskInstanceRepository.findAll(filter);
    // add Process Variables
    addVariables(pageableTask);
    return pageableTask;
  }


  private void addVariables(PageableLista<TaskInstance> pageableTask) {

    final var variables = pageableTask.getContent().stream()
        .map(TaskInstance::getEngineProcessNumber)
        .distinct()
        .collect(Collectors.toMap(n->n,runtimeProcessEngineRepository::getProcessVariables));

    pageableTask.getContent().forEach(taskInstance ->
        taskInstance.addVariables(variables.get(taskInstance.getEngineProcessNumber())));
  }


  public Map<String,Object> getTaskVariables(Identifier id) {
    var taskInstance = getById(id);
    return runtimeProcessEngineRepository.getTaskVariables(taskInstance.getExternalId().getValue());
  }


  public TaskInstance getById(Identifier id) {
    return taskInstanceRepository.findById(id.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public TaskStatistics getGlobalTaskStatistics(){
    return taskInstanceRepository.getGlobalTaskStatistics();
  }


  public TaskStatistics getTaskStatisticsByUser(Code user){
    return taskInstanceRepository.getTaskStatisticsByUser(user);
  }



  void createNextTaskInstances(ProcessInstance processInstance, Code user) {
    // tasks from activiti
    final var activeTaskInstanceList = runtimeProcessEngineRepository
        .getActiveTaskInstances(processInstance.getEngineProcessNumber().getValue());

    if(activeTaskInstanceList.isEmpty())
      return;

    activeTaskInstanceList.forEach( t -> runtimeProcessEngineRepository
        .setTaskPriority(t.getExternalId().getValue(),processInstance.getPriority()));

    final var artifactAssociations = processArtifactEntityRepository
        .findAllByProcessDefinitionId(processInstance.getId().getValue().toString())
        .stream().collect( Collectors.toMap(ProcessArtifactEntity::getKey, a->Code.create(a.getFormKey())));

    activeTaskInstanceList.forEach( t-> this.createTask(
        t.withProperties(processInstance, artifactAssociations.get(t.getTaskKey().toString()), user))
    );
  }


  void createTask(TaskInstance taskInstance) {
    taskInstance.create();
    taskInstanceRepository.create(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getFirst());
  }


  TaskInstance save(TaskInstance taskInstance) {
    taskInstanceRepository.update(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getLast());
    return taskInstance;
  }


  private void saveCurrentEvent(TaskInstanceEvent taskInstanceEvent){
    taskInstanceEvent.create();
    taskInstanceEventRepository.save(taskInstanceEvent);
  }


}

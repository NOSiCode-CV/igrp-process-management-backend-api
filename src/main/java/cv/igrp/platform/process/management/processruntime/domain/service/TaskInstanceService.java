package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
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
import java.util.UUID;
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

    this.createNextTaskInstances(processInstance.getId(), processInstance.getNumber(),
        processInstance.getName(), processInstance.getBusinessKey(),
        processInstance.getApplicationBase(), Code.create(processInstance.getStartedBy()));
  }


  public TaskInstance getById(UUID id) {
    return taskInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public TaskInstance getByIdWihEvents(UUID id) {
    return taskInstanceRepository.findByIdWihEvents(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public void claimTask(UUID id, Code currentUser, String note) {
    var taskInstance = getByIdWihEvents(id);
    taskInstance.claim(currentUser,note);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.claimTask(
        taskInstance.getExternalId().getValue(),
        taskInstance.getAssignedBy().getValue()
    );
  }


  public void assignTask(UUID id, Code currentUser, Code userToAssign, String note) {
    var taskInstance = getByIdWihEvents(id);
    taskInstance.assign(currentUser, userToAssign,note);
    this.save(taskInstance);
    // Call the process engine to assign a task
    runtimeProcessEngineRepository.assignTask(
        taskInstance.getExternalId().getValue(),
        userToAssign.getValue(),
        note
    );
  }


  public void unClaimTask(UUID id, Code currentUser, String note) {
    var taskInstance = getByIdWihEvents(id);
    taskInstance.unClaim(currentUser,note);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.unClaimTask(
        taskInstance.getExternalId().getValue()
    );
  }


  public TaskInstance completeTask(UUID id, Code currentUser, Map<String,Object> forms, Map<String,Object> variables) {

    var taskInstance = getByIdWihEvents(id);

    var processInstance  = processInstanceRepository
        .findById(taskInstance.getProcessInstanceId().getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Process Instance found with id: " + id));

    // Call the process engine to complete a task
    runtimeProcessEngineRepository.completeTask(
        taskInstance.getExternalId().getValue(),
        forms,
        variables
    );

    var activityProcess = runtimeProcessEngineRepository
        .getProcessInstanceById(processInstance.getNumber().getValue());

    taskInstance.complete(currentUser);
    var completedTask = save(taskInstance);

    this.createNextTaskInstances(
        taskInstance.getProcessInstanceId(),
        taskInstance.getProcessNumber(),
        activityProcess.getName(),
        processInstance.getBusinessKey(),
        taskInstance.getApplicationBase(),
        currentUser
    );

    if(activityProcess.getStatus() == ProcessInstanceStatus.COMPLETED){
      processInstance.complete(
          activityProcess.getEndedAt(),
          activityProcess.getEndedBy() != null ? activityProcess.getEndedBy() : currentUser.getValue()
      );
      processInstanceRepository.save(processInstance);
    }

    return completedTask;
  }


  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
    return taskInstanceRepository.findAll(filter);
  }


  public Map<String,Object> getTaskVariables(UUID id) {
    var taskInstance = getById(id);
    return runtimeProcessEngineRepository.getTaskVariables(taskInstance.getExternalId().getValue());
  }



  private void createNextTaskInstances(Identifier processInstanceId, Code processNumber,
                                       String processName, Code businessKey, Code applicationBase, Code user) {
    // tasks from activiti
    final var activeTaskInstanceList = runtimeProcessEngineRepository
        .getActiveTaskInstances(processNumber.getValue());

    final var artifactAssociations = processArtifactEntityRepository
        .findAllByProcessDefinitionId(processInstanceId.getValue().toString())
        .stream().collect( Collectors.toMap(ProcessArtifactEntity::getKey, a->Code.create(a.getFormKey())));

    activeTaskInstanceList.forEach( t-> this.createTask( t.withProperties(applicationBase, Code.create(processName),
        businessKey, processInstanceId, artifactAssociations.get(t.getTaskKey().toString()), user))
    );
  }


  private void createTask(TaskInstance taskInstance) {
    taskInstance.create();
    taskInstanceRepository.create(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getFirst());
  }


  private TaskInstance save(TaskInstance taskInstance) {
    taskInstanceRepository.update(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getLast());
    return taskInstance;
  }


  private void saveCurrentEvent(TaskInstanceEvent taskInstanceEvent){
    taskInstanceEvent.create();
    taskInstanceEventRepository.save(taskInstanceEvent);
  }


}

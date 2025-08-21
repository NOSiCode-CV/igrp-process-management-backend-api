package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
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


  public void createTaskInstancesByProcess(Identifier processInstanceId,
                                           Code processNumber,
                                           String processType,
                                           Code businessKey,
                                           Code applicationBase) {

      final var activeTaskList = runtimeProcessEngineRepository
          .getActiveTaskInstances(processNumber.getValue());

      final var artifactAssociations = processArtifactEntityRepository
          .findAllByProcessDefinitionId(processInstanceId.getValue().toString())
          .stream().collect( Collectors.toMap(ProcessArtifactEntity::getKey, a->Code.create(a.getFormKey())));

      activeTaskList.forEach( t-> this.createTask( t.withIdentity(
          applicationBase,
          Code.create(processType),
          businessKey,
          processInstanceId,
          artifactAssociations.get(t.getTaskKey().toString())))
      );
  }


  private void createTask(TaskInstance taskInstance) {
      taskInstance.create();
      taskInstanceRepository.create(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
  }


  public TaskInstance getById(UUID id) {
      return taskInstanceRepository.findById(id)
          .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public TaskInstance getByIdWihEvents(UUID id) {
    return taskInstanceRepository.findByIdWihEvents(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public void claimTask(UUID id,String note) {
      var taskInstance = getByIdWihEvents(id);
      taskInstance.claim(note);
      this.save(taskInstance);
      // Call the process engine to claim a task
      runtimeProcessEngineRepository.claimTask(
          taskInstance.getExternalId().getValue(),
          taskInstance.getAssignedBy().getValue()
      );
  }


  public void assignTask(UUID id, Code user, String note) {
      var taskInstance = getByIdWihEvents(id);
      taskInstance.assign(user,note);
      this.save(taskInstance);
      // Call the process engine to assign a task
      runtimeProcessEngineRepository.assignTask(
          taskInstance.getExternalId().getValue(),
          user.getValue(),
          note
      );
  }


  public void unClaimTask(UUID id, String note) {
      var taskInstance = getByIdWihEvents(id);
      taskInstance.unClaim(note);
      this.save(taskInstance);
      // Call the process engine to claim a task
      runtimeProcessEngineRepository.unClaimTask(
          taskInstance.getExternalId().getValue()
      );
  }


  public TaskInstance completeTask(UUID id, Map<String,Object> variables) {

      var taskInstance = getByIdWihEvents(id);

      var processInstance  = processInstanceRepository
          .findById(taskInstance.getProcessInstanceId().getValue())
          .orElseThrow(() -> IgrpResponseStatusException.notFound("No Process Instance found with id: " + id));

      runtimeProcessEngineRepository.completeTask(
          taskInstance.getExternalId().getValue(),
          variables
      );

      var activityProcess = runtimeProcessEngineRepository
          .getProcessInstanceById(processInstance.getNumber().getValue());

      taskInstance.complete();
      var completedTask = save(taskInstance);

      createTaskInstancesByProcess(
          taskInstance.getProcessInstanceId(),
          taskInstance.getProcessNumber(),
          activityProcess.getName(),
          processInstance.getBusinessKey(),
          taskInstance.getApplicationBase()
      );

      if(activityProcess.getStatus() == ProcessInstanceStatus.COMPLETED){
          processInstance.complete(
              activityProcess.getEndedAt(),
              activityProcess.getEndedBy() != null ? activityProcess.getEndedBy() : "demo"
          );
          processInstanceRepository.save(processInstance);
      }

      return completedTask;
  }


  private TaskInstance save(TaskInstance taskInstance) {
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getLast());
      taskInstanceRepository.update(taskInstance);
      return taskInstance;
  }


  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstance> getAllMyTasks(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public Map<String,Object> getTaskVariables(UUID id){
      var taskInstance = getById(id);
      return runtimeProcessEngineRepository.getTaskVariables(taskInstance.getExternalId().getValue());
  }


}

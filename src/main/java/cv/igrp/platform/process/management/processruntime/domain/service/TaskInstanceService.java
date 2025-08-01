package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.nosi.igrp.runtime.activiti.engine.task.ActivitiTaskManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskInstanceService {

  private final ActivitiTaskManager activitiTaskManager;
  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;

  public TaskInstanceService(ActivitiTaskManager activitiTaskManager,
                             TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository) {

      this.activitiTaskManager = activitiTaskManager;
      this.taskInstanceRepository = taskInstanceRepository;
      this.taskInstanceEventRepository = taskInstanceEventRepository;
  }


  @Transactional
  public TaskInstance createTask(TaskInstance model) {

      final var externalTaskId = activitiTaskManager.createTask(
          model.getProcessInstanceId().getValue().toString(),
          model.getTaskKey().getValue(),
          model.getName().getValue(),
          model.getUser().getValue(),
          model.getTaskVariables());

      return taskInstanceRepository.create(model,
          Code.create(externalTaskId),
          TaskEventType.CREATE);
  }


  public TaskInstance getTaskInstanceById(UUID id) {
    return taskInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  @Transactional
  public void claimTask(TaskInstance model) {

      activitiTaskManager.assignTask(
          taskInstanceRepository.getExternalIdByTaskId(model.getId().getValue()),
          model.getUser().getValue(),
          model.getReason().getValue());

      taskInstanceRepository.updateTask(
          model.getId().getValue(),
          TaskEventType.CLAIM,
          model.getUser(),
          TaskInstanceStatus.ASSIGNED,
          LocalDateTime.now());
  }


  @Transactional
  public void assignTask(TaskInstance model) {

      activitiTaskManager.assignTask(
          taskInstanceRepository.getExternalIdByTaskId(model.getId().getValue()),
          model.getUser().getValue(),
          model.getReason().getValue());

      taskInstanceRepository.updateTask(
          model.getId().getValue(),
          TaskEventType.ASSIGN,
          model.getUser(),
          TaskInstanceStatus.ASSIGNED,
          LocalDateTime.now());
  }


  @Transactional
  public void unClaimTask(TaskInstance model) {

      activitiTaskManager.claimTask(
          taskInstanceRepository.getExternalIdByTaskId(model.getId().getValue()),
          model.getUser().getValue());

      taskInstanceRepository.updateTask(
          model.getId().getValue(),
          TaskEventType.UNCLAIM,
          model.getUser(),
          TaskInstanceStatus.SUSPENDED,
          LocalDateTime.now());
  }


  @Transactional
  public TaskInstance completeTask(TaskInstance model) {

      activitiTaskManager.completeTask(
          taskInstanceRepository.getExternalIdByTaskId(model.getId().getValue()),
          model.getTaskVariables(),
          model.getUser().getValue());

      return taskInstanceRepository.completeTask(
          model.getId().getValue(),
          TaskEventType.COMPLETE,
          model.getUser(),
          TaskInstanceStatus.COMPLETED,
          LocalDateTime.now());
  }


  @Transactional(readOnly = true)
  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  @Transactional(readOnly = true)
  public PageableLista<TaskInstance> getAllMyTasks(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  @Transactional(readOnly = true)
  public PageableLista<TaskInstanceEvent> getTaskHistory(TaskInstanceFilter filter) {
      return taskInstanceEventRepository.getTaskHistory(filter);
  }


}

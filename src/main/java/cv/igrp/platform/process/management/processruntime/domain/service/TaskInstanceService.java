package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceInfo;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.nosi.igrp.runtime.activiti.engine.task.ActivitiTaskManager;
import org.springframework.stereotype.Service;

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



  public TaskInstanceInfo createTask(TaskInstance taskInstance) {

      taskInstance.create();

      return taskInstanceRepository.create(taskInstance);
  }


  public TaskInstanceInfo getTaskInstanceById(UUID id) {
      return taskInstanceRepository.findById(id)
          .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public void claimTask(TaskInstance taskInstance) {

      taskInstance.update();

//      activitiTaskManager.assignTask(
//          taskInstanceRepository.getExternalIdByTaskId(taskInstance.getId().getValue()),
//          taskInstance.getUser().getValue(),
//          null);

      taskInstanceRepository.updateTask(taskInstance);
  }


  public void assignTask(TaskInstance taskInstance) {

      taskInstance.update();

//      activitiTaskManager.assignTask(
//          taskInstanceRepository.getExternalIdByTaskId(taskInstance.getId().getValue()),
//          taskInstance.getUser().getValue(),
//          null);

      taskInstanceRepository.updateTask(taskInstance);
  }


  public void unClaimTask(TaskInstance taskInstance) {

      taskInstance.update();

//      activitiTaskManager.unclaimTask(
//          taskInstanceRepository.getExternalIdByTaskId(taskInstance.getId().getValue()));

      taskInstanceRepository.updateTask(taskInstance);
  }


  public TaskInstanceInfo completeTask(TaskInstance taskInstance) {

      taskInstance.complete();

      /*activitiTaskManager.completeTask(
          taskInstanceRepository.getExternalIdByTaskId(taskInstance.getId().getValue()),
          taskInstance.getTaskVariables(),
          taskInstance.getUser().getValue());*/

      var completedTask = taskInstanceRepository.completeTask(taskInstance);

      /*if(processoNaoTerminou){ todo
        activitiTaskManager.createTask(
            processInstanceId,
            taskDefinitionKey,
            taskName,
            assignee,
            variables);
      }*/

      return completedTask;
  }


  public PageableLista<TaskInstanceInfo> getAllTaskInstances(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstanceInfo> getAllMyTasks(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstanceEvent> getTaskHistory(TaskInstanceFilter filter) {
      return taskInstanceEventRepository.getTaskHistory(filter);
  }


}

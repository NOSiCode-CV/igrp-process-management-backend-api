package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.nosi.igrp.runtime.core.engine.task.TaskActionService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TaskInstanceService {

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final TaskActionService taskActionService;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                             TaskActionService taskActionService) {

      this.taskInstanceRepository = taskInstanceRepository;
      this.taskInstanceEventRepository = taskInstanceEventRepository;
      this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
      this.taskActionService = taskActionService;
  }


  public void createTaskInstancesByProcess(Code processNumber, Identifier processInstanceId,
                                           Code processType, Code applicationBase) {

      var activeTaskList = runtimeProcessEngineRepository
          .getActiveTaskInstances(processNumber.getValue());

      if(activeTaskList.isEmpty())
        return;

      activeTaskList.forEach(t->this.createTask(
          t.withIdentity(applicationBase,processType,processInstanceId)));
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


  public void claimTask(UUID id) {
      var taskInstance = getById(id);
      taskInstance.claim(null);
      this.save(taskInstance);
  }


  public void assignTask(UUID id, Code user, String note) {
      var taskInstance = getById(id);
      taskInstance.assign(user,note);
      this.save(taskInstance);
  }


  public void unClaimTask(UUID id, String note) {
      var taskInstance = getById(id);
      taskInstance.unClaim(note);
      this.save(taskInstance);
  }


  public TaskInstance completeTask(UUID id, Map<String,Object> variables, String note) {
      var taskInstance = getById(id);
      taskActionService.completeTask(id.toString(),variables,null);
      taskInstance.complete(variables,note);
      var completedTask = save(taskInstance);
      createTaskInstancesByProcess(taskInstance.getProcessNumber(), taskInstance.getProcessInstanceId(),
              taskInstance.getProcessType(), taskInstance.getApplicationBase());
      return completedTask;
  }


  private TaskInstance save(TaskInstance taskInstance) {
      var completedTask =taskInstanceRepository.update(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
      return completedTask;
  }


  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstance> getAllMyTasks(TaskInstanceFilter filter) {
      return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstanceEvent> getTaskHistory(TaskInstanceFilter filter) {
      return taskInstanceEventRepository.getTaskHistory(filter);
  }


}

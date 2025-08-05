package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TaskInstanceService {

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;

  public TaskInstanceService(
                             TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository) {

      this.taskInstanceRepository = taskInstanceRepository;
      this.taskInstanceEventRepository = taskInstanceEventRepository;
  }



  public TaskInstance createTask(TaskInstance taskInstance) {
      taskInstance.create();
      var saved = taskInstanceRepository.create(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
      return saved;
  }


  public TaskInstance getById(UUID id) {
      return taskInstanceRepository.findById(id)
          .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public void claimTask(UUID id) {
      var taskInstance = getById(id);
      taskInstance.claim(null);
      taskInstanceRepository.update(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
  }


  public void assignTask(UUID id, Code user, String note) {
      var taskInstance = getById(id);
      taskInstance.assign(user,note);
      taskInstanceRepository.update(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
  }


  public void unClaimTask(UUID id, String note) {
      var taskInstance = getById(id);
      taskInstance.unClaim(note);
      taskInstanceRepository.update(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());
  }


  public TaskInstance completeTask(UUID id, Map<String,Object> variables, String note) {
      var taskInstance = getById(id);
      taskInstance.complete(variables,note);
      var completedTask =taskInstanceRepository.update(taskInstance);
      taskInstanceEventRepository.save(taskInstance.getTaskInstanceEvents().getFirst());

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

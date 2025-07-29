package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class TaskInstanceService {

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final TaskInstanceMapper taskInstanceMapper;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             TaskInstanceMapper taskInstanceMapper) {

      this.taskInstanceRepository = taskInstanceRepository;
      this.taskInstanceEventRepository = taskInstanceEventRepository;
      this.taskInstanceMapper = taskInstanceMapper;
  }


  public TaskInstance create(TaskInstance model) {

    var taskInstanceEntity = taskInstanceRepository.create(model);


    var taskInstanceEventEntity = taskInstanceEventRepository.save(
        taskInstanceEntity, model.getTaskInstanceEvents().getFirst());
    taskInstanceEntity.addEvents(new ArrayList<>(taskInstanceEntity));
    return taskInstanceEntity;
  }


  public TaskInstance getTaskInstanceById(UUID id) {
    return taskInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }


  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {
    return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstance> getAllMyTasks(TaskInstanceFilter filter) {
    return taskInstanceRepository.findAll(filter);
  }


  public PageableLista<TaskInstance> getAllTaskHistory(UUID id) {
    return null;
  }


  public TaskInstance updateTaskUser(UUID id, String user) {
    return null;
  }
}

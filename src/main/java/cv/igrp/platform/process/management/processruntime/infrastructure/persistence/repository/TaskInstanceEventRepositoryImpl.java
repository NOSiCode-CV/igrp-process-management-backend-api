package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEventEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskInstanceEventRepositoryImpl implements TaskInstanceEventRepository {

  private final TaskInstanceEventEntityRepository taskInstanceEventEntityRepository;
  private final TaskInstanceMapper taskMapper;

  public TaskInstanceEventRepositoryImpl(TaskInstanceEventEntityRepository taskInstanceEventEntityRepository,
                                         TaskInstanceMapper taskMapper) {
      this.taskInstanceEventEntityRepository = taskInstanceEventEntityRepository;
      this.taskMapper = taskMapper;
  }


  @Override
  public Optional<TaskInstanceEvent> findById(UUID id) {
      return taskInstanceEventEntityRepository.findById(id).map(taskMapper::toEventModel);
  }

  @Override
  public void save(TaskInstanceEvent taskInstanceEvent) {
    taskInstanceEventEntityRepository.save(taskMapper.toTaskEventEntity(taskInstanceEvent));
  }


}

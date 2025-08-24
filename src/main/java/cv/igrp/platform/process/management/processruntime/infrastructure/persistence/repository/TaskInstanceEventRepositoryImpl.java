package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceEventMapper;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEventEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskInstanceEventRepositoryImpl implements TaskInstanceEventRepository {

  private final TaskInstanceEventEntityRepository taskInstanceEventEntityRepository;
  private final TaskInstanceEventMapper eventMapper;

  public TaskInstanceEventRepositoryImpl(TaskInstanceEventEntityRepository taskInstanceEventEntityRepository,
                                         TaskInstanceEventMapper eventMapper) {
    this.taskInstanceEventEntityRepository = taskInstanceEventEntityRepository;
    this.eventMapper = eventMapper;
  }


  @Override
  public Optional<TaskInstanceEvent> findById(UUID id) {
    return taskInstanceEventEntityRepository.findById(id).map(eventMapper::toEventModel);
  }


  @Override
  public void save(TaskInstanceEvent taskInstanceEvent) {
    taskInstanceEventEntityRepository.save(eventMapper.toEventEntity(taskInstanceEvent));
  }


}

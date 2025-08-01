package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class TaskInstanceRepositoryImpl implements TaskInstanceRepository {

  private final TaskInstanceEntityRepository taskInstanceEntityRepository;
  private final TaskInstanceMapper taskMapper;

  public TaskInstanceRepositoryImpl(TaskInstanceEntityRepository taskInstanceEntityRepository,
                                    TaskInstanceMapper taskMapper) {

    this.taskInstanceEntityRepository = taskInstanceEntityRepository;
    this.taskMapper = taskMapper;
  }


  private TaskInstanceEntity getTaskInstanceByID(UUID id) {

    return taskInstanceEntityRepository.findById(id)
        .orElseThrow(()-> IgrpResponseStatusException.notFound("No TaskInstance fount for id["+ id +"]"));
  }


  @Override
  public Optional<TaskInstance> findById(UUID id) {
    return taskInstanceEntityRepository.findById(id).map(taskMapper::toModelWithEvents);
  }


  @Override
  public String getExternalIdByTaskId(UUID id) {
    return getTaskInstanceByID(id).getExternalId();
  }


  @Override
  public TaskInstance create(TaskInstance taskInstance, Code externalId, TaskEventType taskEventType) {

    var taskEntity = taskMapper.toNewTaskEntity(taskInstance,externalId);
    taskMapper.toNewTaskEventEntity(
        taskEntity,
        taskEventType,
        taskInstance.getTaskInstanceEvents().getFirst());
    taskEntity = taskInstanceEntityRepository.save(taskEntity);
    return taskMapper.toModel(taskEntity);
  }


  @Override
  public void updateTask(UUID id, TaskEventType taskEventType, Code user,
                         TaskInstanceStatus taskInstanceStatus, LocalDateTime dateTime) {

      update(id, taskEventType, user, taskInstanceStatus, dateTime);
  }

  @Override
  public TaskInstance completeTask(UUID id, TaskEventType taskEventType, Code user,
                                   TaskInstanceStatus taskInstanceStatus, LocalDateTime dateTime) {

    return taskMapper.toModel( update(id, taskEventType, user, taskInstanceStatus, dateTime) );
  }


  private TaskInstanceEntity update(UUID id, TaskEventType taskEventType, Code user,
                         TaskInstanceStatus taskInstanceStatus, LocalDateTime dateTime) {

    var taskInstanceEntity = getTaskInstanceByID(id);
    taskInstanceEntity.setStatus(taskInstanceStatus);
    //taskMapper.toNewTaskEventEntity(taskInstanceEntity,taskEventType,null); todo
    return taskInstanceEntityRepository.save(taskInstanceEntity);
  }


  @Override
  public PageableLista<TaskInstance> findAll(TaskInstanceFilter filter) {

      Specification<TaskInstanceEntity> spec = buildSpecification(filter);

      PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

      var pageableProcess = taskInstanceEntityRepository.findAll(spec, pageRequest);

      List<TaskInstance> content = pageableProcess.stream()
          .map(taskMapper::toModel)
          .toList();

      return new PageableLista<>(
          pageableProcess.getNumber(),
          pageableProcess.getSize(),
          pageableProcess.getTotalElements(),
          pageableProcess.getTotalPages(),
          pageableProcess.isLast(),
          pageableProcess.isFirst(),
          content
      );
  }


  private Specification<TaskInstanceEntity> buildSpecification(TaskInstanceFilter filter) {

      Specification<TaskInstanceEntity> spec = (root, query, builder) -> null;

      if (filter.getProcessNumber() != null) {
          spec = spec.and((root, query, cb) ->
              cb.equal(root.get("processInstanceId").get("number"), filter.getProcessNumber().getValue()));
      }

      if (filter.getProcessKey() != null) {
          spec = spec.and((root, query, cb) ->
              cb.equal(root.get("processInstanceId").get("procReleaseKey"), filter.getProcessKey().getValue()));
      }

      if (filter.getStatus() != null) {
          spec = spec.and((root, query, cb) ->
              cb.equal(root.get("status"), filter.getStatus().getCode()));
      }

      if (filter.getUser() != null) {
          spec = spec.and((root, query, cb) ->
              cb.equal(root.get("startedBy"), filter.getUser().getValue()));
      }

      if (filter.getSearchTerms() != null) {
          spec = spec.and((root, query, cb) ->
              cb.like(root.get("searchTerms"), "%" + filter.getSearchTerms() + "%"));
      }

      if (filter.getDateFrom() != null) {
          spec = spec.and((root, query, cb) ->
              cb.greaterThanOrEqualTo(root.get("startedAt"), filter.getDateFrom().atStartOfDay()));
      }

      if (filter.getDateTo() != null) {
          spec = spec.and((root, query, cb) ->
            cb.lessThanOrEqualTo(root.get("startedAt"), filter.getDateTo().atTime(LocalTime.MAX)));
      }

      return spec;
  }


}


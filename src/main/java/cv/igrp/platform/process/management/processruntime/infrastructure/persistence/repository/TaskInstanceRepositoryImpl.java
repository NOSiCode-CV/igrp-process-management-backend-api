package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskStatistics;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class TaskInstanceRepositoryImpl implements TaskInstanceRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstanceRepositoryImpl.class);

  private final TaskInstanceEntityRepository taskInstanceEntityRepository;
  private final TaskInstanceMapper taskMapper;

  public TaskInstanceRepositoryImpl(TaskInstanceEntityRepository taskInstanceEntityRepository,
                                    TaskInstanceMapper taskMapper) {

      this.taskInstanceEntityRepository = taskInstanceEntityRepository;
      this.taskMapper = taskMapper;
  }


  @Override
  public Optional<TaskInstance> findById(UUID id) {
    return taskInstanceEntityRepository.findById(id).map(taskMapper::toModel);
  }


  @Override
  public Optional<TaskInstance> findByIdWihEvents(UUID id) {
      return taskInstanceEntityRepository.findById(id).map(taskMapper::toModelWithEvents);
  }


  @Override
  public void create(TaskInstance taskInstance) {
      taskInstanceEntityRepository.save(taskMapper.toNewTaskEntity(taskInstance));
  }


  @Override
  public void update(TaskInstance taskInstance) {
      var taskInstanceEntity = taskInstanceEntityRepository
          .findById(taskInstance.getId().getValue())
          .orElseThrow(() -> IgrpResponseStatusException.notFound(
              "No Task Instance found with id: " + taskInstance.getId().getValue()));
      taskMapper.toTaskEntity(taskInstance,taskInstanceEntity);
      taskInstanceEntityRepository.save(taskInstanceEntity);
  }


  @Override
  @Transactional(readOnly = true)
  public PageableLista<TaskInstance> findAll(TaskInstanceFilter filter) {

      Specification<TaskInstanceEntity> spec = buildSpecification(filter);

      PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(),
          Sort.by(Sort.Direction.DESC, "startedAt"));

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

    LOGGER.debug("Filter: {}",filter);

    Specification<TaskInstanceEntity> spec = (root, query, builder) -> null;

    if (filter.getProcessInstanceId() != null) {
      spec = spec.and((root, query, cb) ->
          cb.equal(root.get("processInstanceId").get("id"), filter.getProcessInstanceId().getValue()));
    }

    if (filter.getProcessNumber() != null) {
      spec = spec.and((root, query, cb) ->
        cb.or(cb.equal(root.get("processNumber"), filter.getProcessNumber().getValue()),
              cb.equal(root.get("businessKey"),filter.getProcessNumber().getValue())));
    }

    if (filter.getProcessName() != null) {
      spec = spec.and((root, query, cb) ->
          cb.like(root.get("processName"), "%"+ filter.getProcessName() +"%"));
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


  @Override
  public TaskStatistics getGlobalTaskStatistics() {
    long total = taskInstanceEntityRepository.count();

    long available = countByStatus(TaskInstanceStatus.CREATED);
    long assigned = countByStatus(TaskInstanceStatus.ASSIGNED);
    long suspended = countByStatus(TaskInstanceStatus.SUSPENDED);
    long completed = countByStatus(TaskInstanceStatus.COMPLETED);
    long canceled = countByStatus(TaskInstanceStatus.CANCELED);

    return TaskStatistics.builder()
        .totalTaskInstances(total)
        .totalAvailableTasks(available)
        .totalAssignedTasks(assigned)
        .totalSuspendedTasks(suspended)
        .totalCompletedTasks(completed)
        .totalCanceledTasks(canceled)
        .build();
  }


  private long countByStatus(TaskInstanceStatus status) {
    return taskInstanceEntityRepository.count(
        (root, query, cb) -> cb.equal(root.get("status"), status)
    );
  }


  @Override
  public TaskStatistics getTaskStatisticsByUser(Code user) {

    LOGGER.debug("User: {}",user.getValue());

    // base: tasks ligadas ao user
    Specification<TaskInstanceEntity> userSpec = (root, q, cb) ->
        cb.or(
            cb.equal(root.get("assignedBy"), user.getValue()),
            cb.equal(root.get("endedBy"), user.getValue())
        );

    // total: todas as tasks, independente do user
    long total = taskInstanceEntityRepository.count();

    long available = countByStatus(userSpec, TaskInstanceStatus.CREATED);
    long assigned = countByStatusAndField(userSpec, TaskInstanceStatus.ASSIGNED, "assignedBy", user.getValue());
    long suspended = countByStatusAndField(userSpec, TaskInstanceStatus.SUSPENDED, "assignedBy", user.getValue());
    long completed = countByStatusAndField(userSpec, TaskInstanceStatus.COMPLETED, "endedBy", user.getValue());
    long canceled = countByStatusAndField(userSpec, TaskInstanceStatus.CANCELED, "endedBy", user.getValue());

    return TaskStatistics.builder()
        .totalTaskInstances(total)
        .totalAvailableTasks(available)
        .totalAssignedTasks(assigned)
        .totalSuspendedTasks(suspended)
        .totalCompletedTasks(completed)
        .totalCanceledTasks(canceled)
        .build();
  }


  private long countByStatus(Specification<TaskInstanceEntity> base, TaskInstanceStatus status) {
    return taskInstanceEntityRepository.count(
      base.and((root, q, cb) -> cb.equal(root.get("status"), status))
    );
  }


  private long countByStatusAndField(Specification<TaskInstanceEntity> base,
                                     TaskInstanceStatus status, String field, String value) {
    return taskInstanceEntityRepository.count(
      base.and((root, q, cb) -> cb.equal(root.get("status"), status))
          .and((root, q, cb) -> cb.equal(root.get(field), value))
    );
  }


}


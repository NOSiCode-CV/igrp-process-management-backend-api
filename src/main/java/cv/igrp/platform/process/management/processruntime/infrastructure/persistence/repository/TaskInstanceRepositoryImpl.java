package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class TaskInstanceRepositoryImpl implements TaskInstanceRepository {

  private final TaskInstanceEntityRepository taskInstanceEntityRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final TaskInstanceMapper taskMapper;

  public TaskInstanceRepositoryImpl(TaskInstanceEntityRepository taskInstanceEntityRepository,
                                    TaskInstanceEventRepository taskInstanceEventRepository,
                                    TaskInstanceMapper taskMapper) {
      this.taskInstanceEntityRepository = taskInstanceEntityRepository;
      this.taskInstanceEventRepository = taskInstanceEventRepository;
      this.taskMapper = taskMapper;
  }


  @Override
  public TaskInstance save(TaskInstance taskInstance) {
    var taskInstanceEntity = taskInstanceEntityRepository.save(taskMapper.toEntity(taskInstance));
    var taskInstanceEvent = taskInstanceEventRepository.save(
        taskInstanceEntity,taskInstance.getTaskInstanceEvents().getFirst());
    return taskMapper.toModel(taskInstanceEntity);
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
      spec = spec.and((root, query, cb) -> {
        return cb.lessThanOrEqualTo(root.get("startedAt"), filter.getDateTo().atTime(LocalTime.MAX));
      });
    }

    return spec;
  }


  @Override
  public Optional<TaskInstance> findById(UUID id) {
      return taskInstanceEntityRepository.findById(id).map(taskMapper::toModel);
  }

  @Override
  public void updateStatus(UUID id, TaskInstanceStatus newStatus) {
    // todo
  }

}


package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;


import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessStatistics;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessInstanceEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessInstanceRepositoryImpl implements ProcessInstanceRepository {

  private final ProcessInstanceEntityRepository processInstanceEntityRepository;
  private final ProcessInstanceMapper mapper;

  public ProcessInstanceRepositoryImpl(ProcessInstanceEntityRepository processInstanceEntityRepository,
                                       ProcessInstanceMapper mapper) {
    this.processInstanceEntityRepository = processInstanceEntityRepository;
    this.mapper = mapper;
  }

  @Override
  public ProcessInstance save(ProcessInstance processInstance) {
    ProcessInstanceEntity processInstanceEntity = mapper.toEntity(processInstance);
    return mapper.toModel(processInstanceEntityRepository.save(processInstanceEntity));
  }

  @Override
  public ProcessInstance update(ProcessInstance processInstance) {
    ProcessInstanceEntity processInstanceEntity = processInstanceEntityRepository.findById(processInstance.getId().getValue()).orElseThrow(
        () -> new IllegalArgumentException("Process Instance not found with id: " + processInstance.getId())
    );
    mapper.updateEntityFromModel(processInstance, processInstanceEntity);
    return mapper.toModel(processInstanceEntityRepository.save(processInstanceEntity));
  }

  @Override
  public PageableLista<ProcessInstance> findAll(ProcessInstanceFilter filter) {

    Specification<ProcessInstanceEntity> spec = buildSpecification(filter);

    PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

    var pageableProcess = processInstanceEntityRepository.findAll(spec, pageRequest);

    List<ProcessInstance> content = pageableProcess.stream()
        .map(mapper::toModel)
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

  private Specification<ProcessInstanceEntity> buildSpecification(ProcessInstanceFilter filter) {

    Specification<ProcessInstanceEntity> spec = (root, query, builder) -> null;

    if (filter.getNumber() != null) {
      spec = spec.and((root, query, cb) -> {
        String value = filter.getNumber().getValue();
        return cb.or(cb.equal(root.get("number"), value),  cb.equal(root.get("businessKey"), value));
      });
    }

    if (filter.getStatus() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("status"), filter.getStatus());
      });
    }

    if (filter.getProcReleaseId() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("procReleaseId"), filter.getProcReleaseId().getValue());
      });
    }

    if (filter.getProcReleaseKey() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("procReleaseKey"), filter.getProcReleaseKey().getValue());
      });
    }

    if (filter.getApplicationBase() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("applicationBase"), filter.getApplicationBase().getValue());
      });
    }

    if (!filter.getIncludeProcessNumbers().isEmpty()) {
      spec = spec.and((root, query, cb) ->
          root.get("engineProcessNumber").in(filter.getIncludeProcessNumbers())
      );
    }

    if (filter.getDateFrom() != null) {
      spec = spec.and((root, query, cb) ->
          cb.greaterThanOrEqualTo(root.get("startedAt"), filter.getDateFrom().atStartOfDay()));
    }

    if (filter.getDateTo() != null) {
      spec = spec.and((root, query, cb) ->
          cb.lessThanOrEqualTo(root.get("startedAt"), filter.getDateTo().atTime(LocalTime.MAX)));
    }

    if (filter.getName() != null) {
      spec = spec.and((root, query, cb) ->
          cb.like(root.get("name"), "%"+ filter.getName().getValue() +"%"));
    }

    return spec;
  }

  @Override
  public Optional<ProcessInstance> findById(UUID id) {
    return processInstanceEntityRepository.findById(id).map(mapper::toModel);
  }

  @Override
  public ProcessStatistics getProcessInstanceStatistics() {

    long total = processInstanceEntityRepository.count();

    long created = countByStatus(ProcessInstanceStatus.CREATED);
    long running = countByStatus(ProcessInstanceStatus.RUNNING);
    long suspended = countByStatus(ProcessInstanceStatus.SUSPENDED);
    long completed = countByStatus(ProcessInstanceStatus.COMPLETED);
    long canceled = countByStatus(ProcessInstanceStatus.CANCELED);

    return ProcessStatistics.builder()
        .totalProcessInstances(total)
        .totalCreatedProcess(created)
        .totalRunningProcess(running)
        .totalSuspendedProcess(suspended)
        .totalCompletedProcess(completed)
        .totalCanceledProcess(canceled)
        .build();
  }

  private long countByStatus(ProcessInstanceStatus status) {
    return processInstanceEntityRepository.count((root, query, cb) -> cb.equal(root.get("status"), status));
  }

  @Override
  public Optional<ProcessInstance> findByBusinessKey(String businessKey) {
    return processInstanceEntityRepository.findByBusinessKey(businessKey).map(mapper::toModel);
  }

}

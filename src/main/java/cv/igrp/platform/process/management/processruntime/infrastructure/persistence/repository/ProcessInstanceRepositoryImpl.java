package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;


import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessInstanceEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
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
  public ProcessInstance save(ProcessInstance area) {
    ProcessInstanceEntity processInstanceEntity = mapper.toEntity(area);
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

    if (filter.getSearchTerms() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.like(cb.lower(root.get("searchTerms")), "%" + filter.getSearchTerms().toLowerCase() + "%");
      });
    }

    if (filter.getApplicationBase() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("applicationBase"), filter.getApplicationBase().getValue());
      });
    }

    return spec;
  }

  @Override
  public Optional<ProcessInstance> findById(UUID id) {
    return processInstanceEntityRepository.findById(id).map(mapper::toModel);
  }

}

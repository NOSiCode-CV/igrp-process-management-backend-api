package cv.igrp.platform.process.management.area.infrastructure.persistence.repository;


import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.area.domain.repository.AreaProcessRepository;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaProcessEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.AreaProcessEntityRepository;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AreaProcessRepositoryImpl implements AreaProcessRepository {

  private final AreaProcessEntityRepository areaProcessEntityRepository;
  private final AreaProcessMapper areaProcessMapper;

  public AreaProcessRepositoryImpl(AreaProcessEntityRepository areaProcessEntityRepository,
                                   AreaProcessMapper areaProcessMapper) {
    this.areaProcessEntityRepository = areaProcessEntityRepository;
    this.areaProcessMapper = areaProcessMapper;
  }

  @Override
  public AreaProcess save(AreaProcess areaProcess) {
    AreaProcessEntity areaProcessEntity = areaProcessEntityRepository.save(areaProcessMapper.toEntity(areaProcess));
    return areaProcessMapper.toModel(areaProcessEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public PageableLista<AreaProcess> findAll(AreaProcessFilter filter) {
    Specification<AreaProcessEntity> spec = buildSpecification(filter);

    PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

    var pageableAreaProcess = areaProcessEntityRepository.findAll(spec, pageRequest);

    List<AreaProcess> content = pageableAreaProcess.stream()
        .map(areaProcessMapper::toModel)
        .toList();

    return new PageableLista<>(
        pageableAreaProcess.getNumber(),
        pageableAreaProcess.getSize(),
        pageableAreaProcess.getTotalElements(),
        pageableAreaProcess.getTotalPages(),
        pageableAreaProcess.isLast(),
        pageableAreaProcess.isFirst(),
        content
    );
  }

  private Specification<AreaProcessEntity> buildSpecification(AreaProcessFilter filter) {

    Specification<AreaProcessEntity> spec = (root, query, builder) -> null;

    if (filter.getProcessKey() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("procReleaseKey"), filter.getProcessKey().getValue());
      });
    }

    if (filter.getReleaseId() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("procReleaseId"), filter.getReleaseId().getValue());
      });
    }

    if (filter.getStatus() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("status"), filter.getStatus());
      });
    }

    if (filter.getAreaId() != null) {
      spec = spec.and((root, query, cb) -> {
        Join<AreaProcessEntity, AreaEntity> area = root.join("areaId");
        return cb.equal(area.get("id"), filter.getAreaId().getValue());
      });
    }

    return spec;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<AreaProcess> findById(UUID id) {
    return areaProcessEntityRepository.findById(id).map(areaProcessMapper::toModel);
  }

  @Override
  public void updateStatus(UUID id, Status newStatus) {
    AreaProcessEntity areaProcessEntity = areaProcessEntityRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Area process definition not found. ID: " + id));
    areaProcessEntity.setStatus(newStatus);
    areaProcessEntityRepository.save(areaProcessEntity);
  }

}

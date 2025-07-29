package cv.igrp.platform.process.management.area.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaFilter;
import cv.igrp.platform.process.management.area.domain.repository.AreaRepository;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.AreaEntityRepository;
import jakarta.persistence.criteria.Join;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AreaRepositoryImpl implements AreaRepository {

  private final AreaEntityRepository areaEntityRepository;
  private final AreaMapper areaMapper;

  public AreaRepositoryImpl(AreaEntityRepository areaEntityRepository,
                            AreaMapper areaMapper) {
    this.areaEntityRepository = areaEntityRepository;
    this.areaMapper = areaMapper;
  }

  @Override
  public Area save(Area area) {
    AreaEntity areaEntity = areaEntityRepository.save(areaMapper.toEntity(area));
    return areaMapper.toModel(areaEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public PageableLista<Area> findAll(AreaFilter filter) {

    Specification<AreaEntity> spec = buildSpecification(filter);

    PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

    var pageableAreas = areaEntityRepository.findAll(spec, pageRequest);

    List<Area> content = pageableAreas.stream()
        .map(areaMapper::toModel)
        .toList();

    return new PageableLista<>(
        pageableAreas.getNumber(),
        pageableAreas.getSize(),
        pageableAreas.getTotalElements(),
        pageableAreas.getTotalPages(),
        pageableAreas.isLast(),
        pageableAreas.isFirst(),
        content
    );

  }

  private Specification<AreaEntity> buildSpecification(AreaFilter filter) {

    Specification<AreaEntity> spec = (root, query, builder) -> null;

    if (filter.getCode() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("code"), filter.getCode().getValue());
      });
    }

    if (filter.getName() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("name"), filter.getName().getValue());
      });
    }

    if (filter.getApplicationBase() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("applicationBase"), filter.getApplicationBase().getValue());
      });
    }

    if (filter.getStatus() != null) {
      spec = spec.and((root, query, cb) -> {
        return cb.equal(root.get("status"), filter.getStatus());
      });
    }

    if (filter.getParentId() != null) {
      spec = spec.and((root, query, cb) -> {
        Join<AreaEntity, AreaEntity> parentArea = root.join("areaId");
        return cb.equal(parentArea.get("id"), filter.getParentId().getValue());
      });
    }else{
      spec = spec.and((root, query, cb) -> {
        return cb.isNull(root.get("areaId"));
      });
    }

    return spec;
  }


  @Override
  @Transactional(readOnly = true)
  public Optional<Area> findById(UUID id) {
    return areaEntityRepository.findById(id).map(areaMapper::toModel);
  }

  @Override
  public void updateStatus(UUID id, Status status) {
    AreaEntity areaEntity = areaEntityRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Area not found. ID: " + id));
    areaEntity.setStatus(status);
    areaEntityRepository.save(areaEntity);
  }

}

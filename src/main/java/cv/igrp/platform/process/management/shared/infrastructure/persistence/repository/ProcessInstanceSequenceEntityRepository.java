package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProcessInstanceSequenceEntityRepository extends
    JpaRepository<ProcessInstanceSequenceEntity, UUID>,
    JpaSpecificationExecutor<ProcessInstanceSequenceEntity>
{
  Optional<ProcessInstanceSequenceEntity> findByProcessDefinitionId(String processDefinitionId);
}

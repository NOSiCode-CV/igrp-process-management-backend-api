package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.history.RevisionRepository;

@Repository
public interface ProcessArtifactEntityRepository extends
    JpaRepository<ProcessArtifactEntity, UUID>,
    JpaSpecificationExecutor<ProcessArtifactEntity>,
    RevisionRepository<ProcessArtifactEntity, UUID, Integer>
{

  List<ProcessArtifactEntity> findAllByProcessDefinitionId(String processDefinitionId);

}

package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.history.RevisionRepository;

@Repository
public interface ProcessInstanceEntityRepository extends
    JpaRepository<ProcessInstanceEntity, UUID>,
    JpaSpecificationExecutor<ProcessInstanceEntity>,
    RevisionRepository<ProcessInstanceEntity, UUID, Integer>
{

}
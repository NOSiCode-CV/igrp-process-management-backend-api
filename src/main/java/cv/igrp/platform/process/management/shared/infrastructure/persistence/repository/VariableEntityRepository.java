package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.VariableEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.history.RevisionRepository;

@Repository
public interface VariableEntityRepository extends
    JpaRepository<VariableEntity, UUID>,
    JpaSpecificationExecutor<VariableEntity>,
    RevisionRepository<VariableEntity, UUID, Integer>
{

}
package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInstanceEntityRepository extends
    JpaRepository<TaskInstanceEntity, Integer>,
    JpaSpecificationExecutor<TaskInstanceEntity>,
    RevisionRepository<TaskInstanceEntity, Integer, Integer>
{

}

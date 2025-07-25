package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskInstanceEventEntityRepository extends
    JpaRepository<TaskInstanceEventEntity, UUID>,
    JpaSpecificationExecutor<TaskInstanceEventEntity>,
    RevisionRepository<TaskInstanceEventEntity, UUID, Integer>
{

}

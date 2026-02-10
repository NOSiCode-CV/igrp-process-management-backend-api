package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskPriorityEntity;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.history.RevisionRepository;

@Repository
public interface TaskPriorityEntityRepository extends
    JpaRepository<TaskPriorityEntity, UUID>,
    JpaSpecificationExecutor<TaskPriorityEntity>,
    RevisionRepository<TaskPriorityEntity, UUID, Integer>
{

      default TaskPriorityEntity findByIdOrThrow(UUID id) {
          return this.findById(id)
          .orElseThrow(() -> IgrpResponseStatusException.of(HttpStatus.NOT_FOUND,"TaskPriorityEntity not found for id: " + id));
      }

	List<TaskPriorityEntity> findAllByProcessDefinitionKey(String processDefinitionKey);

}

package cv.igrp.platform.process.management.shared.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceSequenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ProcessInstanceSequenceEntityRepository extends
    JpaRepository<ProcessInstanceSequenceEntity, UUID>,
    JpaSpecificationExecutor<ProcessInstanceSequenceEntity>
{
  Optional<ProcessInstanceSequenceEntity> findByProcessDefinitionKeyAndApplicationCode(
      String processDefinitionKey,
      String applicationCode
  );
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("""
      SELECT s
      FROM ProcessInstanceSequenceEntity s
      WHERE s.processDefinitionKey = :processDefinitionKey
      AND s.applicationCode = :applicationCode
  """)
  Optional<ProcessInstanceSequenceEntity> findForUpdate(
      @Param("processDefinitionKey") String processDefinitionKey,
      @Param("applicationCode") String applicationCode
  );
}

package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProcessInstanceRepositoryImpl implements ProcessInstanceRepository {

  @Override
  public ProcessInstance save(ProcessInstance area) {
    return null;
  }

  @Override
  public PageableLista<ProcessInstance> findAll(ProcessInstanceFilter filter) {
    return null;
  }

  @Override
  public Optional<ProcessInstance> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public void updateStatus(UUID id, ProcessInstanceStatus newStatus) {

  }

}

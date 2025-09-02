package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessSequenceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProcessSequenceService {

  private static final int MAX_RETRIES = 5;
  private static final long RETRY_DELAY_MS = 100;

  private final ProcessSequenceRepository processSequenceRepository;

  public ProcessSequenceService(ProcessSequenceRepository processSequenceRepository) {
    this.processSequenceRepository = processSequenceRepository;
  }


  public ProcessSequence getSequenceByProcessDefinitionId(Code processDefinitionId) {
    return processSequenceRepository.findByProcessDefinitionId(processDefinitionId.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Process Sequence not found for Process Definition ID: " + processDefinitionId.getValue()));
  }


  @Transactional
  public ProcessSequence save(ProcessSequence processSequence) {
    var dbSequence = getAsLockedProcessSequenceByProcessDefinitionId(processSequence.getProcessDefinitionId());
    final ProcessSequence sequenceResult;
    if(dbSequence.isEmpty())
      sequenceResult = processSequence.newInstance();
    else {
      var s = dbSequence.get();
      sequenceResult = processSequence.copyWithId(s.getId());
    }
    sequenceResult.validate();
    return processSequenceRepository.save(sequenceResult);
  }


  public ProcessNumber getGeneratedProcessNumberByProcessDefinitionId(Code processDefinitionId){
    var sequence = getAsLockedProcessSequenceByProcessDefinitionId(processDefinitionId)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Process Sequence not found for Process Definition ID: " + processDefinitionId.getValue()));
    var processNumber = sequence.generateNextProcessNumberAndIncrement();
    processSequenceRepository.save(sequence);
    return processNumber;
  }


  private Optional<ProcessSequence> getAsLockedProcessSequenceByProcessDefinitionId(Code processDefinitionId) {
    int attempts = 0;
    while (true) {
      attempts++;
      try {
        return processSequenceRepository.findByProcessDefinitionIdForUpdate(processDefinitionId.getValue());
      } catch (PessimisticLockException | LockTimeoutException e) {
        if (attempts >= MAX_RETRIES) {
          throw new IllegalStateException("Failed to acquire lock after " + attempts + " attempts", e);
        }
        try {
          Thread.sleep(RETRY_DELAY_MS);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
          throw new IllegalStateException("Interrupted while retrying lock acquisition", ie);
        }
      }
    }
  }


}

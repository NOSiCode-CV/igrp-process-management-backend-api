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


  public ProcessSequence getSequenceByProcessAndApplication(Code processDefinitionKey, Code applicationCode) {
    return processSequenceRepository.findByProcessAndApplication(processDefinitionKey.getValue(), applicationCode.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound(
            "Process Sequence not found for processDefinitionKey[" + processDefinitionKey.getValue()
            + "] and applicationCode["+ applicationCode.getValue() +"]"));
  }


  @Transactional
  public ProcessSequence save(ProcessSequence processSequence) {
    var dbSequence = getProcessSequenceAsLocked(
        processSequence.getProcessDefinitionKey(),
        processSequence.getApplicationCode()
    );
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


  public ProcessNumber getGeneratedProcessNumber(Code processDefinitionKey, Code applicationCode){
    var sequence = getProcessSequenceAsLocked(processDefinitionKey, applicationCode)
        .orElseThrow(() -> IgrpResponseStatusException.notFound(
            "Process Sequence not found for processDefinitionKey[" + processDefinitionKey.getValue()
                + "] and applicationCode["+ applicationCode.getValue() +"]"));
    var processNumber = sequence.generateNextProcessNumberAndIncrement();
    processSequenceRepository.save(sequence);
    return processNumber;
  }


  private Optional<ProcessSequence> getProcessSequenceAsLocked(Code processDefinitionKey, Code applicationCode) {
    int attempts = 0;
    while (true) {
      attempts++;
      try {
        return processSequenceRepository.findForUpdate(processDefinitionKey.getValue(), applicationCode.getValue());
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

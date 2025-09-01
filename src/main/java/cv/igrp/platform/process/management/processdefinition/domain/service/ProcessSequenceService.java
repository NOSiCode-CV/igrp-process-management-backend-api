package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessSequenceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.stereotype.Service;

@Service
public class ProcessSequenceService {

  private final ProcessSequenceRepository processSequenceRepository;

  public ProcessSequenceService(ProcessSequenceRepository processSequenceRepository) {
    this.processSequenceRepository = processSequenceRepository;
  }


  public ProcessSequence getSequenceByProcessDefinitionId(Code processDefinitionId) {
    return processSequenceRepository.getFindByProcessDefinitionId(processDefinitionId.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Process Sequence not found for Process Definition ID: " + processDefinitionId.getValue()));
  }


  public ProcessSequence save(ProcessSequence processSequence) {
    var sequence = processSequenceRepository.getFindByProcessDefinitionId(processSequence.getProcessDefinitionId().getValue());
    final ProcessSequence toSave;
    if(sequence.isEmpty())
      toSave = processSequence.newInstance();
    else {
      var s = sequence.get();
      toSave = processSequence.with(s.getId(), s.getNextNumber());
    }
    return processSequenceRepository.save(toSave);
  }



}

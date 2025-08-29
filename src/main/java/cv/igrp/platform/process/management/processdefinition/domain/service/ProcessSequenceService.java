package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessSequenceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.springframework.stereotype.Service;

@Service
public class ProcessSequenceService {

  private final ProcessSequenceRepository processSequenceRepository;

  public ProcessSequenceService(ProcessSequenceRepository processSequenceRepository) {
    this.processSequenceRepository = processSequenceRepository;
  }


  public ProcessSequence getSequenceById(Identifier id) {
    return processSequenceRepository.getFindById(id.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Process Sequence not found. ID: " + id.getValue()));
  }



}

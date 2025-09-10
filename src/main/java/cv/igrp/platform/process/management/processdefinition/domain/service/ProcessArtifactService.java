package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDefinitionRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessArtifactService {

  private final ProcessDefinitionRepository processDefinitionRepository;


  public ProcessArtifactService(ProcessDefinitionRepository processDefinitionRepository) {
    this.processDefinitionRepository = processDefinitionRepository;
  }

  public ProcessArtifact create(ProcessArtifact processArtifact) {
    return processDefinitionRepository.saveArtifact(processArtifact);
  }

  public List<ProcessArtifact> getArtifactsByProcessDefinitionId(Code processDefinitionId) {
    return processDefinitionRepository.findAllArtifacts(processDefinitionId);
  }

  public void deleteArtifact(Identifier id) {
    ProcessArtifact processArtifact = processDefinitionRepository.findArtifactById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Process Artifact not found. ID: " + id.getValue()));
    processDefinitionRepository.deleteArtifact(processArtifact);
  }

}

package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessDeploymentService {

  private final ProcessDeploymentRepository processDeploymentRepository;

  public ProcessDeploymentService(ProcessDeploymentRepository processDeploymentRepository) {
    this.processDeploymentRepository = processDeploymentRepository;
  }

  public ProcessDeployment deployProcess(ProcessDeployment processDeployment){
    processDeployment.deploy();
    return processDeploymentRepository.deploy(processDeployment);
  }

  public PageableLista<ProcessDeployment> getAllDeployments(ProcessDeploymentFilter processDeploymentFilter){
    return processDeploymentRepository.findAll(processDeploymentFilter);
  }

  public List<ProcessArtifact> getDeployedArtifactsByProcessDefinitionId(Code processDefinitionId) {
    return processDeploymentRepository.findAllArtifacts(processDefinitionId.getValue());
  }

  public String findLatesProcessDefinitionIdByKey(String processDefinitionKey) {
    return processDeploymentRepository.findLatesProcessDefinitionIdByKey(processDefinitionKey);
  }

}

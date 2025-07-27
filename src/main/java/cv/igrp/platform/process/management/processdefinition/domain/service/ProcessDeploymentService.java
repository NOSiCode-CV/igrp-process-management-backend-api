package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import org.springframework.stereotype.Service;

@Service
public class ProcessDeploymentService {

  private final ProcessDeploymentRepository processDeploymentRepository;

  public ProcessDeploymentService(ProcessDeploymentRepository processDeploymentRepository) {
    this.processDeploymentRepository = processDeploymentRepository;
  }

  public ProcessDeployment deployProcess(ProcessDeployment processDeployment){
    return processDeploymentRepository.deploy(processDeployment);
  }

  public void undeployProcess(String deploymentId){

  }

}

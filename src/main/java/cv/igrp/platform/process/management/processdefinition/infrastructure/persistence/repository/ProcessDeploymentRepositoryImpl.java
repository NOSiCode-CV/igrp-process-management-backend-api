package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessDeploymentRepositoryImpl implements ProcessDeploymentRepository {

  @Override
  public ProcessDeployment deploy(ProcessDeployment processDeployment) {

    return processDeployment;
  }

  @Override
  public void undeploy(String deploymentId) {

  }

}

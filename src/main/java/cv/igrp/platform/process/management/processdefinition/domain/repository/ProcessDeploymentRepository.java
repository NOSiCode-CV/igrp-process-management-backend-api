package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;

public interface ProcessDeploymentRepository {

  ProcessDeployment deploy(ProcessDeployment processDeployment);
  void undeploy(String deploymentId);

}

package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

public interface ProcessDeploymentRepository {

  ProcessDeployment deploy(ProcessDeployment processDeployment);
  void undeploy(String deploymentId);
  PageableLista<ProcessDeployment> findAll(ProcessDeploymentFilter processDeploymentFilter);

}

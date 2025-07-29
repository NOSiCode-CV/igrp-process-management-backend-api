package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ProcessDeploymentRepositoryImpl implements ProcessDeploymentRepository {

  @Override
  public ProcessDeployment deploy(ProcessDeployment processDeployment) {

    return processDeployment;
  }

  @Override
  public void undeploy(String deploymentId) {

  }

  @Override
  public PageableLista<ProcessDeployment> findAll(Code applicationCode) {
    ArrayList<ProcessDeployment> deployments = new ArrayList<>();
    // Go to activiti engine here
    // ...
    return new PageableLista<>(
        0,
        50,
        0L,
        0,
        false,
        false,
        deployments
    );
  }

}

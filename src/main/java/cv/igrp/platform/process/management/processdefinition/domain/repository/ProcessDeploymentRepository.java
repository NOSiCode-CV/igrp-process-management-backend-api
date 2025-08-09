package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.exception.ProcessDeploymentException;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

/**
 * Repository interface for deploying and querying process definitions or deployments.
 */
public interface ProcessDeploymentRepository {

  /**
   * Deploys a new process deployment.
   *
   * @param processDeployment the process deployment entity to deploy
   * @return the deployed {@link ProcessDeployment} instance
   * @throws ProcessDeploymentException if deployment fails
   */
  ProcessDeployment deploy(ProcessDeployment processDeployment) throws ProcessDeploymentException;

  /**
   * Finds all process deployments matching the given filter criteria.
   *
   * @param processDeploymentFilter the filter criteria
   * @return a pageable list of matching {@link ProcessDeployment} instances
   */
  PageableLista<ProcessDeployment> findAll(ProcessDeploymentFilter processDeploymentFilter);

}

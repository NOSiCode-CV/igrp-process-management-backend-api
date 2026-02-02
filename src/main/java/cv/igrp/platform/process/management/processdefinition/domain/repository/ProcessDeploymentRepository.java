package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.exception.ProcessDeploymentException;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.List;
import java.util.Optional;

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

  /**
   * Retrieves all artifacts associated with a given process definition.
   *
   * @param processDefinitionId the unique identifier of the process definition
   * @return a list of {@link ProcessArtifact} instances linked to the specified process definition
   */
  List<ProcessArtifact> findAllArtifacts(String processDefinitionId);


  /**
   * Retrieves the unique identifier of the most recent process definition based on the specified process definition key.
   *
   * @param processDefinitionKey the key representing the process definition
   * @return the unique identifier of the latest process definition associated with the provided key
   */
  String findLastProcessDefinitionIdByKey(String processDefinitionKey);

  /**
   * Adds a candidate starter group to a specified process definition.
   *
   * @param processDefinitionId the unique identifier of the process definition to which the group will be added
   * @param groupId the unique identifier of the group to be added as a candidate starter for the process definition
   */
  void addCandidateStarterGroup(String processDefinitionId, String groupId);

  /**
   * Retrieves a process deployment record by its unique identifier.
   *
   * @param id the unique identifier of the process deployment to find
   * @return an {@link Optional} containing the found {@link ProcessDeployment}, or an empty {@link Optional}
   *         if no deployment is found with the specified identifier
   */
  Optional<ProcessDeployment> findById(String id);

}

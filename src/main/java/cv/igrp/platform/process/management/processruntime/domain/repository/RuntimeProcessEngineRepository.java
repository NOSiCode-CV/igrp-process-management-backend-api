package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.exception.RuntimeProcessEngineException;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;

import java.util.List;
import java.util.Map;

/**
 * Repository interface defining runtime operations for process engine execution.
 * <p>
 * This interface abstracts the interaction with a BPM engine, providing methods
 * to start processes, retrieve process and task information, and manage task assignments.
 * </p>
 *
 * <p>
 * All methods may throw {@link RuntimeProcessEngineException} in case of errors.
 * </p>
 */
public interface RuntimeProcessEngineRepository {

  /**
   * Starts a new process instance using the given process definition ID.
   *
   * @param processDefinitionId the unique identifier of the process definition
   * @param businessKey         a business-specific correlation key (may be {@code null})
   * @param variables           initial process variables (may be empty or {@code null})
   * @return the created {@link ProcessInstance}
   * @throws RuntimeProcessEngineException if the process cannot be started
   */
  ProcessInstance startProcessInstanceById(
      String processDefinitionId,
      String businessKey,
      Map<String, Object> variables
  ) throws RuntimeProcessEngineException;

  /**
   * Retrieves a process instance by its unique identifier.
   *
   * @param processInstanceId the unique identifier of the process instance
   * @return the {@link ProcessInstance} if found
   * @throws RuntimeProcessEngineException if the process instance does not exist or cannot be retrieved
   */
  ProcessInstance getProcessInstanceById(String processInstanceId)
      throws RuntimeProcessEngineException;

  /**
   * Retrieves the status of all tasks belonging to a process instance.
   *
   * @param processInstanceId the unique identifier of the process instance
   * @return a list of {@link ProcessInstanceTaskStatus} representing task statuses
   * @throws RuntimeProcessEngineException if the process instance cannot be found or accessed
   */
  List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId)
      throws RuntimeProcessEngineException;

  /**
   * Retrieves all currently active task instances for a process instance.
   *
   * @param processInstanceId the unique identifier of the process instance
   * @return a list of active {@link TaskInstance} objects
   * @throws RuntimeProcessEngineException if the process instance cannot be found or accessed
   */
  List<TaskInstance> getActiveTaskInstances(String processInstanceId)
      throws RuntimeProcessEngineException;

  /**
   * Completes a given task and optionally updates process variables.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @param variables      variables to update when completing the task (may be {@code null})
   * @throws RuntimeProcessEngineException if the task cannot be completed
   */
  void completeTask(String taskInstanceId, Map<String, Object> variables)
      throws RuntimeProcessEngineException;

  /**
   * Claims a task on behalf of a specific user.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @param userId         the unique identifier of the user claiming the task
   * @throws RuntimeProcessEngineException if the task cannot be claimed
   */
  void claimTask(String taskInstanceId, String userId) throws RuntimeProcessEngineException;

  /**
   * Releases a previously claimed task, making it available to other users.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @throws RuntimeProcessEngineException if the task cannot be unclaimed
   */
  void unClaimTask(String taskInstanceId) throws RuntimeProcessEngineException;

  /**
   * Assigns a task directly to a user, overriding any existing claim.
   *
   * @param taskId the unique identifier of the task
   * @param userId the unique identifier of the user
   * @param reason the reason for assignment (optional, may be {@code null})
   * @throws RuntimeProcessEngineException if the task cannot be assigned
   */
  void assignTask(String taskId, String userId, String reason) throws RuntimeProcessEngineException;

  /**
   * Retrieves all variables associated with a task.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @return a map of variable names to values (never {@code null}, may be empty)
   * @throws RuntimeProcessEngineException if the variable cannot be retrieved
   */
  Map<String, Object> getTaskVariables(String taskInstanceId)
      throws RuntimeProcessEngineException;

  /**
   * Retrieves all variables associated with a process instance.
   *
   * @param processInstanceId the unique identifier of the process instance
   * @return a map of variable names to values (never {@code null}, may be empty)
   * @throws RuntimeProcessEngineException if the variables cannot be retrieved
   */
  Map<String, Object> getProcessVariables(String processInstanceId)
      throws RuntimeProcessEngineException;

}

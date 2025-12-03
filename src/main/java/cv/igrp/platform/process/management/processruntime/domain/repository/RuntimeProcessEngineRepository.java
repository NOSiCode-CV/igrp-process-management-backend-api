package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.platform.process.management.processruntime.domain.exception.RuntimeProcessEngineException;
import cv.igrp.platform.process.management.processruntime.domain.models.*;

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
   * Starts a new process instance using the given process instance ID.
   *
   * @param processInstanceId the unique identifier of the process instance
   * @param processDefinitionId the unique identifier of the process definition
   * @param businessKey         a business-specific correlation key (may be {@code null})
   * @param variables           initial process variables (may be empty or {@code null})
   * @return the created {@link ProcessInstance}
   * @throws RuntimeProcessEngineException if the process cannot be started
   */
  ProcessInstance startProcessInstanceById(
      String processInstanceId,
      String processDefinitionId,
      String businessKey,
      Map<String, Object> variables
  ) throws RuntimeProcessEngineException;

  /**
   * Creates a new process instance using the given process definition ID.
   *
   * @param processDefinitionId the unique identifier of the process definition
   * @param businessKey         a business-specific correlation key (may be {@code null})
   * @return the created {@link ProcessInstance}
   * @throws RuntimeProcessEngineException if the process cannot be created
   */
  ProcessInstance createProcessInstanceById(
      String processDefinitionId,
      String businessKey
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
   * Retrieves a process instance by business key.
   *
   * @param businessKey the business key
   * @return the {@link ProcessInstance} if found
   * @throws RuntimeProcessEngineException if the process instance does not exist or cannot be retrieved
   */
  ProcessInstance getProcessInstanceByBusinessKey(String businessKey)
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
   * Saves a given task and updates process variables.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @param forms      forms to update when saving the task (may be {@code null})
   * @param variables      variables to update when saving the task (may be {@code null})
   * @throws RuntimeProcessEngineException if the task cannot be saved
   */
  void saveTask(String taskInstanceId, Map<String, Object> forms, Map<String, Object> variables)
      throws RuntimeProcessEngineException;

  /**
   * Completes a given task and optionally updates process variables.
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @param forms      forms to update when completing the task (may be {@code null})
   * @param variables      variables to update when completing the task (may be {@code null})
   * @throws RuntimeProcessEngineException if the task cannot be completed
   */
  void completeTask(String taskInstanceId, Map<String, Object> forms, Map<String, Object> variables)
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

  /**
   * Updates the priority of a task.
   *
   * <p>
   * Task priority is an integer value that can be used to influence
   * task ordering, escalation, or custom business logic. The default
   * priority is typically {@code 50}, but any integer value is allowed.
   * </p>
   *
   * @param taskInstanceId the unique identifier of the task instance
   * @param priority       the new priority value to set
   * @throws RuntimeProcessEngineException if the task cannot be found or the priority cannot be updated
   */
  void setTaskPriority(String taskInstanceId, int priority) throws RuntimeProcessEngineException;

  /**
   * Correlates a message to a process instance.
   * @param messageName
   * @param businessKey
   * @param variables
   * @throws RuntimeProcessEngineException
   */
  void correlateMessage(String messageName, String businessKey, Map<String, Object> variables) throws RuntimeProcessEngineException;

  /**
   * Signals a process instance.
   * @param processInstanceId
   * @param taskId
   * @param variables
   * @throws RuntimeProcessEngineException
   */
  void signal(String processInstanceId, String taskId, Map<String, Object> variables) throws RuntimeProcessEngineException;

  /**
   * Retrieves the process definition representation by its ID.
   *
   * @param processDefinitionId the unique identifier of the process definition
   * @return the {@link ProcessDefinitionRepresentation}
   */
  ProcessDefinitionRepresentation getProcessDefinition(String processDefinitionId);

  /**
   * Retrieves the activity representation by its ID
   *
   * @param activityId the unique identifier for the activity
   * @return the {@link ActivityData} if present
   */
  ActivityData getActivityById(String activityId);

  /**
   * Retrieve the activity variables through the activity's ID
   *
   * @param activityId the unique identifier for the activity
   * @return a map of variables
   */
  Map<String, Object> getActivityVariables(String activityId);

  /**
   * Retrieve the active activity instances by process instance's ID
   *
   * @param processInstanceId the unique identifier for the process instance
   * @param type the activity type filter
   * @return a list of {@link ActivityData}
   */
  List<ActivityData> getActiveActivityInstances(String processInstanceId, IGRPActivityType type);

  /**
   * Retrieve the activity progress by process instance's ID
   *
   * @param processInstanceId the unique identifier for the process instance
   * @param type the activity type filter
   * @return a list of {@link ProcessActivityInfo}
   */
  List<ProcessActivityInfo> getActivityProgress(String processInstanceId, IGRPActivityType type);

  /**
   * Retrieve all process instances by variables expressions.
   * @param variablesExpressions the list of variables expressions
   * @return a list of {@link ProcessInstance}
   */
  List<ProcessInstance> getAllProcessInstancesByVariables(List<VariablesExpression> variablesExpressions);

  /**
   * Retrieve all task instances by variables expressions.
   * @param variablesExpressions the list of variables expressions
   * @return a list of {@link TaskInstance}
   */
  List<TaskInstance> getAllTaskInstancesByVariables(List<VariablesExpression> variablesExpressions);


  /**
   * Add a candidate group to a task.
   *
   * @param taskId the unique identifier of the task
   * @param groupId the unique identifier of the candidate group
   * @throws RuntimeProcessEngineException if the task cannot be found or the candidate group cannot be added
   */
  void addCandidateGroup(String taskId, String groupId) throws RuntimeProcessEngineException;

}

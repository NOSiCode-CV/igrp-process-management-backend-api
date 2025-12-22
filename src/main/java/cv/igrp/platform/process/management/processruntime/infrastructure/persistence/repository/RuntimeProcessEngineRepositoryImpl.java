package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.framework.runtime.core.engine.activity.ActivityQueryService;
import cv.igrp.framework.runtime.core.engine.activity.model.ActivityInfo;
import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.ProcessManagerAdapter;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessVariableInstance;
import cv.igrp.framework.runtime.core.engine.process.model.TaskFilter;
import cv.igrp.framework.runtime.core.engine.process.model.VariablesOperator;
import cv.igrp.framework.runtime.core.engine.task.TaskActionService;
import cv.igrp.framework.runtime.core.engine.task.TaskQueryService;
import cv.igrp.framework.runtime.core.engine.task.model.TaskInfo;
import cv.igrp.framework.runtime.core.engine.task.model.TaskVariableInstance;
import cv.igrp.platform.process.management.processruntime.domain.exception.RuntimeProcessEngineException;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceTaskStatusMapper;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Implementation of {@link RuntimeProcessEngineRepository} that delegates
 * process and task operations to the underlying process engine adapters and services.
 */
@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessEngineRepositoryImpl.class);

  private final ProcessDefinitionAdapter processDefinitionAdapter;
  private final ProcessManagerAdapter processManagerAdapter;
  private final ProcessInstanceMapper processInstanceMapper;
  private final TaskInstanceMapper taskInstanceMapper;
  private final TaskActionService taskActionService;
  private final ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper;
  private final TaskQueryService taskQueryService;
  private final ActivityQueryService activityQueryService;

  public RuntimeProcessEngineRepositoryImpl(
      ProcessDefinitionAdapter processDefinitionAdapter,
      ProcessManagerAdapter processManagerAdapter,
      ProcessInstanceMapper processInstanceMapper,
      TaskInstanceMapper taskInstanceMapper,
      TaskActionService taskActionService,
      ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper,
      TaskQueryService taskQueryService,
      ActivityQueryService activityQueryService
  ) {
    this.processDefinitionAdapter = processDefinitionAdapter;
    this.processManagerAdapter = processManagerAdapter;
    this.processInstanceMapper = processInstanceMapper;
    this.taskInstanceMapper = taskInstanceMapper;
    this.taskActionService = taskActionService;
    this.processInstanceTaskStatusMapper = processInstanceTaskStatusMapper;
    this.taskQueryService = taskQueryService;
    this.activityQueryService = activityQueryService;
  }

  @Override
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables) {
    LOGGER.info("Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      var processInstance = processManagerAdapter.startProcess(processDefinitionId, businessKey, variables);
      LOGGER.info("Process started with ID: {}", processInstance.id());
      return processInstanceMapper.toModel(processInstance);
    } catch (Exception e) {
      LOGGER.error("Failed to start process with definition ID: {}", processDefinitionId, e);
      throw new RuntimeProcessEngineException("Failed to start process", e);
    }
  }

  @Override
  public ProcessInstance startProcessInstanceById(String processInstanceId, String processDefinitionId, String businessKey, Map<String, Object> variables) throws RuntimeProcessEngineException {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    LOGGER.info("Starting process. user={}, processInstanceId={}, processDefinitionId={}",
        auth, processInstanceId, processDefinitionId);
    try {
      var processInstance = processManagerAdapter.startCreatedProcess(
          processInstanceId,
          processDefinitionId,
          businessKey,
          variables
      );
      LOGGER.info("Process started successfully. instanceId={}, definitionId={}",
          processInstance.id(), processDefinitionId);
      return processInstanceMapper.toModel(processInstance);
    } catch (Exception e) {
      LOGGER.error(
          "Error starting process. processInstanceId={}, processDefinitionId={}, message={}",
          processInstanceId, processDefinitionId, e.getMessage(), e
      );
      throw new RuntimeProcessEngineException("Failed to start process", e);
    }
  }

  @Override
  public ProcessInstance createProcessInstanceById(String processDefinitionId, String businessKey) throws RuntimeProcessEngineException {
    LOGGER.info("Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      var processInstance = processManagerAdapter.createProcess(processDefinitionId, businessKey);
      LOGGER.info("Process created by user: {}", processInstance.initiator());
      LOGGER.info("Process created with ID: {}", processInstance.id());
      return processInstanceMapper.toModel(processInstance);
    } catch (Exception e) {
      LOGGER.error("Failed to create process with definition ID: {}", processDefinitionId, e);
      throw new RuntimeProcessEngineException("Failed to create process", e);
    }
  }

  @Override
  public ProcessInstance getProcessInstanceById(String processInstanceId) {
    try {
      return processManagerAdapter
          .getProcessInstance(processInstanceId)
          .map(processInstanceMapper::toModel)
          .orElseThrow(() ->
              new RuntimeProcessEngineException("Process instance not found: " + processInstanceId)
          );
    } catch (RuntimeProcessEngineException e) {
      throw e; // rethrow custom exception
    } catch (Exception e) {
      LOGGER.error("Error retrieving process instance: {}", processInstanceId, e);
      throw new RuntimeProcessEngineException("Error retrieving process instance: " + processInstanceId, e);
    }
  }

  @Override
  public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
    try {
      return processManagerAdapter
          .getProcessInstanceByBusinessKey(businessKey)
          .map(processInstanceMapper::toModel)
          .orElseThrow(() ->
              new RuntimeProcessEngineException("Process instance not found for business key: " + businessKey)
          );
    } catch (RuntimeProcessEngineException e) {
      throw e; // rethrow custom exception
    } catch (Exception e) {
      LOGGER.error("Error retrieving process instance by business key: {}", businessKey, e);
      throw new RuntimeProcessEngineException("Error retrieving process instance by business key: " + businessKey, e);
    }
  }

  @Override
  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId) {
    try {
      return taskQueryService.getUserTaskProgress(processInstanceId).stream()
          .map(processInstanceTaskStatusMapper::toModel)
          .collect(Collectors.toList());
    } catch (Exception e) {
      LOGGER.error("Failed to retrieve task list for process instance ID: {}", processInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to retrieve task list", e);
    }
  }

  @Override
  public List<TaskInstance> getActiveTaskInstances(String processInstanceId) {
    try {
      return taskQueryService.getActiveTaskInstances(processInstanceId).stream()
          .map(taskInstanceMapper::toModel)
          .collect(Collectors.toList());
    } catch (Exception e) {
      LOGGER.error("Failed to retrieve active tasks for process instance ID: {}", processInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to retrieve active tasks", e);
    }
  }

  @Override
  public void saveTask(String taskInstanceId, Map<String, Object> forms, Map<String, Object> variables) {
    try {
      TaskInfo taskInfo = taskQueryService.getTask(taskInstanceId).orElseThrow();
      processManagerAdapter.setProcessVariables(taskInfo.processInstanceId(), variables);
      taskActionService.saveTask(taskInstanceId, forms);
    } catch (Exception e) {
      LOGGER.error("Failed to save task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to save task. " + e.getMessage(), e);
    }
  }

  @Override
  public void completeTask(String taskInstanceId, Map<String, Object> forms, Map<String, Object> variables) {
    try {
      TaskInfo taskInfo = taskQueryService.getTask(taskInstanceId).orElseThrow();
      processManagerAdapter.setProcessVariables(taskInfo.processInstanceId(), variables);
      taskActionService.completeTask(taskInstanceId, forms);
    } catch (Exception e) {
      LOGGER.error("Failed to complete task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to complete task. " + e.getMessage(), e);
    }
  }

  @Override
  public void claimTask(String taskInstanceId, String userId) {
    try {
      taskActionService.claimTask(taskInstanceId, userId);
    } catch (Exception e) {
      LOGGER.error("Failed to claim task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to claim task. " + e.getMessage(), e);
    }
  }

  @Override
  public void unClaimTask(String taskInstanceId) {
    try {
      taskActionService.unclaimTask(taskInstanceId);
    } catch (Exception e) {
      LOGGER.error("Failed to unclaim task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to unclaim task. " + e.getMessage(), e);
    }
  }

  @Override
  public void assignTask(String taskId, String userId, String reason) {
    try {
      taskActionService.assignTask(taskId, userId, reason);
    } catch (Exception e) {
      LOGGER.error("Failed to assign task: {}", taskId, e);
      throw new RuntimeProcessEngineException("Failed to assign task. " + e.getMessage(), e);
    }
  }

  @Override
  public Map<String, Object> getTaskVariables(String taskInstanceId) {
    try {
      List<TaskVariableInstance> variables = taskQueryService.getTaskVariables(taskInstanceId);
      return variables.stream()
          .collect(Collectors.toMap(TaskVariableInstance::name, TaskVariableInstance::value));
    } catch (Exception e) {
      LOGGER.error("Failed to retrieve variables for task with id={}", taskInstanceId, e);
      return Map.of();
    }
  }

  @Override
  public Map<String, Object> getProcessVariables(String processInstanceId) {
    try {
      List<ProcessVariableInstance> variables = processManagerAdapter.getProcessVariables(processInstanceId);
      if (variables == null) return new HashMap<>();//todo remove
      return variables.stream()
          .filter(p -> p.name() != null && p.value() != null) //todo fix
          .collect(Collectors.toMap(ProcessVariableInstance::name, ProcessVariableInstance::value));
    } catch (Exception e) {
      LOGGER.error("Failed to retrieve variables for process with id={}", processInstanceId, e);
      return Map.of();
    }
  }

  @Override
  public void setTaskPriority(String taskInstanceId, int priority) throws RuntimeProcessEngineException {
    try {
      taskActionService.setTaskPriority(taskInstanceId, priority);
    } catch (Exception e) {
      LOGGER.error("Failed to set priority for task with id={}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Unable to set priority for task: " + taskInstanceId, e);
    }
  }

  @Override
  public void correlateMessage(String messageName, String businessKey, Map<String, Object> variables) throws RuntimeProcessEngineException {
    try {
      processManagerAdapter.correlateMessage(businessKey, messageName, variables);
    } catch (Exception e) {
      LOGGER.error("Failed to correlate message '{}' for businessKey '{}': {}", messageName, businessKey, e.getMessage(), e);
      throw new RuntimeProcessEngineException("Failed to correlate message '" + messageName + "' for businessKey '" + businessKey + "'", e);
    }
  }

  @Override
  public void signal(String processInstanceId, String taskId, Map<String, Object> variables) throws RuntimeProcessEngineException {
    try {
      processManagerAdapter.signal(processInstanceId, taskId, variables);
    } catch (Exception e) {
      LOGGER.error("Failed to signal process instance '{}' with variables: {}", processInstanceId, variables, e);
      throw new RuntimeProcessEngineException(
          "Failed to signal process instance with ID '" + processInstanceId + "'.", e
      );
    }
  }

  @Override
  public ProcessDefinitionRepresentation getProcessDefinition(String processDefinitionId) {
    try {
      return processDefinitionAdapter
          .getProcessDefinition(processDefinitionId);
    } catch (Exception e) {
      LOGGER.error("Error retrieving process definition by Id: {}", processDefinitionId, e);
      throw new RuntimeProcessEngineException("Error retrieving process definition by ID: " + processDefinitionId, e);
    }
  }

  @Override
  public ActivityData getActivityById(String activityId) {
    ActivityInfo info = activityQueryService
        .getActivity(activityId)
        .orElseThrow(() ->
            new RuntimeProcessEngineException(
                "No activity found with id: " + activityId
            )
        );
    return ActivityData.builder()
        .id(Code.create(info.id()))
        .name(Name.create(info.name()))
        .description(info.description())
        .processInstanceId(Code.create(info.processInstanceId()))
        .parentId(Code.create(info.parentId()))
        .parentProcessInstanceId(
            Code.create(info.parentProcessInstanceId())
        )
        .status(info.status())
        .type(info.type())
        .build();
  }

  @Override
  public Map<String, Object> getActivityVariables(String activityId) {

    var variables = activityQueryService.getActivityVariables(activityId);

    var variablesMap = new HashMap<String, Object>();

    if (variables == null || variables.isEmpty()) return variablesMap;

    variables.forEach(variable -> variablesMap.put(variable.name(), variable.value()));

    return variablesMap;

  }

  @Override
  public List<ActivityData> getActiveActivityInstances(String processInstanceId, IGRPActivityType activityType) {

    List<ActivityInfo> activityInfos = activityQueryService.getActiveActivityInstances(processInstanceId);

    System.out.println("processInstanceId: " + processInstanceId);
    System.out.println("ActivityInfos(Teste): " + activityInfos);

    return activityInfos
        .stream()
        .filter(a -> activityType == null || Objects.equals(a.type(), activityType))
        .map(
            info -> ActivityData.builder()
                .id(Code.create(info.id()))
                .name(Name.create(info.name()))
                .description(info.description())
                .processInstanceId(Code.create(info.processInstanceId()))
                .parentId(Code.create(info.parentId()))
                .parentProcessInstanceId(Code.create(processInstanceId))
                .status(info.status())
                .type(info.type())
                .build()
        ).toList();
  }

  @Override
  public List<ProcessActivityInfo> getActivityProgress(String processInstanceId, IGRPActivityType type) {
    return activityQueryService.getActivityProgress(processInstanceId)
        .stream()
        .filter(a -> type == null || Objects.equals(a.type(), type))
        .toList();
  }

  @Override
  public void addCandidateGroup(String taskId, String groupId) throws RuntimeProcessEngineException {
    try {
      taskActionService.addCandidateGroup(taskId, groupId);
      LOGGER.info("Added candidate group '{}' to task '{}'", groupId, taskId);
    } catch (Exception e) {
      LOGGER.error("Failed to add candidate group '{}' to task '{}'", groupId, taskId, e);
      throw new RuntimeProcessEngineException(
          String.format("Unable to add candidate group '%s' to task '%s'", groupId, taskId), e
      );
    }
  }

  @Override
  public List<ProcessInstance> getAllProcessInstancesByVariables(List<VariablesExpression> variablesExpressions) {
    if (variablesExpressions == null || variablesExpressions.isEmpty())
      return Collections.emptyList();
    ProcessFilter filter = new ProcessFilter();
    variablesExpressions.forEach(vE -> {
      filter.getVariablesExpressions().add(
          new cv.igrp.framework.runtime.core.engine.process.model.VariablesExpression(
              vE.getName(),
              VariablesOperator.valueOf(vE.getOperator().name()),
              vE.getValue())
      );
    });
    return processManagerAdapter.listProcessInstances(filter)
        .stream()
        .map(processInstanceMapper::toModel)
        .collect(Collectors.toList());
  }

  @Override
  public List<TaskInstance> getAllTaskInstancesByVariables(List<VariablesExpression> variablesExpressions) {

    if (variablesExpressions == null || variablesExpressions.isEmpty())
      return Collections.emptyList();

    TaskFilter filter = new TaskFilter();

    variablesExpressions.forEach(vE -> {
      filter.getVariablesExpressions().add(
          new cv.igrp.framework.runtime.core.engine.process.model.VariablesExpression(
              vE.getName(),
              VariablesOperator.valueOf(vE.getOperator().name()),
              vE.getValue())
      );
    });

    return taskQueryService.listTaskInstances(filter)
        .stream()
        .map(taskInstanceMapper::toModel)
        .collect(Collectors.toList());
  }

  @Override
  public void rescheduleTimer(String processInstanceId, long seconds) {
    try {
      processManagerAdapter.rescheduleTimer(processInstanceId, seconds);
      LOGGER.info("Rescheduled timer for process instance '{}' in {} seconds", processInstanceId, seconds);
    } catch (Exception e) {
      LOGGER.error("Failed to reschedule timer for process instance '{}'", processInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to reschedule timer for process instance: " + processInstanceId, e);
    }
  }

  @Override
  public void rescheduleTimer(String processInstanceId, String timerElementId, long seconds) {
    try {
      processManagerAdapter.rescheduleTimer(processInstanceId, timerElementId, seconds);
      LOGGER.info("Timer '{}' for process instance '{}' successfully rescheduled to fire after {} seconds", timerElementId, processInstanceId, seconds);
    } catch (Exception e) {
      LOGGER.error("Failed to reschedule timer '{}' for process instance '{}'", timerElementId, processInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to reschedule timer '" + timerElementId + "' for process instance '" + processInstanceId + "'", e);
    }
  }

}

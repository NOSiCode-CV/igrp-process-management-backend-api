package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.exception.RuntimeProcessEngineException;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceTaskStatusMapper;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.SecurityUtil;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import cv.nosi.igrp.runtime.core.engine.task.TaskActionService;
import cv.nosi.igrp.runtime.core.engine.task.TaskQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Implementation of {@link RuntimeProcessEngineRepository} that delegates
 * process and task operations to the underlying process engine adapters and services.
 */
@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessEngineRepositoryImpl.class);

  private final ProcessManagerAdapter processManagerAdapter;
  private final SecurityUtil securityUtil;
  private final ProcessInstanceMapper processInstanceMapper;
  private final TaskInstanceMapper taskInstanceMapper;
  private final TaskActionService taskActionService;
  private final ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper;
  private final TaskQueryService taskQueryService;

  public RuntimeProcessEngineRepositoryImpl(
      ProcessManagerAdapter processManagerAdapter,
      SecurityUtil securityUtil,
      ProcessInstanceMapper processInstanceMapper,
      TaskInstanceMapper taskInstanceMapper,
      TaskActionService taskActionService,
      ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper,
      TaskQueryService taskQueryService
  ) {
    this.processManagerAdapter = processManagerAdapter;
    this.securityUtil = securityUtil;
    this.processInstanceMapper = processInstanceMapper;
    this.taskInstanceMapper = taskInstanceMapper;
    this.taskActionService = taskActionService;
    this.processInstanceTaskStatusMapper = processInstanceTaskStatusMapper;
    this.taskQueryService = taskQueryService;
  }

  @Override
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables) {
    securityUtil.logInAs("joao");
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
  public void completeTask(String taskInstanceId, Map<String, Object> variables) {
    securityUtil.logInAs("demo@nosi.cv");
    try {
      taskActionService.completeTask(taskInstanceId, variables, "user");
    } catch (Exception e) {
      LOGGER.error("Failed to complete task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to complete task", e);
    }
  }

  @Override
  public void claimTask(String taskInstanceId, String userId) {
    securityUtil.logInAs("demo@nosi.cv");
    try {
      taskActionService.claimTask(taskInstanceId, userId);
    } catch (Exception e) {
      LOGGER.error("Failed to claim task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to claim task", e);
    }
  }

  @Override
  public void unClaimTask(String taskInstanceId) {
    securityUtil.logInAs("demo@nosi.cv");
    try {
      taskActionService.unclaimTask(taskInstanceId);
    } catch (Exception e) {
      LOGGER.error("Failed to unclaim task: {}", taskInstanceId, e);
      throw new RuntimeProcessEngineException("Failed to unclaim task", e);
    }
  }

  @Override
  public void assignTask(String taskId, String userId, String reason) {
    securityUtil.logInAs("demo@nosi.cv");
    try {
      taskActionService.assignTask(taskId, userId, reason);
    } catch (Exception e) {
      LOGGER.error("Failed to assign task: {}", taskId, e);
      throw new RuntimeProcessEngineException("Failed to assign task", e);
    }
  }

}

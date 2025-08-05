package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

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
import cv.nosi.igrp.runtime.core.engine.task.model.TaskFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {


  private final ProcessManagerAdapter processManagerAdapter;
  private final SecurityUtil securityUtil;
  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessEngineRepositoryImpl.class);

  private final ProcessInstanceMapper processInstanceMapper;

  private final TaskInstanceMapper taskInstanceMapper;

  private final TaskActionService taskActionService;
  private final ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper;

  private final TaskQueryService taskQueryService;

  public RuntimeProcessEngineRepositoryImpl(ProcessManagerAdapter processManagerAdapter, SecurityUtil securityUtil, ProcessInstanceMapper processInstanceMapper,
                                            TaskInstanceMapper taskInstanceMapper,
                                            TaskActionService taskActionService, ProcessInstanceTaskStatusMapper processInstanceTaskStatusMapper, TaskQueryService taskQueryService) {
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

     // Set authentication context
    securityUtil.logInAs("joao");

    LOGGER.info("Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      var processInstance = processManagerAdapter.startProcess(processDefinitionId, businessKey, variables);
      LOGGER.info("Process started with ID: {}", processInstance.getId());
      return processInstanceMapper.toModel(processInstance);
    } catch (Exception e) {
      LOGGER.error("Erro ao iniciar processo", e);
      throw new RuntimeException("Falha ao iniciar processo", e);
    }
  }

  @Override
  public ProcessInstance getProcessInstanceById(String processInstanceId) {
    try {
      return processManagerAdapter
          .getProcessInstance(processInstanceId)
          .map(processInstanceMapper::toModel)
          .orElse(null);
    } catch (Exception e) {
      LOGGER.error("Erro ao obter a instância de processo com ID: {}", processInstanceId, e);
      throw new RuntimeException("Falha ao obter instância de processo", e);
    }
  }

  @Override
  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId) {
    try {


      var tasks = taskQueryService.getAllTasks(processInstanceId);

      return tasks.stream()
          .map(processInstanceTaskStatusMapper::toModel)
          .collect(Collectors.toList());


    } catch (Exception e) {
      LOGGER.error("Erro ao obter tarefas da instância: {}", processInstanceId, e);
      throw new RuntimeException("Erro ao obter tarefas", e);
    }
  }

  @Override
  public List<TaskInstance> getActiveTaskInstances(String processInstanceId) {
    try {

      return taskQueryService.getActiveTaskInstances(processInstanceId).stream()
          .map(taskInstanceMapper::toModel)
          .collect(Collectors.toList());

    } catch (Exception e) {
      LOGGER.error("Erro ao obter tarefas da instância: {}", processInstanceId, e);
      throw new RuntimeException("Erro ao obter tarefas", e);
    }
  }


  @Override
  public void completeTask(String taskInstanceId, Map<String, Object> variables) {

    taskActionService.completeTask(taskInstanceId, variables, "user");
  }




}

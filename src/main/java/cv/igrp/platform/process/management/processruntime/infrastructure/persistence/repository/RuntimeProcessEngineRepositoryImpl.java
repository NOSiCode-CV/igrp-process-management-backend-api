package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.SecurityUtil;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {


  private final ProcessManagerAdapter processManagerAdapter;
  private final SecurityUtil securityUtil;
  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessEngineRepositoryImpl.class);

  private final ProcessInstanceMapper processInstanceMapper;


  public RuntimeProcessEngineRepositoryImpl(ProcessManagerAdapter processManagerAdapter, SecurityUtil securityUtil, ProcessInstanceMapper processInstanceMapper) {
    this.processManagerAdapter = processManagerAdapter;
    this.securityUtil = securityUtil;
    this.processInstanceMapper = processInstanceMapper;
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
    return null;
  }

  @Override
  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId) {
    return List.of();
  }

  @Override
  public List<TaskInstance> getActiveTaskInstances(String processInstanceId) {
    return List.of();
  }

  @Override
  public List<TaskInstance> getTaskInstances(String processInstanceId) {
    return List.of();
  }

  @Override
  public void completeTask(String taskInstanceId, Map<String, Object> variables) {

  }

  @Override
  public TaskInstance getTaskInstance(String taskInstanceId) {
    return null;
  }
  /*@Override
  public String startProcessInstanceByKeyT(String processKey, String businessKey, Map<String, Object> variables) {

    // Set authentication context
    securityUtil.logInAs("joao");

    LOGGER.info("Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      i bn processManagerAdapter.startProcess(processKey, businessKey, variables);
    } catch (Exception e) {
      LOGGER.error("Erro ao iniciar processo", e);
      throw new RuntimeException("Falha ao iniciar processo", e);
    }
  }*/




}

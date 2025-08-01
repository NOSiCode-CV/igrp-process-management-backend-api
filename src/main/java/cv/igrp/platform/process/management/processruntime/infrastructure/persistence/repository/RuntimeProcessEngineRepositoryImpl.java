package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.application.commands.StartProcessInstanceCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.security.SecurityUtil;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {


  private final ProcessManagerAdapter processManagerAdapter;
  private final SecurityUtil securityUtil;
  private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeProcessEngineRepositoryImpl.class);


  public RuntimeProcessEngineRepositoryImpl(ProcessManagerAdapter processManagerAdapter, SecurityUtil securityUtil) {
    this.processManagerAdapter = processManagerAdapter;
    this.securityUtil = securityUtil;
  }

  @Override
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables) {

    return null;
  }

  @Override
  public ProcessInstance startProcessInstanceByKey(String processKey, String businessKey, Map<String, Object> variables) {

    return null;
  }

  @Override
  public String startProcessInstanceByKeyT(String processKey, String businessKey, Map<String, Object> variables) {

    // Set authentication context
    securityUtil.logInAs("joao");

    LOGGER.info("Authenticated user: {}", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      return processManagerAdapter.startProcess(processKey, businessKey, variables);
    } catch (Exception e) {
      LOGGER.error("Erro ao iniciar processo", e);
      throw new RuntimeException("Falha ao iniciar processo", e);
    }
  }//d504ae77-6e2e-11f0-8cc3-00090faa0001

}

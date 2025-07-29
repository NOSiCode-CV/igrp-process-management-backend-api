package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class RuntimeProcessEngineRepositoryImpl implements RuntimeProcessEngineRepository {

  @Override
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables) {

    return null;
  }

  @Override
  public ProcessInstance startProcessInstanceByKey(String processKey, String businessKey, Map<String, Object> variables) {

    return null;
  }

}

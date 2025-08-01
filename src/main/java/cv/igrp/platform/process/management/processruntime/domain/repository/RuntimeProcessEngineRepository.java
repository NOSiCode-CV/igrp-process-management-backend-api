package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;

import java.util.Map;

public interface RuntimeProcessEngineRepository {

  ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables);
  ProcessInstance startProcessInstanceByKey(String processKey, String businessKey, Map<String, Object> variables);

  String startProcessInstanceByKeyT(String processKey, String businessKey, Map<String, Object> variables);


}

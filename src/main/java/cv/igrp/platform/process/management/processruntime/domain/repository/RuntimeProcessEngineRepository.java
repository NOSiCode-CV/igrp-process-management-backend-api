package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;

import java.util.List;
import java.util.Map;

public interface RuntimeProcessEngineRepository {

  ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables);

  ProcessInstance getProcessInstanceById(String processInstanceId);

  List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId);

  List<TaskInstance> getActiveTaskInstances(String processInstanceId);

  void completeTask(String taskInstanceId, Map<String, Object> variables);

  void claimTask(String taskInstanceId, String userId);

  void unClaimTask(String taskInstanceId);

  void assignTask(String taskId, String userId, String reason);

}

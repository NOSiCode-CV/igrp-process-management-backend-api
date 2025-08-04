package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;

import java.util.List;
import java.util.Map;

public interface RuntimeProcessEngineRepository {

  ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables);

  // Process Instance Details (with diagram with current stage/task)
  ProcessInstance getProcessInstanceById(String processInstanceId);

  List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(String processInstanceId);

  // list of available Tasks
  List<TaskInstance> getActiveTaskInstances(String processInstanceId);

  List<TaskInstance> getTaskInstances(String processInstanceId);

  // Execute/Complete Task
  void completeTask(String taskInstanceId, Map<String, Object> variables);

  // Task Instance Details + variable/forms
  TaskInstance getTaskInstance(String taskInstanceId);

}

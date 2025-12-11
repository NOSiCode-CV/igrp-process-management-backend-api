package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessSequenceService;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessStatistics;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProcessInstanceService {

  private final ProcessInstanceRepository processInstanceRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessSequenceService processSequenceService;
  private final TaskInstanceService taskInstanceService;
  private final ProcessDeploymentService processDeploymentService;

  public ProcessInstanceService(ProcessInstanceRepository processInstanceRepository,
                                RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                                ProcessSequenceService processSequenceService,
                                TaskInstanceService taskInstanceService,
                                ProcessDeploymentService processDeploymentService) {
    this.processInstanceRepository = processInstanceRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processSequenceService = processSequenceService;
    this.taskInstanceService = taskInstanceService;
    this.processDeploymentService = processDeploymentService;
  }

  public PageableLista<ProcessInstance> getAllProcessInstances(ProcessInstanceFilter filter) {

    if (!filter.getVariablesExpressions().isEmpty()) {
      // Call engine to filter by variables
      List<ProcessInstance> engineProcessInstances = runtimeProcessEngineRepository.getAllProcessInstancesByVariables(
          filter.getVariablesExpressions()
      );
      if (!engineProcessInstances.isEmpty()) {
        engineProcessInstances.forEach(processInstance -> {
          filter.includeProcessNumber(processInstance.getEngineProcessNumber().getValue());
        });
      } else {
        filter.includeProcessNumber(null);
      }
    }

    PageableLista<ProcessInstance> pageableLista = processInstanceRepository.findAll(filter);
    pageableLista.getContent().forEach(processInstance -> {
      setProcessInstanceProgress(processInstance);
      addProcessVariables(processInstance);
    });
    return pageableLista;
  }

  public ProcessInstance getProcessInstanceById(UUID id) {
    ProcessInstance processInstance = processInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with id: " + id));
    setProcessInstanceProgress(processInstance);
    addProcessVariables(processInstance);
    return processInstance;
  }

  public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
    ProcessInstance processInstance = processInstanceRepository.findByBusinessKey(businessKey)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with businessKey: " + businessKey));
    setProcessInstanceProgress(processInstance);
    addProcessVariables(processInstance);
    return processInstance;
  }

  void setProcessInstanceProgress(ProcessInstance processInstance) {
    List<ProcessInstanceTaskStatus> taskStatus = runtimeProcessEngineRepository.getProcessInstanceTaskStatus(processInstance.getEngineProcessNumber().getValue());
    int totalTasks = taskStatus.size();
    long completedTasks = taskStatus.stream()
        .filter(processInstanceTaskStatus -> processInstanceTaskStatus.getStatus() == TaskInstanceStatus.COMPLETED)
        .count();
    processInstance.setProgress(totalTasks, (int) completedTasks);
  }

  void addProcessVariables(ProcessInstance processInstance) {
    var processVariables = runtimeProcessEngineRepository.getProcessVariables(processInstance.getEngineProcessNumber().getValue());
    processInstance.addVariables(processVariables);
  }

  public ProcessInstance startProcessInstanceById(UUID id, Map<String, Object> variables, String user) {
    ProcessInstance processInstance = getProcessInstanceById(id);
    processInstance.addVariables(variables);
    return startProcessInstance(processInstance, user);
  }

  private ProcessInstance startProcessInstance(ProcessInstance processInstance, String user) {

    processInstance.start(user);

    var process = runtimeProcessEngineRepository.startProcessInstanceById(
        processInstance.getEngineProcessNumber().getValue(),
        processInstance.getProcReleaseId().getValue(),
        processInstance.getBusinessKey().getValue(),
        processInstance.getVariables()
    );

    taskInstanceService.createTaskInstancesByProcess(processInstance);

    updateProcessInstanceStatus(process, processInstance);

    return processInstance;
  }

  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(UUID id) {
    ProcessInstance processInstance = getProcessInstanceById(id);
    return runtimeProcessEngineRepository.getProcessInstanceTaskStatus(processInstance.getEngineProcessNumber().getValue());
  }

  public ProcessStatistics getProcessInstanceStatistics() {
    return processInstanceRepository.getProcessInstanceStatistics();
  }

  public void correlateMessage(String businessKey, String messageName, Map<String, Object> variables) {

    ProcessInstance processInstance = processInstanceRepository.findByBusinessKey(businessKey)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with businessKey: " + businessKey));

    runtimeProcessEngineRepository.correlateMessage(
        messageName,
        businessKey,
        variables
    );

    taskInstanceService.createTaskInstancesByProcess(processInstance);

    ProcessInstance process = runtimeProcessEngineRepository.getProcessInstanceById(
        processInstance.getEngineProcessNumber().getValue());

    updateProcessInstanceStatus(process, processInstance);

  }

  public void signal(String businessKey, String taskId, Map<String, Object> variables) {

    ProcessInstance processInstance = processInstanceRepository.findByBusinessKey(businessKey)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with businessKey: " + businessKey));

    runtimeProcessEngineRepository.signal(
        processInstance.getEngineProcessNumber().getValue(),
        taskId,
        variables
    );

    taskInstanceService.createTaskInstancesByProcess(processInstance);

    ProcessInstance process = runtimeProcessEngineRepository.getProcessInstanceById(
        processInstance.getEngineProcessNumber().getValue());

    updateProcessInstanceStatus(process, processInstance);

  }

  private void updateProcessInstanceStatus(ProcessInstance engineProcess, ProcessInstance processInstance) {
    if (engineProcess.getStatus() == ProcessInstanceStatus.COMPLETED) {
      processInstance.complete(
          engineProcess.getEndedAt(),
          engineProcess.getEndedBy() != null ? engineProcess.getEndedBy() : "demo@nosi.cv"
      );
    } else if (engineProcess.getStatus() == ProcessInstanceStatus.SUSPENDED) {
      processInstance.suspend();
    }
    // Persist
    processInstanceRepository.save(processInstance);
  }

  public ProcessInstance createProcessInstance(ProcessInstance processInstance, String user) {
    var latestProcessDefinitionId = processInstance.getProcReleaseId() == null
        ? processDeploymentService.findLatesProcessDefinitionIdByKey(processInstance.getProcReleaseKey().getValue())
        : processInstance.getProcReleaseId().getValue();

    var engineProcessInstance = runtimeProcessEngineRepository.createProcessInstanceById(
        latestProcessDefinitionId,
        processInstance.getBusinessKey().getValue()
    );

    var number = processSequenceService.getGeneratedProcessNumber(processInstance.getProcReleaseKey());

    processInstance.create(
        number,
        engineProcessInstance,
        user
    );

    return processInstanceRepository.save(processInstance);
  }

  public ProcessInstance createAndStartProcessInstance(ProcessInstance processInstance, String user) {
    ProcessInstance createdProcessInstance = createProcessInstance(processInstance, user);
    // Copy variables from processInstance to createdProcessInstance
    createdProcessInstance.addVariables(processInstance.getVariables());
    return startProcessInstance(createdProcessInstance, user);
  }

  public void rescheduleTimerByProcessInstanceId(UUID id, Long seconds) {
    ProcessInstance processInstance = getProcessInstanceById(id);
    runtimeProcessEngineRepository.rescheduleTimer(
        processInstance.getEngineProcessNumber().getValue(),
        seconds
    );
  }

}

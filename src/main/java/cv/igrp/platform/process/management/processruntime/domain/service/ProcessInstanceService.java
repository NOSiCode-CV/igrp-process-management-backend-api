package cv.igrp.platform.process.management.processruntime.domain.service;

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
import java.util.UUID;

@Service
public class ProcessInstanceService {

  private final ProcessInstanceRepository processInstanceRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessSequenceService processSequenceService;
  private final TaskInstanceService taskInstanceService;

  public ProcessInstanceService(ProcessInstanceRepository processInstanceRepository,
                                RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                                ProcessSequenceService processSequenceService,
                                TaskInstanceService taskInstanceService) {
    this.processInstanceRepository = processInstanceRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processSequenceService = processSequenceService;
    this.taskInstanceService = taskInstanceService;
  }

  public PageableLista<ProcessInstance> getAllProcessInstances(ProcessInstanceFilter filter) {
    PageableLista<ProcessInstance> pageableLista = processInstanceRepository.findAll(filter);
    pageableLista.getContent().forEach( processInstance ->{
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

  void setProcessInstanceProgress(ProcessInstance processInstance){
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

  public ProcessInstance startProcessInstance(ProcessInstance processInstance, String user) {

    var process = runtimeProcessEngineRepository.startProcessInstanceById(
        processInstance.getProcReleaseId().getValue(),
        processInstance.getBusinessKey().getValue(),
        processInstance.getVariables()
    );

    var number = processSequenceService.getGeneratedProcessNumber(process.getProcReleaseKey());

    processInstance.start(number, process.getEngineProcessNumber(), process.getVersion(), process.getName(), user);

    ProcessInstance runningProcessInstance = processInstanceRepository.save(processInstance);

    taskInstanceService.createTaskInstancesByProcess(runningProcessInstance);

    if(process.getStatus() == ProcessInstanceStatus.COMPLETED){
      runningProcessInstance.complete(
          process.getEndedAt(),
          process.getEndedBy() != null ? process.getEndedBy() : user
      );
      return processInstanceRepository.save(runningProcessInstance);
    }

    return runningProcessInstance;
  }

  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(UUID id) {
    ProcessInstance processInstance = getProcessInstanceById(id);
    return runtimeProcessEngineRepository.getProcessInstanceTaskStatus(processInstance.getEngineProcessNumber().getValue());
  }

  public ProcessStatistics getProcessInstanceStatistics() {
    return processInstanceRepository.getProcessInstanceStatistics();
  }
}

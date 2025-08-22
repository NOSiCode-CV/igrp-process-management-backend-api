package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessInstanceService {

  private final ProcessInstanceRepository processInstanceRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  private final TaskInstanceService taskInstanceService;

  public ProcessInstanceService(ProcessInstanceRepository processInstanceRepository,
                                RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                                TaskInstanceService taskInstanceService) {
    this.processInstanceRepository = processInstanceRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.taskInstanceService = taskInstanceService;
  }

  public PageableLista<ProcessInstance> getAllProcessInstances(ProcessInstanceFilter filter) {
    return processInstanceRepository.findAll(filter);
  }

  public ProcessInstance getProcessInstanceById(UUID id) {
    return processInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with id: " + id));
  }

  public ProcessInstance startProcessInstance(ProcessInstance processInstance) {

    final String user = "demo@nosi.cv";

    var process = runtimeProcessEngineRepository.startProcessInstanceById(
        processInstance.getProcReleaseId().getValue(),
        processInstance.getBusinessKey().getValue(),
        processInstance.getVariables()
    );

    processInstance.start(process.getNumber(), process.getVersion(), process.getName(), user);

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
    return runtimeProcessEngineRepository.getProcessInstanceTaskStatus(processInstance.getNumber().getValue());
  }

}

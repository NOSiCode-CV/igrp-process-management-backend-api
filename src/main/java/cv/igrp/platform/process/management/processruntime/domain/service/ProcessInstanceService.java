package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    Map<String, Object> variables = processInstance.getVariables();
    Code businessKey = processInstance.getBusinessKey();
    Code processDefinitionId = processInstance.getProcReleaseId();
    final String user = "demo@nosi.cv";

    if (variables == null) {
      variables = Collections.emptyMap();
    }

    var process = runtimeProcessEngineRepository.startProcessInstanceById(
        processDefinitionId.getValue(),
        businessKey != null ?  businessKey.getValue() : null,
        variables
    );

    if(processInstance.getStatus() == ProcessInstanceStatus.COMPLETED){
      processInstance.complete(
          processInstance.getEndedAt(),
          user
      );
    }else{
      processInstance.start(process.getNumber());
    }

    ProcessInstance persitedProcessInstance = processInstanceRepository.save(processInstance);

    taskInstanceService.createTaskInstancesByProcess(
        persitedProcessInstance.getId(),
        persitedProcessInstance.getNumber(),
        persitedProcessInstance.getApplicationBase()
    );

    return persitedProcessInstance;
  }

  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(UUID id) {
      return runtimeProcessEngineRepository.getProcessInstanceTaskStatus(id.toString());
  }

}

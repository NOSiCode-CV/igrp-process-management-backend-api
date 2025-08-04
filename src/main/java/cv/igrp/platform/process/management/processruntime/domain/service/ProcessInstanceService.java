package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProcessInstanceService {

  private final ProcessInstanceRepository processInstanceRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  public ProcessInstanceService(ProcessInstanceRepository processInstanceRepository,
                                RuntimeProcessEngineRepository runtimeProcessEngineRepository) {
    this.processInstanceRepository = processInstanceRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
  }

  public PageableLista<ProcessInstance> getAllProcessInstances(ProcessInstanceFilter filter) {
    return processInstanceRepository.findAll(filter);
  }

  public ProcessInstance getProcessInstanceById(UUID id) {
    return processInstanceRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with id: " + id));
  }

  public ProcessInstance startProcessInstance(ProcessInstance processInstance) {
    processInstance.start();

    var variables = processInstance.getVariables();
    var businessKey = processInstance.getBusinessKey();
    var processKey= processInstance.getProcReleaseKey();
    var processDefinitionId = processInstance.getProcReleaseId();

    if (variables == null) {
      variables = Collections.emptyMap();
    }

    var id = runtimeProcessEngineRepository.startProcessInstanceById(processDefinitionId.getValue(), businessKey.getValue(),variables);
    //return processInstance;
    return processInstanceRepository.save(processInstance);
  }

  public List<ProcessInstanceTaskStatus> getProcessInstanceTaskStatus(UUID id) {
    ProcessInstance processInstance = getProcessInstanceById(id);
    //return runtimeProcessEngineRepository.getTaskStatus(processInstance.getNumber().getValue());
    return null;
  }

}

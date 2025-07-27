package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

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
    // Call process engine
    // ...
    // Persist
    return processInstanceRepository.save(processInstance);
  }

}

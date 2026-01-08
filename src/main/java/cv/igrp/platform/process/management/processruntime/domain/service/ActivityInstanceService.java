package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityInstanceService {

  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceRepository processInstanceRepository;
  private final TaskInstanceRepository taskInstanceRepository;

  public ActivityInstanceService(RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                                 ProcessInstanceRepository processInstanceRepository,
                                 TaskInstanceRepository taskInstanceRepository) {
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceRepository = processInstanceRepository;
    this.taskInstanceRepository = taskInstanceRepository;
  }

  public ActivityData getById(String id) {
    var activity = runtimeProcessEngineRepository.getActivityById(id);
    Optional<TaskInstance> optionalTaskInstance = taskInstanceRepository.findByExternalId(id);
    if (optionalTaskInstance.isPresent()) {
      TaskInstance taskInstance = optionalTaskInstance.get();
      activity.addVariables(taskInstance.getVariables());
      activity.addVariables(taskInstance.getForms());
    }
    return activity;
  }


  public List<ActivityData> getActiveActivityInstances(UUID processInstanceId, IGRPActivityType type) {
    ProcessInstance processInstance = getProcessInstanceById(processInstanceId);
    return runtimeProcessEngineRepository.getActiveActivityInstances(
        processInstance.getEngineProcessNumber().getValue(),
        type
    );
  }

  public List<ProcessActivityInfo> getActivityProgress(UUID processInstanceId, IGRPActivityType type) {
    ProcessInstance processInstance = getProcessInstanceById(processInstanceId);
    return runtimeProcessEngineRepository.getActivityProgress(
        processInstance.getEngineProcessNumber().getValue(),
        type
    );
  }

  private ProcessInstance getProcessInstanceById(UUID processInstanceId) {
    return processInstanceRepository.findById(processInstanceId)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with id: " + processInstanceId));
  }

}

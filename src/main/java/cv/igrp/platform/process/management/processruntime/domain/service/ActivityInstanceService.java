package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessTimelineEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.VariableTag;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.stereotype.Service;

import java.util.*;

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

  public List<ActivityData> getActiveActivityInstances(String processIdentifier, IGRPActivityType type) {
    Optional<ProcessInstance> optionalProcessInstance = processInstanceRepository
        .findByBusinessKey(processIdentifier);
    if(optionalProcessInstance.isPresent()) {
      return getActiveActivityInstances(
          optionalProcessInstance.get(),
          type
      );
    }
    ProcessInstance processInstance = getProcessInstanceById(UUID.fromString(processIdentifier));
    return getActiveActivityInstances(processInstance, type);
  }

  public List<ActivityData> getActiveActivityInstances(ProcessInstance processInstance, IGRPActivityType type) {
    return runtimeProcessEngineRepository.getActiveActivityInstances(
        processInstance.getEngineProcessNumber().getValue(),
        type
    );
  }

  public List<ProcessTimelineEvent> getProcessTimelineEvents(String processIdentifier, IGRPActivityType type) {
    Optional<ProcessInstance> optionalProcessInstance = processInstanceRepository
        .findByBusinessKey(processIdentifier);
    if(optionalProcessInstance.isPresent()) {
      return getProcessTimelineEvents(
          optionalProcessInstance.get(),
          type
      );
    }
    ProcessInstance processInstance = getProcessInstanceById(UUID.fromString(processIdentifier));
    return getProcessTimelineEvents(processInstance, type);
  }

    public List<ProcessTimelineEvent> getProcessTimelineEvents(ProcessInstance processInstance, IGRPActivityType type) {
    List<ProcessTimelineEvent> timelineEvents = runtimeProcessEngineRepository.getProcessTimelineEvents(
        processInstance.getEngineProcessNumber().getValue(),
        type
    );
    // Enrich the timeline events with task variables
    timelineEvents.forEach(timelineEvent -> {
      if(timelineEvent.getTaskId() != null){
        Optional<TaskInstance> optTaskInstance =  taskInstanceRepository.findByExternalId(timelineEvent.getTaskId());
        if(optTaskInstance.isPresent()){
          TaskInstance taskInstance = optTaskInstance.get();
          Map<String, Object> variables = new HashMap<>();
          variables.put(VariableTag.VARIABLES.getCode(), taskInstance.getVariables());
          variables.put(VariableTag.FORMS.getCode(), taskInstance.getForms());
          timelineEvent.setVariables(variables);
        }
      }
    });
    return timelineEvents;
  }

  private ProcessInstance getProcessInstanceById(UUID processInstanceId) {
    return processInstanceRepository.findById(processInstanceId)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process instance found with id: " + processInstanceId));
  }

}

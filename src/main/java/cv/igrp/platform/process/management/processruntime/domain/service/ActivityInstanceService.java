package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessTimelineEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
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


  public List<ActivityData> getActiveActivityInstances(UUID processInstanceId, IGRPActivityType type) {
    ProcessInstance processInstance = getProcessInstanceById(processInstanceId);
    return runtimeProcessEngineRepository.getActiveActivityInstances(
        processInstance.getEngineProcessNumber().getValue(),
        type
    );
  }

  public List<ProcessTimelineEvent> getProcessTimelineEvents(UUID processInstanceId, IGRPActivityType type) {
    ProcessInstance processInstance = getProcessInstanceById(processInstanceId);
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
          Map<String, Object> variables = timelineEvent.getVariables();
          variables.putAll(taskInstance.getVariables());
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

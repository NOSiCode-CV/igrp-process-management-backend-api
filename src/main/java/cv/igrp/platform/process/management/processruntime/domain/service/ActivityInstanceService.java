package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.framework.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityInstanceService {

  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  public ActivityInstanceService(RuntimeProcessEngineRepository runtimeProcessEngineRepository) {
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
  }

  public ActivityData getById(String id) {
    var activity = runtimeProcessEngineRepository.getActivityById(id);
    addVariables(activity);
    return activity;
  }


  public List<ActivityData> getActiveActivityInstances(String processInstanceId, IGRPActivityType type) {
    return runtimeProcessEngineRepository.getActiveActivityInstances(processInstanceId, type);
  }

  public List<ProcessActivityInfo> getActivityProgress(String processInstanceId, IGRPActivityType type) {
    return runtimeProcessEngineRepository.getActivityProgress(processInstanceId, type);
  }

  private void addVariables(ActivityData activityData) {
    activityData.addVariables(runtimeProcessEngineRepository.getActivityVariables(activityData.getId().getValue()));
  }

}

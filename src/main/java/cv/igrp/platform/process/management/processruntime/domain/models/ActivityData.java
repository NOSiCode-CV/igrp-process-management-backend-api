package cv.igrp.platform.process.management.processruntime.domain.models;


import cv.igrp.framework.process.runtime.core.engine.activity.model.ActivityInfo;
import cv.igrp.framework.process.runtime.core.engine.activity.model.IGRPActivityStatus;
import cv.igrp.framework.process.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class ActivityData {

  Code id;
  Name name;
  String description;
  Code processInstanceId;
  Code parentId;
  Code parentProcessInstanceId;
  IGRPActivityStatus status;
  IGRPActivityType type;
  Map<String,Object> variables;

  @Builder
  public ActivityData(
      Code id,
      Name name,
      String description,
      Code processInstanceId,
      Code parentId,
      Code parentProcessInstanceId,
      IGRPActivityStatus status,
      IGRPActivityType type,
      Map<String, Object> variables
  ) {
    this.id = id;
    this.name = Objects.requireNonNull(name, "Name cannot be null");
    this.description = description;
    this.processInstanceId = Objects.requireNonNull(processInstanceId,  "Process instance id cannot be null");
    this.parentId = Objects.requireNonNull(parentId,   "Parent process instance id cannot be null");
    this.parentProcessInstanceId = Objects.requireNonNull(parentProcessInstanceId,   "Parent process instance id cannot be null");
    this.status = Objects.requireNonNull(status, "Status cannot be null");
    this.type = Objects.requireNonNull(type, "Type cannot be null");
    this.variables = Objects.nonNull(variables)? variables: new HashMap<>();
  }

  public ActivityData withProperties(ActivityInfo info) {
    return ActivityData.builder()
        .id(Code.create(info.id()))
        .name(Name.create(info.name()))
        .description(info.description())
        .processInstanceId(Code.create(info.processInstanceId()))
        .parentId(Code.create(info.parentId()))
        .parentProcessInstanceId(Code.create(info.parentProcessInstanceId()))
        .status(info.status())
        .type(info.type())
        .build();
  }

  public void addVariables(Map<String,Object> variables) {
    this.variables.putAll(variables);
  }

  public void addVariable(String key, Object value) {
    this.variables.put(key, value);
  }

}

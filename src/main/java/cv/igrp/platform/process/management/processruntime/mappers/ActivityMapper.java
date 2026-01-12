package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.framework.runtime.core.engine.activity.model.ProcessTimelineEvent;
import cv.igrp.platform.process.management.processruntime.application.dto.ActivityDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ActivityProgressDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ActivityData;
import cv.igrp.platform.process.management.shared.application.constants.VariableTag;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActivityMapper {

  public ActivityDTO toDto(ActivityData activityData) {

    var activityDto = new ActivityDTO();

    activityDto.setId(activityData.getId().getValue());
    activityDto.setName(activityData.getName().getValue());
    activityDto.setDescription(activityData.getDescription());
    activityDto.setStatus(activityData.getStatus().name());
    activityDto.setType(activityData.getType().name());
    activityDto.setParentId(activityData.getParentId().getValue());
    activityDto.setParentProcessInstanceId(activityData.getParentProcessInstanceId().getValue());
    activityDto.setProcessInstanceId(activityData.getProcessInstanceId().getValue());
    activityDto.setVariables(toActivityVariableDTO(activityData.getVariables(), VariableTag.VARIABLES));

    return activityDto;

  }

  public List<TaskVariableDTO> toActivityVariableDTO(Map<String,Object> variables, VariableTag tag){
    Object group = variables.get(tag.getCode());

    if (!(group instanceof Map<?, ?> map))
      return List.of();

    return map.entrySet().stream()
        .map(e -> new TaskVariableDTO(e.getKey().toString(), e.getValue()))
        .toList();
  }

  public ActivityProgressDTO toProgressDto(ProcessTimelineEvent timelineEvent) {

    var activityProgressDTO = new ActivityProgressDTO();

    activityProgressDTO.setActivityId(timelineEvent.getActivityId());
    activityProgressDTO.setActivityName(timelineEvent.getActivityName());
    activityProgressDTO.setType(timelineEvent.getType().name());
    activityProgressDTO.setStatus(timelineEvent.getStatus().name());
    activityProgressDTO.setProcessInstanceId(timelineEvent.getProcessInstanceId());
    activityProgressDTO.setAssignee(timelineEvent.getAssignee());
    activityProgressDTO.setDurationMillis(timelineEvent.getDuration());
    activityProgressDTO.setAssignee(timelineEvent.getAssignee());
    activityProgressDTO.setExecutionId(timelineEvent.getExecutionId());
    activityProgressDTO.setTaskId(timelineEvent.getTaskId());
    activityProgressDTO.setActivityInstanceId(timelineEvent.getActivityInstanceId());
    activityProgressDTO.setTreeNumber(timelineEvent.getTreeNumber());

    ZoneId cvZone = ZoneId.of("Atlantic/Cape_Verde");
    activityProgressDTO.setStartTime(
        timelineEvent.getStartTime() != null ? LocalDateTime.ofInstant(timelineEvent.getStartTime(), cvZone) : null
    );
    activityProgressDTO.setEndTime(
        timelineEvent.getEndTime() != null ? LocalDateTime.ofInstant(timelineEvent.getEndTime(), cvZone) : null
    );

    activityProgressDTO.setVariables(toActivityVariableDTO(timelineEvent.getVariables(), VariableTag.VARIABLES));
    activityProgressDTO.setForms(toActivityVariableDTO(timelineEvent.getVariables(), VariableTag.FORMS));

    return activityProgressDTO;

  }

  public List<ActivityDTO> toInstancesDto(List<ActivityData> instances) {
    return instances.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<ActivityProgressDTO> toProgressesDto(List<ProcessTimelineEvent> progress) {
    return progress.stream().map(this::toProgressDto).collect(Collectors.toList());
  }

}

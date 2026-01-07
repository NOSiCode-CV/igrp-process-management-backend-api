package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.framework.runtime.core.engine.activity.model.ProcessActivityInfo;
import cv.igrp.platform.process.management.processruntime.application.dto.ActivityDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ActivityProgressDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ActivityData;
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
    activityDto.setVariables(toActivityVariableDTO(activityData.getVariables()));

    return activityDto;

  }

  public List<TaskVariableDTO> toActivityVariableDTO(Map<String,Object> variables){
    return variables==null ? List.of() : variables.entrySet().stream()
        .map(e-> new TaskVariableDTO(e.getKey(),e.getValue()))
        .toList();
  }

  public ActivityProgressDTO toProgressDto(ProcessActivityInfo activityInfo) {

    var activityProgressDTO = new ActivityProgressDTO();

    activityProgressDTO.setActivityId(activityInfo.getActivityId());
    activityProgressDTO.setActivityKey(activityInfo.getActivityKey());
    activityProgressDTO.setActivityName(activityInfo.getActivityName());
    activityProgressDTO.setType(activityInfo.getType().name());
    activityProgressDTO.setStatus(activityInfo.getStatus().name());
    activityProgressDTO.setProcessInstanceId(activityInfo.getProcessInstanceId());
    activityProgressDTO.setAssignee(activityInfo.getAssignee());
    activityProgressDTO.setCandidateGroups(activityInfo.getCandidateGroups());
    activityProgressDTO.setCandidateUsers(activityInfo.getCandidateUsers());

    activityProgressDTO.setDurationMillis(activityInfo.getDurationMillis());
    ZoneId cvZone = ZoneId.of("Atlantic/Cape_Verde");
    activityProgressDTO.setStartTime(
        activityInfo.getStartTime() != null ? LocalDateTime.ofInstant(activityInfo.getStartTime(), cvZone) : null
    );
    activityProgressDTO.setEndTime(
        activityInfo.getEndTime() != null ? LocalDateTime.ofInstant(activityInfo.getEndTime(), cvZone) : null
    );

    activityProgressDTO.setVariables(toActivityVariableDTO(activityInfo.getVariables()));

    return activityProgressDTO;

  }

  public List<ActivityDTO> toInstancesDto(List<ActivityData> instances) {
    return instances.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<ActivityProgressDTO> toProgressesDto(List<ProcessActivityInfo> progresses) {
    return progresses.stream().map(this::toProgressDto).collect(Collectors.toList());
  }

}

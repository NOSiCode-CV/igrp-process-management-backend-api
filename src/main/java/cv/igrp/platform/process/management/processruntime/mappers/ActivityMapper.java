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

    activityProgressDTO.setActivityKey(activityInfo.activityKey());
    activityProgressDTO.setActivityName(activityInfo.activityName());
    activityProgressDTO.setType(activityInfo.type().name());
    activityProgressDTO.setStatus(activityInfo.status().name());
    activityProgressDTO.setProcessInstanceId(activityInfo.processInstanceId());
    activityProgressDTO.setAssignee(activityInfo.assignee());
    activityProgressDTO.setCandidateGroups(activityInfo.candidateGroups());
    activityProgressDTO.setCandidateUsers(activityInfo.candidateUsers());

    activityProgressDTO.setDurationMillis(activityInfo.durationMillis());
    ZoneId cvZone = ZoneId.of("Atlantic/Cape_Verde");
    activityProgressDTO.setStartTime(
        activityInfo.startTime() != null ? LocalDateTime.ofInstant(activityInfo.startTime(), cvZone) : null
    );
    activityProgressDTO.setEndTime(
        activityInfo.endTime() != null ? LocalDateTime.ofInstant(activityInfo.endTime(), cvZone) : null
    );

    return activityProgressDTO;

  }

  public List<ActivityDTO> toInstancesDto(List<ActivityData> instances) {
    return instances.stream().map(this::toDto).collect(Collectors.toList());
  }

  public List<ActivityProgressDTO> toProgressesDto(List<ProcessActivityInfo> progresses) {
    return progresses.stream().map(this::toProgressDto).collect(Collectors.toList());
  }

}

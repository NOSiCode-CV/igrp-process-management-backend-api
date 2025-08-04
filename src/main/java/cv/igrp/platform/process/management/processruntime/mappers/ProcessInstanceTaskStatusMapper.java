package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessInstanceTaskStatusMapper {

  public List<ProcessInstanceTaskStatusDTO> toDTO(List<ProcessInstanceTaskStatus> processInstanceTaskStatusList) {
    List<ProcessInstanceTaskStatusDTO> processInstanceTaskStatusDTOList = new ArrayList<>();
    for (ProcessInstanceTaskStatus processInstanceTaskStatus : processInstanceTaskStatusList) {
      ProcessInstanceTaskStatusDTO processInstanceTaskStatusDTO = new ProcessInstanceTaskStatusDTO();
      processInstanceTaskStatusDTO.setTaskKey(processInstanceTaskStatus.getTaskKey().getValue());
      processInstanceTaskStatusDTO.setStatus(processInstanceTaskStatus.getStatus());
      processInstanceTaskStatusDTO.setName(processInstanceTaskStatus.getTaskName().getValue());
    }
    return processInstanceTaskStatusDTOList;
  }

}

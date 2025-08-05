package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
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

  public ProcessInstanceTaskStatus toModel(TaskInfo taskInfo){
    return ProcessInstanceTaskStatus.builder()
        .taskKey(Code.create(taskInfo.getTaskDefinitionKey() != null ? taskInfo.getTaskDefinitionKey() : "UNKNOWN"))
        .taskName(Name.create(taskInfo.getName() != null ? taskInfo.getName() : "UNKNOWN"))
        // .status(...) // Não disponível no TaskInfo
        .status(TaskInstanceStatus.CREATED) // status by default
        .processInstanceId(Code.create(taskInfo.getProcessInstanceId() != null ? taskInfo.getProcessInstanceId() : "UNKNOWN"))
        .build();
  }

}

package cv.igrp.platform.process.management.processruntime.mappers;


import cv.igrp.framework.process.runtime.core.engine.task.model.IGRPTaskStatus;
import cv.igrp.framework.process.runtime.core.engine.task.model.ProcessTaskInfo;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
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
      processInstanceTaskStatusDTOList.add(processInstanceTaskStatusDTO);
    }
    return processInstanceTaskStatusDTOList;
  }

  public ProcessInstanceTaskStatus toModel(ProcessTaskInfo processTaskInfo){

    return ProcessInstanceTaskStatus.builder()
        .taskKey(processTaskInfo.taskKey() != null ? Code.create(processTaskInfo.taskKey()) : null)
        .taskName(Name.create( processTaskInfo.taskName() != null ? processTaskInfo.taskName() : "NOT SET"))
        .status(mapStatus(processTaskInfo.status()))
        .processInstanceId(processTaskInfo.processInstanceId() != null ? Code.create(processTaskInfo.processInstanceId()) : null)
        .build();
  }

  private TaskInstanceStatus mapStatus(IGRPTaskStatus igrpTaskStatus) {
    if (igrpTaskStatus == null) return TaskInstanceStatus.CREATED;
    try {
      return TaskInstanceStatus.valueOf(igrpTaskStatus.name());
    } catch (IllegalArgumentException ex) {
      return TaskInstanceStatus.CREATED;
    }
  }

}

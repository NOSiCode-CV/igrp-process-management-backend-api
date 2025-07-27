package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.StartProcessRequestDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.stereotype.Component;

@Component
public class ProcessInstanceMapper {

  public ProcessInstanceEntity toEntity(ProcessInstance processInstance) {
    ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();

    return processInstanceEntity;
  }

  public ProcessInstance toModel(ProcessInstanceEntity processInstanceEntity) {
    return ProcessInstance.builder()
        .build();
  }

  public ProcessInstance toModel(StartProcessRequestDTO startProcessRequestDTO) {
    return ProcessInstance.builder()
        .build();
  }

  public ProcessInstanceDTO toDTO(ProcessInstance processInstance) {
    ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();

    return processInstanceDTO;
  }

}

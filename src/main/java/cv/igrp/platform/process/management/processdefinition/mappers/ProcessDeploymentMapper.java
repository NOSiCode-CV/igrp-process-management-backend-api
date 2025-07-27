package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.springframework.stereotype.Component;

@Component
public class ProcessDeploymentMapper {

  public ProcessDeployment toModel(ProcessDeploymentRequestDTO dto){
    return ProcessDeployment.builder()
        .bpmnXml(BpmnXml.create(dto.getBpmnXml()))
        .name(Name.create(dto.getName()))
        .description(dto.getDescription())
        .resourceName(Name.create(dto.getResourceName()))
        .key(Code.create(dto.getKey()))
        .build();
  }

  public ProcessDeploymentDTO toDTO(ProcessDeployment model){
    ProcessDeploymentDTO dto = new ProcessDeploymentDTO();
    dto.setId(model.getId().getValue());
    dto.setDeploymentId(model.getDeploymentId());
    dto.setBpmnXml(model.getBpmnXml().toString());
    dto.setName(model.getName().getValue());
    dto.setDeployed(model.isDeployed());
    dto.setResourceName(model.getResourceName().getValue());
    dto.setKey(model.getKey().getValue());
    dto.setDescription(model.getDescription());
    dto.setBpmnUrl(model.getBpmnUrl());
    dto.setDeployedAt(model.getDeployedAt());
    dto.setBpmnSourceType(model.getBpmnSourceType());
    return dto;
  }

}

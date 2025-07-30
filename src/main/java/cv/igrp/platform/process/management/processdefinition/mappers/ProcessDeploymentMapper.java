package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.nosi.igrp.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProcessDeploymentMapper {

  public ProcessDeployment toModel(ProcessDeploymentRequestDTO dto){
    return ProcessDeployment.builder()
        .bpmnXml(BpmnXml.create(dto.getBpmnXml()))
        .name(Name.create(dto.getName()))
        .description(dto.getDescription())
        .resourceName(Name.create(dto.getResourceName()))
        .key(Code.create(dto.getKey()))
        .applicationBase(Code.create(dto.getApplicationBase()))
        .build();
  }

  public ProcessDeploymentDTO toDTO(ProcessDeployment model){
    ProcessDeploymentDTO dto = new ProcessDeploymentDTO();
    dto.setId(model.getId());
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
    dto.setApplicationBase(model.getApplicationBase().getValue());
    return dto;
  }

  public ProcessDeploymentListDTO toListDTO(ProcessDeployment model){
    ProcessDeploymentListDTO dto = new ProcessDeploymentListDTO();
    dto.setId(model.getId());
    dto.setName(model.getName().getValue());
    dto.setDescription("dadasdadasdsad");
    dto.setProcessKey(model.getKey().getValue());
    dto.setVersion(model.getVersion());
    dto.setApplicationBase(model.getApplicationBase().getValue());
    dto.setDeploymentId(model.getDeploymentId());
    return dto;
  }

  public ProcessDeploymentListPageDTO toDTO(PageableLista<ProcessDeployment> deployments) {
    ProcessDeploymentListPageDTO dto = new ProcessDeploymentListPageDTO();
    dto.setTotalElements(deployments.getTotalElements());
    dto.setTotalPages(deployments.getTotalPages());
    dto.setPageNumber(deployments.getPageNumber());
    dto.setPageSize(deployments.getPageSize());
    dto.setFirst(deployments.isFirst());
    dto.setLast(deployments.isLast());
    List<ProcessDeploymentListDTO> content = deployments.getContent()
        .stream()
        .map(this::toListDTO)
        .toList();
    dto.setContent(content);
    return dto;
  }

  public ProcessDeployment toModel(IgrpProcessDefinitionRepresentation definition) {
    return ProcessDeployment.builder()
        .key(Code.create(definition.getKey()))
        .name(Name.create(definition.getName()))
        .resourceName(Name.create(definition.getResourceName()))
        .bpmnXml(BpmnXml.create(definition.getBpmnXml()))
        .version(definition.getVersion())
        .deploymentId(definition.getDeploymentId())
        .deployed(definition.isDeployed())
        .deployedAt(definition.getDeployedAt())
        .applicationBase(Code.create(definition.getApplicationBase()))
        .build();
  }


}

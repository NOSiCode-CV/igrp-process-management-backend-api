package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessArtifactMapper {

  public ProcessArtifact toModel(ProcessArtifactRequestDTO dto, String processDefinitionId) {
    return ProcessArtifact.builder()
        .name(dto.getName())
        .processDefinitionId(Code.create(processDefinitionId))
        .key(Code.create(dto.getKey()))
        .formKey(Code.create(dto.getFormKey()))
        .build();
  }

  public ProcessArtifactDTO toDTO(ProcessArtifact model) {
    ProcessArtifactDTO dto = new ProcessArtifactDTO();
    dto.setId(model.getId().getValue());
    dto.setName(model.getName());
    dto.setProcessDefinitionId(model.getProcessDefinitionId().getValue());
    dto.setKey(model.getKey().getValue());
    dto.setFormKey(model.getFormKey().getValue());
    return dto;
  }

  public ProcessArtifactEntity toEntity(ProcessArtifact model) {
    ProcessArtifactEntity entity = new ProcessArtifactEntity();
    entity.setId(model.getId().getValue());
    entity.setName(model.getName());
    entity.setProcessDefinitionId(model.getProcessDefinitionId().getValue());
    entity.setKey(model.getKey().getValue());
    entity.setFormKey(model.getFormKey().getValue());
    return entity;
  }

  public ProcessArtifact toModel(ProcessArtifactEntity entity) {
    return ProcessArtifact.builder()
        .id(Identifier.create(entity.getId()))
        .name(entity.getName())
        .processDefinitionId(Code.create(entity.getProcessDefinitionId()))
        .key(Code.create(entity.getKey()))
        .formKey(Code.create(entity.getFormKey()))
        .build();
  }

  public List<ProcessArtifactDTO> toDTO(List<ProcessArtifact> processArtifacts) {
    return processArtifacts.stream().map(this::toDTO).toList();
  }

  public List<ProcessArtifact> toModel(List<ProcessArtifactEntity> entities) {
    return entities.stream().map(this::toModel).toList();
  }

}

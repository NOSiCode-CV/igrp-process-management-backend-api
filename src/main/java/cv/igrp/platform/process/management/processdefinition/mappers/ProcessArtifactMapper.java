package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessArtifactMapper {

  public ProcessArtifact toModel(ProcessArtifactRequestDTO dto, String processDefinitionId) {
    return ProcessArtifact.builder()
        .name(Name.create(dto.getName()))
        .processDefinitionId(Code.create(processDefinitionId))
        .key(Code.create(dto.getKey()))
        .formKey(Code.create(dto.getFormKey()))
        .candidateGroups(dto.getCandidateGroups())
        .build();
  }

  public ProcessArtifactDTO toDTO(ProcessArtifact model) {
    ProcessArtifactDTO dto = new ProcessArtifactDTO();
    dto.setId(model.getId().getValue());
    dto.setName(model.getName().getValue());
    dto.setProcessDefinitionId(model.getProcessDefinitionId().getValue());
    dto.setKey(model.getKey().getValue());
    dto.setFormKey(model.getFormKey().getValue());
    dto.setCandidateGroups(model.getCandidateGroups());
    return dto;
  }

  public ProcessArtifactEntity toEntity(ProcessArtifact model) {
    ProcessArtifactEntity entity = new ProcessArtifactEntity();
    entity.setName(model.getName().getValue());
    entity.setProcessDefinitionId(model.getProcessDefinitionId().getValue());
    entity.setKey(model.getKey().getValue());
    entity.setFormKey(model.getFormKey().getValue());
    entity.setId(model.getId().getValue());
    if(!model.getCandidateGroups().isEmpty()) {
      entity.setCandidateGroups(String.join(",", model.getCandidateGroups()));
    }
    return entity;
  }

  public ProcessArtifact toModel(ProcessArtifactEntity entity) {
    return ProcessArtifact.builder()
        .id(Identifier.create(entity.getId()))
        .name(Name.create(entity.getName()))
        .processDefinitionId(Code.create(entity.getProcessDefinitionId()))
        .key(Code.create(entity.getKey()))
        .formKey(Code.create(entity.getFormKey()))
        .candidateGroups(entity.getCandidateGroups() != null
            ? new ArrayList<>(List.of(entity.getCandidateGroups().split(",")))
            : new ArrayList<>())
        .build();
  }

  public List<ProcessArtifactDTO> toDTO(List<ProcessArtifact> processArtifacts) {
    return processArtifacts.stream().map(this::toDTO).toList();
  }

  public List<ProcessArtifact> toModel(List<ProcessArtifactEntity> entities) {
    return entities.stream().map(this::toModel).toList();
  }

}

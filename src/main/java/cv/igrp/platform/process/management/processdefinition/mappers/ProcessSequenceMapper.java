package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaProcessEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceSequenceEntity;
import org.springframework.stereotype.Component;

@Component
public class ProcessSequenceMapper {


  public ProcessSequence toModel(ProcessSequenceDTO dto) {
    return  ProcessSequence.builder()
        .id(Identifier.create(dto.getId()))
        .name(Name.create(dto.getName()))
        .prefix(Code.create(dto.getPrefix()))
        .checkDigitSize(dto.getCheckDigitSize())
        .padding(dto.getPadding())
        .dateFormat(dto.getDateFormat())
        .nextNumber(dto.getNextNumber())
        .numberIncrement(dto.getNumberIncrement())
        .processDefinitionId(Identifier.create(dto.getProcessDefinitionId()))
        .build();
  }


  public ProcessInstanceSequenceEntity toEntity(ProcessSequence model) {
    var entity = new ProcessInstanceSequenceEntity();
    entity.setId(model.getId().getValue());
    entity.setName(model.getName().getValue());
    entity.setPrefix(model.getPrefix().getValue());
    entity.setCheckDigitSize(model.getCheckDigitSize());
    entity.setPadding(model.getPadding());
    entity.setDateFormat(model.getDateFormat());
    entity.setNextNumber(model.getNextNumber());
    entity.setNumberIncrement(model.getNumberIncrement());
    var processDefinition = new AreaProcessEntity();
    processDefinition.setId(model.getProcessDefinitionId().getValue());
    entity.setProcessDefinitionId(processDefinition);
    return  entity;
  }


  public ProcessSequence toModel(ProcessInstanceSequenceEntity entity) {
    return  ProcessSequence.builder()
        .id(Identifier.create(entity.getId()))
        .name(Name.create(entity.getName()))
        .prefix(Code.create(entity.getPrefix()))
        .checkDigitSize(entity.getCheckDigitSize())
        .padding(entity.getPadding())
        .dateFormat(entity.getDateFormat())
        .nextNumber(entity.getNextNumber())
        .numberIncrement(entity.getNumberIncrement())
        .processDefinitionId(Identifier.create(entity.getProcessDefinitionId().getId()))
        .build();
  }


  public ProcessSequenceDTO toDTO(ProcessSequence model) {
    var dto = new ProcessSequenceDTO();
    dto.setId(model.getId().getValue());
    dto.setName(model.getName().getValue());
    dto.setPrefix(model.getPrefix().getValue());
    dto.setCheckDigitSize(model.getCheckDigitSize());
    dto.setPadding(model.getPadding());
    dto.setDateFormat(model.getDateFormat());
    dto.setNextNumber(model.getNextNumber());
    dto.setNumberIncrement(model.getNumberIncrement());
    dto.setProcessDefinitionId(model.getProcessDefinitionId().getValue());
    return dto;
  }

}

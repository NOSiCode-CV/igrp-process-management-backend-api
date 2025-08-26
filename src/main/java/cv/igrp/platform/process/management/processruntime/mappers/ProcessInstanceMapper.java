package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.framework.runtime.core.engine.process.model.IGRPProcessStatus;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.StartProcessRequestDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProcessInstanceMapper {

  public ProcessInstanceEntity toEntity(ProcessInstance processInstance) {
    ProcessInstanceEntity processInstanceEntity = new ProcessInstanceEntity();
    processInstanceEntity.setNumber(processInstance.getNumber() != null ? processInstance.getNumber().getValue() : null);
    processInstanceEntity.setId(processInstance.getId().getValue());
    processInstanceEntity.setProcReleaseId(processInstance.getProcReleaseId().getValue());
    processInstanceEntity.setProcReleaseKey(processInstance.getProcReleaseKey().getValue());
    processInstanceEntity.setApplicationBase(processInstance.getApplicationBase().getValue());
    processInstanceEntity.setVersion(processInstance.getVersion());
    processInstanceEntity.setStatus(processInstance.getStatus());
    processInstanceEntity.setBusinessKey(processInstance.getBusinessKey() != null ? processInstance.getBusinessKey().getValue() : null);
    processInstanceEntity.setCanceledAt(processInstance.getCanceledAt());
    processInstanceEntity.setCanceledBy(processInstance.getCanceledBy());
    processInstanceEntity.setEndedAt(processInstance.getEndedAt());
    processInstanceEntity.setEndedBy(processInstance.getEndedBy());
    processInstanceEntity.setStartedAt(processInstance.getStartedAt());
    processInstanceEntity.setStartedBy(processInstance.getStartedBy());
    processInstanceEntity.setObsCancel(processInstance.getObsCancel());
    processInstanceEntity.setSearchTerms(processInstance.getSearchTerms());
    processInstanceEntity.setName(processInstance.getName());
    return processInstanceEntity;
  }

  public ProcessInstance toModel(ProcessInstanceEntity processInstanceEntity) {
    return ProcessInstance.builder()
        .id(Identifier.create(processInstanceEntity.getId()))
        .name(processInstanceEntity.getName())
        .number(Code.create(processInstanceEntity.getNumber()))
        .procReleaseKey(Code.create(processInstanceEntity.getProcReleaseKey()))
        .businessKey(processInstanceEntity.getBusinessKey() != null ? Code.create(processInstanceEntity.getBusinessKey()) : null)
        .procReleaseId(Code.create(processInstanceEntity.getProcReleaseId()))
        .status(processInstanceEntity.getStatus())
        .searchTerms(processInstanceEntity.getSearchTerms())
        .version(processInstanceEntity.getVersion())
        .startedAt(processInstanceEntity.getStartedAt())
        .endedAt(processInstanceEntity.getEndedAt())
        .canceledAt(processInstanceEntity.getCanceledAt())
        .startedBy(processInstanceEntity.getStartedBy())
        .endedBy(processInstanceEntity.getEndedBy())
        .canceledBy(processInstanceEntity.getCanceledBy())
        .obsCancel(processInstanceEntity.getObsCancel())
        .applicationBase(Code.create(processInstanceEntity.getApplicationBase()))

        .build();
  }

  public ProcessInstance toModel(StartProcessRequestDTO startProcessRequestDTO) {
    Map<String, Object> vars = new HashMap<>();
    startProcessRequestDTO.getVariables().forEach(variable -> {
      vars.put(variable.getName(), variable.getValue());
    });
    return ProcessInstance.builder()
        .procReleaseId(Code.create(startProcessRequestDTO.getProcessDefinitionId()))
        .procReleaseKey(Code.create(startProcessRequestDTO.getProcessKey()))
        .businessKey(startProcessRequestDTO.getBusinessKey() != null ? Code.create(startProcessRequestDTO.getBusinessKey()) : null)
        .applicationBase(Code.create(startProcessRequestDTO.getApplicationBase()))
        .variables(vars)
        .build();
  }

  public ProcessInstanceDTO toDTO(ProcessInstance processInstance) {
    ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
    processInstanceDTO.setId(processInstance.getId().getValue());
    processInstanceDTO.setNumber(processInstance.getNumber() != null ? processInstance.getNumber().getValue() : null);
    processInstanceDTO.setProcReleaseId(processInstance.getProcReleaseId().getValue());
    processInstanceDTO.setProcReleaseKey(processInstance.getProcReleaseKey().getValue());
    processInstanceDTO.setVersion(processInstance.getVersion());
    processInstanceDTO.setStatus(processInstance.getStatus());
    processInstanceDTO.setStatusDesc(processInstance.getStatus().getDescription());
    processInstanceDTO.setStartedAt(processInstance.getStartedAt());
    processInstanceDTO.setStartedBy(processInstance.getStartedBy());
    processInstanceDTO.setCanceledAt(processInstance.getCanceledAt());
    processInstanceDTO.setCanceledBy(processInstance.getCanceledBy());
    processInstanceDTO.setEndedAt(processInstance.getEndedAt());
    processInstanceDTO.setEndedBy(processInstance.getEndedBy());
    processInstanceDTO.setObsCancel(processInstance.getObsCancel());
    processInstanceDTO.setApplicationBase(processInstance.getApplicationBase().getValue());
    processInstanceDTO.setBusinessKey(processInstance.getBusinessKey() != null ? processInstance.getBusinessKey().getValue() : null);
    processInstanceDTO.setName(processInstance.getName());
    processInstanceDTO.setProgress(processInstance.getProgress());
    return processInstanceDTO;
  }

  public ProcessInstanceListPageDTO toDTO(PageableLista<ProcessInstance> processInstances) {
    ProcessInstanceListPageDTO dto = new ProcessInstanceListPageDTO();
    dto.setTotalElements(processInstances.getTotalElements());
    dto.setTotalPages(processInstances.getTotalPages());
    dto.setPageNumber(processInstances.getPageNumber());
    dto.setPageSize(processInstances.getPageSize());
    dto.setFirst(processInstances.isFirst());
    dto.setLast(processInstances.isLast());
    List<ProcessInstanceDTO> content = processInstances.getContent()
        .stream()
        .map(this::toDTO)
        .toList();
    dto.setContent(content);
    return dto;
  }

  public ProcessInstance toModel(cv.igrp.framework.runtime.core.engine.process.model.ProcessInstance processInstance) {

    if (processInstance == null) {
      return null;
    }

    return ProcessInstance.builder()
        .number(Code.create(processInstance.id()))
        .procReleaseId(processInstance.processDefinitionId() != null ? Code.create(processInstance.processDefinitionId()) : null)
        .procReleaseKey(processInstance.processDefinitionKey() != null ? Code.create(processInstance.processDefinitionKey()) : null)
        .businessKey(processInstance.businessKey() != null ? Code.create(processInstance.businessKey()) : null)
        .applicationBase(Code.create("igrp")) // ou mapeia de algum lado se houver contexto
        .version(processInstance.processDefinitionVersion() != null ? String.valueOf(processInstance.processDefinitionVersion()) : null)
        .startedBy(processInstance.initiator())
        .startedAt(Optional.ofNullable(processInstance.startDate())
            .map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
            .orElse(null))
        .endedAt(Optional.ofNullable(processInstance.completedDate())
            .map(d -> d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
            .orElse(null))
        .status(mapStatus(processInstance.status()))
        .variables(new HashMap<>()) // se houver alguma origem para as variáveis, mapeia aqui
        .name(processInstance.name() != null ? processInstance.name()  :  processInstance.processDefinitionName())
        .build();
  }

  private ProcessInstanceStatus mapStatus(IGRPProcessStatus igrpStatus) {
    if (igrpStatus == null) return ProcessInstanceStatus.CREATED;
    try {
      return ProcessInstanceStatus.valueOf(igrpStatus.name());
    } catch (IllegalArgumentException ex) {
      return ProcessInstanceStatus.CREATED; // fallback
    }
  }



}

package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class TaskInstanceMapper {

  public TaskInstanceEntity toEntity(TaskInstance taskInstance) {
    var entity = new TaskInstanceEntity();
    entity.setId(taskInstance.getId().getValue());
    //entity.setProcessInstanceId(taskInstance.getProcessInstanceId()); todo
    entity.setTaskKey(taskInstance.getTaskKey().getValue());
    entity.setTaskKeyDesc(taskInstance.getTaskKeyDesc().getValue());
    entity.setExternalId(taskInstance.getExternalId().getValue());
    entity.setStartedBy(taskInstance.getUser().getValue());
    entity.setStartedAt(taskInstance.getStartedAt());
    entity.setStatus(taskInstance.getStatus());
    entity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    entity.setSearchTerms(taskInstance.getSearchTerms());
    return entity;
  }


  public TaskInstance toModel(TaskInstanceEntity entity) {
    return TaskInstance.builder()
        .id(Identifier.create(entity.getId()))
        .processType(Code.create(entity.getProcessInstanceId().getProcReleaseKey())) // todo
        .processNumber(Code.create(entity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(entity.getTaskKey()))
        .taskKeyDesc(Code.create(entity.getTaskKey())) // todo
        .status(entity.getStatus())
        .startedBy(entity.getStartedBy())
        .startedAt(entity.getStartedAt())
        .searchTerms(entity.getSearchTerms())
        .processInstanceId(Code.create(entity.getProcessInstanceId().getId().toString()))
        .taskInstanceEvents(entity.getTaskinstanceevents()==null ? new ArrayList<>()
            :entity.getTaskinstanceevents()
            .stream()
            .map(this::toEventModel)
            .toList())
        .build();
  }


  public TaskInstanceDTO toDTO(TaskInstance model) {
    var dto = new TaskInstanceDTO();
    dto.setId(model.getId().getValue());
    dto.setStatus(model.getStatus());
    // todo more fields
    return dto;
  }


  public TaskInstanceListaPageDTO toDTO(PageableLista<TaskInstance> taskInstances) {
    var listDto = new TaskInstanceListaPageDTO();
    listDto.setTotalElements(taskInstances.getTotalElements());
    listDto.setTotalPages(taskInstances.getTotalPages());
    listDto.setPageNumber(taskInstances.getPageNumber());
    listDto.setPageSize(taskInstances.getPageSize());
    listDto.setFirst(taskInstances.isFirst());
    listDto.setLast(taskInstances.isLast());
    listDto.setContent(taskInstances.getContent()
        .stream()
        .map(this::toListDTO)
        .toList());
    return listDto;
  }


  public TaskInstanceListDTO toListDTO(TaskInstance model) {
    var dto = new TaskInstanceListDTO();
    dto.setId(model.getId().getValue());
    dto.setStatus(model.getStatus());
    dto.setStatusDesc(model.getStatus().getDescription());
    dto.setTaskKey(model.getTaskKey().getValue());
    dto.setTaskKeyDesc(model.getTaskKeyDesc().getValue());
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setProcessType(model.getProcessNumber().getValue());
    dto.setStartedAt(String.valueOf(model.getStartedAt()));
    return dto;
  }


  public TaskInstanceEventEntity toEventEntity(TaskInstanceEvent model,
                                               TaskInstanceEntity taskInstanceEntity) {
    var eventEntity = new TaskInstanceEventEntity();
    eventEntity.setId(UUID.randomUUID());
    eventEntity.setTaskInstanceId(taskInstanceEntity);
    eventEntity.setStatus(taskInstanceEntity.getStatus());
    eventEntity.setEventType(model.getEventType().getValue());
    eventEntity.setStartedAt(model.getStartedAt());
    eventEntity.setStartedBy(model.getStartedBy());
    eventEntity.setStartObs(model.getStartObs());
    eventEntity.setInputTask(model.getInputTask());
    eventEntity.setOutputTask(model.getOutputTask());
    eventEntity.setEndedAt(model.getEndedAt());
    eventEntity.setEndedBy(model.getEndedBy());
    eventEntity.setEndObs(model.getEndObs());
    return eventEntity;
  }


  public TaskInstanceEvent toEventModel(TaskInstanceEventEntity entity) {
    return TaskInstanceEvent.builder()
        .id(Identifier.create(entity.getId()))
        .taskInstanceId(Code.create(entity.getTaskInstanceId().getId().toString()))
        .eventType(Code.create(entity.getEventType()))
        .status(entity.getStatus())
        .startedAt(entity.getStartedAt())
        .startedBy(entity.getStartedBy())
        .startObs(entity.getStartObs())
        .inputTask(entity.getInputTask())
        .outputTask(entity.getOutputTask())
        .endedAt(entity.getEndedAt())
        .endedBy(entity.getEndedBy())
        .endObs(entity.getEndObs())
        .build();
  }


}

package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceEventListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceInfo;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import org.springframework.stereotype.Component;


@Component
public class TaskInstanceMapper {


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {
    var taskEntity = new TaskInstanceEntity();
    taskEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskEntity.setName(taskInstance.getName().getValue());
    taskEntity.setId(taskInstance.getId().getValue());
    taskEntity.setStartedBy(taskInstance.getStartedBy());
    taskEntity.setStartedAt(taskInstance.getStartedAt());
    taskEntity.setAssignedBy(taskInstance.getAssignedBy());
    taskEntity.setAssignedAt(taskInstance.getAssignedAt());
    taskEntity.setStatus(taskInstance.getStatus());
    taskEntity.setSearchTerms(taskInstance.getSearchTerms());
    return taskEntity;
  }



  public TaskInstanceEventEntity toNewTaskEventEntity(TaskInstanceEvent taskInstanceEvent) {

    var eventEntity = new TaskInstanceEventEntity();
    eventEntity.setId(taskInstanceEvent.getId().getValue());
    eventEntity.setEventType(taskInstanceEvent.getEventType());
    eventEntity.setStatus(taskInstanceEvent.getStatus());
    eventEntity.setPerformedAt(taskInstanceEvent.getPerformedAt());
    eventEntity.setPerformedBy(taskInstanceEvent.getPerformedBy());
    eventEntity.setObs(taskInstanceEvent.getObs());
    return eventEntity;
  }



  public TaskInstanceInfo toModelInfo(TaskInstanceEntity entity) {
    return TaskInstanceInfo.builder()
        .id(Identifier.create(entity.getId()))
        .processType(Code.create(entity.getProcessInstanceId().getProcReleaseKey()))
        .processNumber(Code.create(entity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(entity.getTaskKey()))
        .name(Name.create(entity.getName()))
        .status(entity.getStatus())
        .startedBy(Code.create(entity.getStartedBy()))
        .startedAt(entity.getStartedAt())
        .searchTerms(Code.create(entity.getSearchTerms()))
        .processInstanceId(Identifier.create(entity.getProcessInstanceId().getId()))
        .build();
  }



  public TaskInstanceInfo toModelWithEvents(TaskInstanceEntity taskEntity) {
    return TaskInstanceInfo.builder()
        .id(Identifier.create(taskEntity.getId()))
        .processType(Code.create(taskEntity.getProcessInstanceId().getProcReleaseKey()))
        .processNumber(Code.create(taskEntity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(taskEntity.getTaskKey()))
        .name(Name.create(taskEntity.getName()))
        .status(taskEntity.getStatus())
        .startedBy(Code.create(taskEntity.getStartedBy()))
        .startedAt(taskEntity.getStartedAt())
        .searchTerms(Code.create(taskEntity.getSearchTerms()))
        .processInstanceId(Identifier.create(taskEntity.getProcessInstanceId().getId()))
        .taskInstanceEvents(taskEntity.getTaskinstanceevents()
            .stream()
            .map(this::toEventModel)
            .toList())
        .build();
  }



  public TaskInstanceEvent toEventModel(TaskInstanceEventEntity eventEntity) {
    return TaskInstanceEvent.builder()
        .id(Identifier.create(eventEntity.getId()))
        .taskInstanceId(Identifier.create(eventEntity.getTaskInstanceId().getId()))
        .eventType(eventEntity.getEventType())
        .status(eventEntity.getStatus())
        .performedAt(eventEntity.getPerformedAt())
        .performedBy(eventEntity.getPerformedBy())
        .obs(eventEntity.getObs())
        .build();
  }



  public TaskInstanceListaPageDTO toDTO(PageableLista<TaskInstanceInfo> taskInstances) {
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



  public TaskInstanceListDTO toListDTO(TaskInstanceInfo model) {
    var dto = new TaskInstanceListDTO();
    dto.setId(model.getId().getValue());
    dto.setStatus(model.getStatus());
    dto.setStatusDesc(model.getStatus().getDescription());
    dto.setTaskKey(model.getTaskKey().getValue());
    dto.setName(model.getName().getValue());
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setProcessType(model.getProcessNumber().getValue());
    dto.setStartedAt(String.valueOf(model.getStartedAt()));
    return dto;
  }



  public TaskInstanceDTO toTaskDTO(TaskInstanceInfo taskInstanceInfo) {
    var dto = new TaskInstanceDTO();
    dto.setId(taskInstanceInfo.getId().getValue());
    dto.setProcessInstanceId(taskInstanceInfo.getProcessInstanceId().getValue());
    dto.setProcessType(taskInstanceInfo.getProcessType().getValue());
    dto.setApplicationBase(taskInstanceInfo.getApplicationBase().getValue());
    dto.setProcessNumber(taskInstanceInfo.getProcessNumber().getValue());
    dto.setTaskKey(taskInstanceInfo.getTaskKey().getValue());
    dto.setName(taskInstanceInfo.getName().getValue());
    dto.setExternalId(taskInstanceInfo.getExternalId().getValue());
    dto.setUser(taskInstanceInfo.getUser().getValue());
    dto.setSearchTerms(taskInstanceInfo.getSearchTerms().getValue());
    dto.setStartedAt(taskInstanceInfo.getStartedAt());
    dto.setStartedBy(taskInstanceInfo.getStartedBy().getValue());
    dto.setStatus(TaskInstanceStatus.CREATED);
    dto.setTaskInstanceEvents(taskInstanceInfo.getTaskInstanceEvents()
        .stream()
        .map(this::toEventListDTO)
        .toList());
    return dto;
  }


  public TaskInstanceEventListDTO toEventListDTO(TaskInstanceEvent event) {
    var eventDto = new TaskInstanceEventListDTO();
    eventDto.setId(event.getId().getValue());
    eventDto.setTaskInstanceId(event.getTaskInstanceId().getValue());
    eventDto.setEventType(event.getEventType().getCode());
    eventDto.setPerformedAt(event.getPerformedAt());
    eventDto.setPerformedBy(event.getPerformedBy());
    eventDto.setObs(event.getObs());
    eventDto.setStatus(event.getStatus());
    return eventDto;
  }


}

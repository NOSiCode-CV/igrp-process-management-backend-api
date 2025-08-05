package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceEventListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import cv.nosi.igrp.runtime.core.engine.task.model.IGRPTaskStatus;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;


@Component
public class TaskInstanceMapper {


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {
    var taskInstanceEntity = new TaskInstanceEntity();
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskInstanceEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskInstanceEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskInstanceEntity.setName(taskInstance.getName().getValue());
    taskInstanceEntity.setStartedBy(taskInstance.getStartedBy().getValue());
    taskInstanceEntity.setStartedAt(taskInstance.getStartedAt());
    taskInstanceEntity.setAssignedBy(taskInstance.getAssignedBy().getValue());
    taskInstanceEntity.setAssignedAt(taskInstance.getAssignedAt());
    taskInstanceEntity.setStatus(taskInstance.getStatus());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
    return taskInstanceEntity;
  }


  public TaskInstanceEntity toTaskEntity(TaskInstance taskInstance) {
    var taskInstanceEntity = new TaskInstanceEntity();
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setStatus(taskInstance.getStatus());
    taskInstanceEntity.setAssignedBy(taskInstance.getAssignedBy().getValue());
    taskInstanceEntity.setAssignedAt(taskInstance.getAssignedAt());
    taskInstanceEntity.setEndedBy(taskInstance.getEndedBy().getValue());
    taskInstanceEntity.setEndedAt(taskInstance.getEndedAt());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
    return taskInstanceEntity;
  }


  public TaskInstanceEventEntity toTaskEventEntity(TaskInstanceEvent taskInstanceEvent) {

    var eventEntity = new TaskInstanceEventEntity();
    eventEntity.setId(taskInstanceEvent.getId().getValue());
    eventEntity.setEventType(taskInstanceEvent.getEventType());
    eventEntity.setStatus(taskInstanceEvent.getStatus());
    eventEntity.setPerformedAt(taskInstanceEvent.getPerformedAt());
    eventEntity.setPerformedBy(taskInstanceEvent.getPerformedBy().getValue());
    eventEntity.setNote(taskInstanceEvent.getNote());
    if (taskInstanceEvent.getTaskInstanceId() != null) {
      var taskInstanceEntity = new TaskInstanceEntity();
      taskInstanceEntity.setId(taskInstanceEvent.getTaskInstanceId().getValue());
      eventEntity.setTaskInstanceId(taskInstanceEntity);
    }
    return eventEntity;
  }


  public TaskInstance toModel(TaskInstanceEntity entity) {
    return TaskInstance.builder()
        .id(Identifier.create(entity.getId()))
        .processType(Code.create(entity.getProcessInstanceId().getProcReleaseKey()))
        .processNumber(Code.create(entity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(entity.getTaskKey()))
        .name(Name.create(entity.getName()))
        .status(entity.getStatus())
        .startedBy(Code.create(entity.getStartedBy()))
        .startedAt(entity.getStartedAt())
        .searchTerms(entity.getSearchTerms())
        .processInstanceId(Identifier.create(entity.getProcessInstanceId().getId()))
        .build();
  }


  public TaskInstance toModelWithEvents(TaskInstanceEntity taskEntity) {
    return TaskInstance.builder()
        .id(Identifier.create(taskEntity.getId()))
        .processType(Code.create(taskEntity.getProcessInstanceId().getProcReleaseKey()))
        .processNumber(Code.create(taskEntity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(taskEntity.getTaskKey()))
        .name(Name.create(taskEntity.getName()))
        .status(taskEntity.getStatus())
        .startedBy(Code.create(taskEntity.getStartedBy()))
        .startedAt(taskEntity.getStartedAt())
        .searchTerms(taskEntity.getSearchTerms())
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
        .performedBy(Code.create(eventEntity.getPerformedBy()))
        .note(eventEntity.getNote())
        .build();
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
    dto.setName(model.getName().getValue());
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setProcessType(model.getProcessNumber().getValue());
    dto.setStartedAt(String.valueOf(model.getStartedAt()));
    return dto;
  }


  public TaskInstanceDTO toTaskDTO(TaskInstance taskInstance) {
    var dto = new TaskInstanceDTO();
    dto.setId(taskInstance.getId().getValue());
    dto.setProcessInstanceId(taskInstance.getProcessInstanceId().getValue());
    dto.setProcessType(taskInstance.getProcessType().getValue());
    dto.setApplicationBase(taskInstance.getApplicationBase().getValue());
    dto.setProcessNumber(taskInstance.getProcessNumber().getValue());
    dto.setTaskKey(taskInstance.getTaskKey().getValue());
    dto.setName(taskInstance.getName().getValue());
    dto.setExternalId(taskInstance.getExternalId().getValue());
    dto.setUser(taskInstance.getAssignedBy().getValue());
    dto.setSearchTerms(taskInstance.getSearchTerms());
    dto.setStartedAt(taskInstance.getStartedAt());
    dto.setStartedBy(taskInstance.getStartedBy().getValue());
    dto.setStatus(TaskInstanceStatus.CREATED);
    dto.setTaskInstanceEvents(taskInstance.getTaskInstanceEvents()
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
    eventDto.setPerformedBy(event.getPerformedBy().getValue());
    eventDto.setObs(event.getNote());
    eventDto.setStatus(event.getStatus());
    return eventDto;
  }


  public TaskInstance toModel(TaskInfo taskInfo) {

    if (taskInfo == null) {
      return null;
    }

    return TaskInstance.builder()
        .applicationBase(null) // Não disponível em TaskInfo
        .processType(null)     // Não disponível em TaskInfo
        .processInstanceId(Identifier.create(taskInfo.processInstanceId()))
        .processNumber(Code.create(taskInfo.processInstanceId()))   //Não disponível em TaskInfo
        .taskKey(Code.create(taskInfo.taskDefinitionKey()))
        .name(Name.create(taskInfo.name()))
        .searchTerms(null)     // Não disponível em TaskInfo
        .status(TaskInstanceStatus.CREATED) // assumindo CREATED por default
        .startedAt(taskInfo.createdTime()!=null ? convertToLocalDateTime(taskInfo.createdTime()) : null)
        .startedAt(null)
        .startedBy(taskInfo.assignee() != null ? Code.create(taskInfo.assignee()) : null)
        .assignedAt(null)      // Não disponível em TaskInfo
        .assignedBy(null)      // Não disponível em TaskInfo
        .endedAt(null)         // Não disponível em TaskInfo
        .endedBy(null)         //Não disponível em TaskInfo
        .taskVariables(Map.of()) // Não disponível em TaskInfo
        .taskInstanceEvents(null) // Não disponível em TaskInfo
        .build();
  }

  private LocalDateTime convertToLocalDateTime(Date createdTime) {
    return createdTime.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
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

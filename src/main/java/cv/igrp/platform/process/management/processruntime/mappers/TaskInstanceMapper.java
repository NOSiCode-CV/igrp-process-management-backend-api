package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceEventListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static cv.igrp.platform.process.management.shared.util.DateUtil.utilDateToLocalDateTime;


@Component
public class TaskInstanceMapper {


  public TaskInstance toModel(TaskInfo taskInfo) {
    return TaskInstance.builder()
        .processNumber(Code.create(taskInfo.processInstanceId()))
        .taskKey(Code.create(taskInfo.taskDefinitionKey()))
        .externalId(Code.create(taskInfo.id()))
        .name(Name.create(taskInfo.name()))
        .startedAt(utilDateToLocalDateTime.apply(taskInfo.createdTime()))
        .build();
  }


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {
    var taskInstanceEntity = new TaskInstanceEntity();
    taskInstanceEntity.setProcessNumber(taskInstance.getProcessNumber().getValue());
    taskInstanceEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskInstanceEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskInstanceEntity.setName(taskInstance.getName().getValue());
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskInstanceEntity.setProcessType(taskInstance.getProcessType()!=null
        ? taskInstance.getProcessType().getValue() : null);
    taskInstanceEntity.setStartedBy(taskInstance.getStartedBy().getValue());
    taskInstanceEntity.setStartedAt(taskInstance.getStartedAt());
    taskInstanceEntity.setStatus(taskInstance.getStatus());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
    if(taskInstance.getProcessInstanceId()!=null) {
        var processInstanceEntity = new ProcessInstanceEntity();
        processInstanceEntity.setId(taskInstance.getProcessInstanceId().getValue());
        taskInstanceEntity.setProcessInstanceId(processInstanceEntity);
    }
    return taskInstanceEntity;
  }


  public void toTaskEntity(TaskInstance taskInstance,TaskInstanceEntity taskInstanceEntity) {
    taskInstanceEntity.setStatus(taskInstance.getStatus());
    taskInstanceEntity.setAssignedBy(taskInstance.getAssignedBy() != null ? taskInstance.getAssignedBy().getValue() : null);
    taskInstanceEntity.setAssignedAt(taskInstance.getAssignedAt());
    taskInstanceEntity.setEndedBy(taskInstance.getEndedBy() != null ? taskInstance.getEndedBy().getValue() : null);
    taskInstanceEntity.setEndedAt(taskInstance.getEndedAt());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
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


  public TaskInstance toModel(TaskInstanceEntity taskInstanceEntity) {
    return toModel(taskInstanceEntity, false);
  }


  public TaskInstance toModelWithEvents(TaskInstanceEntity taskInstanceEntity) {
    return toModel(taskInstanceEntity, true);
  }


  public TaskInstance toModel(TaskInstanceEntity taskInstanceEntity, boolean withEvents) {
    return TaskInstance.builder()
        .processNumber(Code.create(taskInstanceEntity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(taskInstanceEntity.getTaskKey()))
        .externalId(Code.create(taskInstanceEntity.getExternalId()))
        .name(Name.create(taskInstanceEntity.getName()))
        .id(Identifier.create(taskInstanceEntity.getId()))
        .processInstanceId(Identifier.create(taskInstanceEntity.getProcessInstanceId().getId()))
        .processType(taskInstanceEntity.getProcessType() != null ? Code.create(taskInstanceEntity.getProcessType()) : null)
        .applicationBase(Code.create(taskInstanceEntity.getApplicationBase()))
        .status(taskInstanceEntity.getStatus())
        .startedAt(taskInstanceEntity.getStartedAt())
        .startedBy(Code.create(taskInstanceEntity.getStartedBy()))
        .assignedAt(taskInstanceEntity.getAssignedAt())
        .assignedBy(taskInstanceEntity.getAssignedBy()==null?null:Code.create(taskInstanceEntity.getAssignedBy()))
        .endedAt(taskInstanceEntity.getEndedAt())
        .endedBy(taskInstanceEntity.getEndedBy()==null?null:Code.create(taskInstanceEntity.getEndedBy()))
        .searchTerms(taskInstanceEntity.getSearchTerms())
        .taskInstanceEvents( withEvents ?
            taskInstanceEntity.getTaskinstanceevents().stream().map(this::toEventModel).toList()
            : new ArrayList<>()
        ).build();
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


  public TaskInstanceListaPageDTO toTaskInstanceListaPageDTO(PageableLista<TaskInstance> taskInstances) {
    var listDto = new TaskInstanceListaPageDTO();
    listDto.setTotalElements(taskInstances.getTotalElements());
    listDto.setTotalPages(taskInstances.getTotalPages());
    listDto.setPageNumber(taskInstances.getPageNumber());
    listDto.setPageSize(taskInstances.getPageSize());
    listDto.setFirst(taskInstances.isFirst());
    listDto.setLast(taskInstances.isLast());
    listDto.setContent(taskInstances.getContent()
        .stream()
        .map(this::toTaskInstanceListDTO)
        .toList());
    return listDto;
  }


  public TaskInstanceListDTO toTaskInstanceListDTO(TaskInstance model) {
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


  public TaskInstanceDTO toTaskInstanceDTO(TaskInstance taskInstance) {
    var dto = new TaskInstanceDTO();
    dto.setProcessNumber(taskInstance.getProcessNumber().getValue());
    dto.setTaskKey(taskInstance.getTaskKey().getValue());
    dto.setId(taskInstance.getId().getValue());
    dto.setName(taskInstance.getName().getValue());
    dto.setExternalId(taskInstance.getExternalId().getValue());
    dto.setProcessInstanceId(taskInstance.getProcessInstanceId().getValue());
    dto.setProcessType(taskInstance.getProcessType() != null ? taskInstance.getProcessType().getValue() : null);
    dto.setApplicationBase(taskInstance.getApplicationBase().getValue());
    dto.setStartedAt(taskInstance.getStartedAt());
    dto.setStartedBy(taskInstance.getStartedBy().getValue());
    dto.setStatus(taskInstance.getStatus());
    dto.setUser(taskInstance.getAssignedBy()!=null?taskInstance.getAssignedBy().getValue():null);
    dto.setSearchTerms(taskInstance.getSearchTerms());
    dto.setTaskInstanceEvents(new ArrayList<>());
    taskInstance.getTaskInstanceEvents()
        .forEach(e->dto.getTaskInstanceEvents().add(toEventListDTO(e)));
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


}

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
import org.springframework.stereotype.Component;


@Component
public class TaskInstanceMapper {


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {

    var taskEntity = new TaskInstanceEntity();
    taskEntity.setId(taskInstance.getId().getValue());
    taskEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskEntity.setName(taskInstance.getName().getValue());
    taskEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskEntity.setStartedBy(taskInstance.getUser().getValue());
    taskEntity.setStartedAt(taskInstance.getStartedAt());
    taskEntity.setStatus(taskInstance.getStatus());
    taskEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskEntity.setSearchTerms(taskInstance.getSearchTerms().getValue());

    var taskInstanceEvent = taskInstance.getTaskInstanceEvents().getFirst();
    var eventEntity = new TaskInstanceEventEntity();

    eventEntity.setId(taskInstance.getId().getValue());
    eventEntity.setEventType(taskInstanceEvent.getEventType().getValue());
    eventEntity.setStartedAt(taskInstanceEvent.getStartedAt());
    eventEntity.setStartedBy(taskInstanceEvent.getStartedBy());
    eventEntity.setStartObs(taskInstanceEvent.getStartObs());
    eventEntity.setInputTask(taskInstanceEvent.getInputTask());
    eventEntity.setOutputTask(taskInstanceEvent.getOutputTask());
    eventEntity.setEndedAt(taskInstanceEvent.getEndedAt());
    eventEntity.setEndedBy(taskInstanceEvent.getEndedBy());
    eventEntity.setEndObs(taskInstanceEvent.getEndObs());

    eventEntity.setTaskInstanceId(taskEntity);
    eventEntity.setStartedAt(taskInstance.getStartedAt());
    eventEntity.setStartedBy(taskInstance.getStartedBy());
    eventEntity.setStatus(taskEntity.getStatus());

    return taskEntity;
  }



  public TaskInstance toModel(TaskInstanceEntity entity) {
    return TaskInstance.builder()
        .id(Identifier.create(entity.getId()))
        .processType(Code.create(entity.getProcessInstanceId().getProcReleaseKey()))
        .processNumber(Code.create(entity.getProcessInstanceId().getNumber()))
        .taskKey(Code.create(entity.getTaskKey()))
        .name(Name.create(entity.getName()))
        .status(entity.getStatus())
        .startedBy(entity.getStartedBy())
        .startedAt(entity.getStartedAt())
        .searchTerms(Code.create(entity.getSearchTerms()))
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
        .startedBy(taskEntity.getStartedBy())
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
        .eventType(Code.create(eventEntity.getEventType()))
        .status(eventEntity.getStatus())
        .startedAt(eventEntity.getStartedAt())
        .startedBy(eventEntity.getStartedBy())
        .startObs(eventEntity.getStartObs())
        .inputTask(eventEntity.getInputTask())
        .outputTask(eventEntity.getOutputTask())
        .endedAt(eventEntity.getEndedAt())
        .endedBy(eventEntity.getEndedBy())
        .endObs(eventEntity.getEndObs())
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



  public TaskInstanceDTO toTaskDTO(TaskInstance model) {
    var dto = new TaskInstanceDTO();
    dto.setId(model.getId().getValue());
    dto.setProcessInstanceId(model.getProcessInstanceId().getValue());
    dto.setProcessType(model.getProcessType().getValue());
    dto.setApplicationBase(model.getApplicationBase().getValue());
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setTaskKey(model.getTaskKey().getValue());
    dto.setName(model.getName().getValue());
    dto.setExternalId(model.getExternalId().getValue());
    dto.setUser(model.getUser().getValue());
    dto.setSearchTerms(model.getSearchTerms().getValue());
    dto.setStartedAt(model.getStartedAt());
    dto.setStartedBy(model.getStartedBy());
    dto.setStatus(TaskInstanceStatus.CREATED);
    dto.setTaskInstanceEvents(model.getTaskInstanceEvents()
        .stream()
        .map(this::toEventListDTO)
        .toList());
    return dto;
  }


  public TaskInstanceEventListDTO toEventListDTO(TaskInstanceEvent model) {
    var eventDto = new TaskInstanceEventListDTO();
    eventDto.setId(model.getId().getValue());
    eventDto.setTaskInstanceId(model.getTaskInstanceId().getValue());
    eventDto.setEventType(model.getEventType().getValue());
    eventDto.setStartedAt(model.getStartedAt());
    eventDto.setStartedBy(model.getStartedBy());
    eventDto.setStartObs(model.getStartObs());
    eventDto.setEndedAt(model.getEndedAt());
    eventDto.setEndedBy(model.getEndedBy());
    eventDto.setEndObs(model.getEndObs());
    eventDto.setStatus(model.getStatus());
    return eventDto;
  }


}

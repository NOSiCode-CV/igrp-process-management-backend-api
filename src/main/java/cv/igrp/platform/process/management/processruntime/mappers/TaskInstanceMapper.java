package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.application.queries.GetAllMyTasksQuery;
import cv.igrp.platform.process.management.processruntime.application.queries.ListTaskInstancesQuery;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import cv.nosi.igrp.runtime.core.engine.task.model.TaskInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static cv.igrp.platform.process.management.shared.util.DateUtil.utilDateToLocalDateTime;


@Component
public class TaskInstanceMapper {

  private final TaskInstanceEventMapper eventMapper;

  public TaskInstanceMapper(TaskInstanceEventMapper eventMapper) {
    this.eventMapper = eventMapper;
  }


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
    taskInstanceEntity.setBusinessKey(taskInstance.getBusinessKey()!=null
        ? taskInstance.getBusinessKey().getValue() : null);
    taskInstanceEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskInstanceEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskInstanceEntity.setName(taskInstance.getName().getValue());
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskInstanceEntity.setProcessName(taskInstance.getProcessName()!=null
        ? taskInstance.getProcessName().getValue() : null);
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
        .processName(taskInstanceEntity.getProcessName() != null ? Code.create(taskInstanceEntity.getProcessName()) : null)
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
            taskInstanceEntity.getTaskinstanceevents().stream().map(eventMapper::toEventModel).toList()
            : new ArrayList<>()
        ).build();
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
    dto.setBusinessKey(taskInstance.getBusinessKey()!=null ? taskInstance.getBusinessKey().getValue() : null);
    dto.setExternalId(taskInstance.getExternalId().getValue());
    dto.setProcessInstanceId(taskInstance.getProcessInstanceId().getValue());
    dto.setProcessName(taskInstance.getProcessName() != null ? taskInstance.getProcessName().getValue() : null);
    dto.setApplicationBase(taskInstance.getApplicationBase().getValue());
    dto.setStartedAt(taskInstance.getStartedAt());
    dto.setStartedBy(taskInstance.getStartedBy().getValue());
    dto.setStatus(taskInstance.getStatus());
    dto.setUser(taskInstance.getAssignedBy()!=null?taskInstance.getAssignedBy().getValue():null);
    dto.setSearchTerms(taskInstance.getSearchTerms());
    dto.setTaskInstanceEvents(new ArrayList<>());
    taskInstance.getTaskInstanceEvents()
        .forEach(e->dto.getTaskInstanceEvents().add(eventMapper.toEventListDTO(e)));
    return dto;
  }


  public TaskInstanceFilter toFilter(ListTaskInstancesQuery query) {
    return TaskInstanceFilter.builder()
        .processInstanceId(query.getProcessInstanceId() != null ? Identifier.create(query.getProcessInstanceId()) : null)
        .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
        .processName(query.getProcessName() != null ? query.getProcessName().trim() : null)
        .user(query.getUser() != null ? Code.create(query.getUser()) : null)
        .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
        .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
        .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
        .page(query.getPage())
        .size(query.getSize())
        .build();
  }

  public TaskInstanceFilter toFilter(GetAllMyTasksQuery query) {
    return TaskInstanceFilter.builder()
        .processInstanceId(query.getProcessInstanceId() != null ? Identifier.create(query.getProcessInstanceId()) : null)
        .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
        .processName(query.getProcessName() != null ? query.getProcessName().trim() : null)
        //.user(query.getUser() != null ? Code.create(query.getUser()) : null) todo current user
        .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
        .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
        .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
        .page(query.getPage())
        .size(query.getSize())
        .build();
  }


}

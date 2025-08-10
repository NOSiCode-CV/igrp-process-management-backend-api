package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
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
        .externalId(Code.create(taskInfo.id()))
        .taskKey(Code.create(taskInfo.taskDefinitionKey()))
        .formKey(taskInfo.formKey()!=null ? Code.create(taskInfo.formKey()) : null)
        .name(Name.create(taskInfo.name()))
        .processNumber(Code.create(taskInfo.processInstanceId()))
        .startedAt(utilDateToLocalDateTime.apply(taskInfo.createdTime()))
        .build();
  }


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {
    var taskInstanceEntity = new TaskInstanceEntity();
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskInstanceEntity.setFormKey(taskInstance.getFormKey()!=null ? taskInstance.getFormKey().getValue() : null);
    taskInstanceEntity.setName(taskInstance.getName().getValue());
    taskInstanceEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskInstanceEntity.setProcessNumber(taskInstance.getProcessNumber().getValue());
    taskInstanceEntity.setProcessName(taskInstance.getProcessName()!=null
        ? taskInstance.getProcessName().getValue() : null);
    taskInstanceEntity.setBusinessKey(taskInstance.getBusinessKey()!=null
        ? taskInstance.getBusinessKey().getValue() : null);
    taskInstanceEntity.setApplicationBase(taskInstance.getApplicationBase().getValue());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
    taskInstanceEntity.setStatus(taskInstance.getStatus());
    taskInstanceEntity.setStartedBy(taskInstance.getStartedBy().getValue());
    taskInstanceEntity.setStartedAt(taskInstance.getStartedAt());
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
        .id(Identifier.create(taskInstanceEntity.getId()))
        .taskKey(Code.create(taskInstanceEntity.getTaskKey()))
        .formKey(taskInstanceEntity.getFormKey() != null ? Code.create(taskInstanceEntity.getFormKey()) : null)
        .name(Name.create(taskInstanceEntity.getName()))
        .externalId(Code.create(taskInstanceEntity.getExternalId()))
        .processInstanceId(Identifier.create(taskInstanceEntity.getProcessInstanceId().getId()))
        .processNumber(Code.create(taskInstanceEntity.getProcessInstanceId().getNumber()))
        .businessKey(taskInstanceEntity.getBusinessKey() != null ? Code.create(taskInstanceEntity.getBusinessKey()) : null)
        .processName(taskInstanceEntity.getProcessName() != null ? Code.create(taskInstanceEntity.getProcessName()) : null)
        .applicationBase(Code.create(taskInstanceEntity.getApplicationBase()))
        .status(taskInstanceEntity.getStatus())
        .searchTerms(taskInstanceEntity.getSearchTerms())
        .startedAt(taskInstanceEntity.getStartedAt())
        .startedBy(Code.create(taskInstanceEntity.getStartedBy()))
        .assignedAt(taskInstanceEntity.getAssignedAt())
        .assignedBy(taskInstanceEntity.getAssignedBy()==null?null:Code.create(taskInstanceEntity.getAssignedBy()))
        .endedAt(taskInstanceEntity.getEndedAt())
        .endedBy(taskInstanceEntity.getEndedBy()==null?null:Code.create(taskInstanceEntity.getEndedBy()))
        .taskInstanceEvents( withEvents ?
            taskInstanceEntity.getTaskinstanceevents().stream().map(eventMapper::toEventModel).toList()
            : new ArrayList<>()
        ).build();
  }


  public TaskInstanceListPageDTO toTaskInstanceListPageDTO(PageableLista<TaskInstance> taskInstances) {
    var listDto = new TaskInstanceListPageDTO();
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
    dto.setTaskKey(model.getTaskKey().getValue());
    dto.setFormKey(model.getFormKey()!=null ? model.getFormKey().getValue() : null);
    dto.setName(model.getName().getValue());
    dto.setProcessInstanceId(model.getProcessInstanceId().getValue().toString());
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setBusinessKey(model.getBusinessKey() != null ? model.getBusinessKey().getValue() : null);
    dto.setProcessName(model.getProcessName() != null ? model.getProcessName().getValue() : null);
    dto.setAssignedBy(model.getAssignedBy()!=null ? model.getAssignedBy().getValue(): null);
    dto.setStatus(model.getStatus());
    dto.setStatusDesc(model.getStatus().getDescription());
    dto.setStartedAt(String.valueOf(model.getStartedAt()));
    return dto;
  }


  public TaskInstanceDTO toTaskInstanceDTO(TaskInstance taskInstance) {
    var dto = new TaskInstanceDTO();
    dto.setTaskKey(taskInstance.getTaskKey().getValue());
    dto.setId(taskInstance.getId().getValue());
    dto.setFormKey(taskInstance.getFormKey()!=null ? taskInstance.getFormKey().getValue() : null);
    dto.setName(taskInstance.getName().getValue());
    dto.setExternalId(taskInstance.getExternalId().getValue());
    dto.setProcessInstanceId(taskInstance.getProcessInstanceId().getValue());
    dto.setProcessNumber(taskInstance.getProcessNumber().getValue());
    dto.setBusinessKey(taskInstance.getBusinessKey()!=null ? taskInstance.getBusinessKey().getValue() : null);
    dto.setProcessName(taskInstance.getProcessName() != null ? taskInstance.getProcessName().getValue() : null);
    dto.setApplicationBase(taskInstance.getApplicationBase().getValue());
    dto.setStatus(taskInstance.getStatus());
    dto.setStatusDesc(taskInstance.getStatus().getDescription());
    dto.setSearchTerms(taskInstance.getSearchTerms());
    dto.setStartedAt(taskInstance.getStartedAt());
    dto.setStartedBy(taskInstance.getStartedBy().getValue());
    dto.setAssignedBy(taskInstance.getAssignedBy()!=null?taskInstance.getAssignedBy().getValue():null);
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

  public TaskInstanceFilter toFilter(GetAllMyTasksQuery query, Code user) {
    return TaskInstanceFilter.builder()
        .user(user)
        .processInstanceId(query.getProcessInstanceId() != null ? Identifier.create(query.getProcessInstanceId()) : null)
        .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
        .processName(query.getProcessName() != null ? query.getProcessName().trim() : null)
        .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
        .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
        .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
        .page(query.getPage())
        .size(query.getSize())
        .build();
  }


}

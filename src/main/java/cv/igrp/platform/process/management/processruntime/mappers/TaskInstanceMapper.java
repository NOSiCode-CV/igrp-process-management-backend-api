package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.framework.runtime.core.engine.task.model.TaskInfo;
import cv.igrp.platform.process.management.processruntime.application.dto.*;
import cv.igrp.platform.process.management.processruntime.application.queries.GetAllMyTasksQuery;
import cv.igrp.platform.process.management.processruntime.application.queries.ListTaskInstancesQuery;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskStatistics;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.*;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static cv.igrp.platform.process.management.shared.util.DateUtil.utilDateToLocalDateTime;


@Component
public class TaskInstanceMapper {

  private final TaskInstanceEventMapper eventMapper;

  public TaskInstanceMapper(TaskInstanceEventMapper eventMapper) {
    this.eventMapper = eventMapper;
  }


  public TaskInstance toModel(TaskInfo taskInfo) {
    return TaskInstance.builder()
        .taskKey(Code.create(taskInfo.taskDefinitionKey()))
        .externalId(Code.create(taskInfo.id()))
        .formKey(taskInfo.formKey()!=null ? Code.create(taskInfo.formKey()) : null)
        .name(Name.create(taskInfo.name() != null ? taskInfo.name() : "NOT SET"))
        .startedAt(utilDateToLocalDateTime.apply(taskInfo.createdTime()))
        .candidateGroups(taskInfo.candidateGroups())
        .build();
  }


  public TaskInstanceEntity toNewTaskEntity(TaskInstance taskInstance) {
    var taskInstanceEntity = new TaskInstanceEntity();
    taskInstanceEntity.setId(taskInstance.getId().getValue());
    taskInstanceEntity.setTaskKey(taskInstance.getTaskKey().getValue());
    taskInstanceEntity.setFormKey(taskInstance.getFormKey()!=null ? taskInstance.getFormKey().getValue() : null);
    taskInstanceEntity.setName(taskInstance.getName().getValue());
    taskInstanceEntity.setExternalId(taskInstance.getExternalId().getValue());
    taskInstanceEntity.setCandidateGroups(!taskInstance.getCandidateGroups().isEmpty() ? String.join(",", taskInstance.getCandidateGroups()) : null);
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
    taskInstanceEntity.setPriority(taskInstance.getPriority());
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
    taskInstanceEntity.setPriority(taskInstance.getPriority());
    taskInstanceEntity.setSearchTerms(taskInstance.getSearchTerms());
  }


  public TaskInstance toModel(TaskInstanceEntity taskInstanceEntity) {
    return toModel(taskInstanceEntity, false);
  }


  public TaskInstance toModel(TaskInstanceEntity taskInstanceEntity, boolean withEvents) {
    var processInstance = taskInstanceEntity.getProcessInstanceId();
    return TaskInstance.builder()
        .id(Identifier.create(taskInstanceEntity.getId()))
        .taskKey(Code.create(taskInstanceEntity.getTaskKey()))
        .formKey(taskInstanceEntity.getFormKey()!=null ? Code.create(taskInstanceEntity.getFormKey()) : null)
        .name(Name.create(taskInstanceEntity.getName()))
        .externalId(Code.create(taskInstanceEntity.getExternalId()))
        .processInstanceId(Identifier.create(processInstance.getId()))
        .processNumber(ProcessNumber.create(processInstance.getNumber()))
        .engineProcessNumber(processInstance.getEngineProcessNumber())
        .businessKey(processInstance.getBusinessKey()!=null ? Code.create(processInstance.getBusinessKey()) : null)
        .processName(Code.create(processInstance.getName()))
        .processKey(Code.create(processInstance.getProcReleaseKey()))
        .applicationBase(Code.create(processInstance.getApplicationBase()))
        .status(taskInstanceEntity.getStatus())
        .searchTerms(taskInstanceEntity.getSearchTerms())
        .priority(taskInstanceEntity.getPriority())
        .startedAt(taskInstanceEntity.getStartedAt())
        .startedBy(Code.create(taskInstanceEntity.getStartedBy()))
        .assignedAt(taskInstanceEntity.getAssignedAt())
        .assignedBy(taskInstanceEntity.getAssignedBy()!=null ? Code.create(taskInstanceEntity.getAssignedBy()) : null)
        .endedAt(taskInstanceEntity.getEndedAt())
        .endedBy(taskInstanceEntity.getEndedBy()!=null ? Code.create(taskInstanceEntity.getEndedBy()) : null)
        .candidateGroups(taskInstanceEntity.getCandidateGroups()!=null ? List.of(taskInstanceEntity.getCandidateGroups().split(",")) : null)
        .taskInstanceEvents(withEvents ? eventMapper.toEventModelList(taskInstanceEntity.getTaskinstanceevents()) : null)
        .build();
  }


  public TaskInstanceListPageDTO toTaskInstanceListPageDTO(PageableLista<TaskInstance> taskInstances) {
    var listDto = new TaskInstanceListPageDTO();
    listDto.setTotalElements(taskInstances.getTotalElements());
    listDto.setTotalPages(taskInstances.getTotalPages());
    listDto.setPageNumber(taskInstances.getPageNumber());
    listDto.setPageSize(taskInstances.getPageSize());
    listDto.setFirst(taskInstances.isFirst());
    listDto.setLast(taskInstances.isLast());
    listDto.setContent(taskInstances.getContent().stream().map(this::toTaskInstanceListDTO).toList());
    return listDto;
  }


  public TaskInstanceListDTO toTaskInstanceListDTO(TaskInstance model) {
    var dto = new TaskInstanceListDTO();
    dto.setId(model.getId().getValue());
    dto.setTaskKey(model.getTaskKey().getValue());
    dto.setFormKey(model.getFormKey()!=null ? model.getFormKey().getValue() : null);
    dto.setName(model.getName().getValue());
    dto.setCandidateGroups(String.join(",", model.getCandidateGroups()));
    dto.setProcessNumber(model.getProcessNumber().getValue());
    dto.setProcessInstanceId(model.getProcessInstanceId().getValue().toString());
    dto.setBusinessKey(model.getBusinessKey()!=null ? model.getBusinessKey().getValue() : null);
    dto.setProcessName(model.getProcessName()!=null ? model.getProcessName().getValue() : null);
    dto.setProcessKey(model.getProcessKey()!=null ? model.getProcessKey().getValue() : null);
    dto.setStartedAt(model.getStartedAt());
    dto.setPriority(model.getPriority());
    dto.setAssignedBy(model.getAssignedBy()!=null ? model.getAssignedBy().getValue() : null);
    dto.setAssignedAt(model.getAssignedAt());
    dto.setEndedBy(model.getEndedBy()!=null ? model.getEndedBy().getValue() : null);
    dto.setEndedAt(model.getEndedAt());
    dto.setStatus(model.getStatus());
    dto.setStatusDesc(model.getStatus().getDescription());
    dto.setStartedBy(model.getStartedBy().getValue());
    dto.setVariables(toProcessVariableDTO(model.getVariables()));
    return dto;
  }


  public TaskInstanceDTO toTaskInstanceDTO(TaskInstance taskInstance) {
    var dto = new TaskInstanceDTO();
    dto.setTaskKey(taskInstance.getTaskKey().getValue());
    dto.setId(taskInstance.getId().getValue());
    dto.setFormKey(taskInstance.getFormKey()!=null ? taskInstance.getFormKey().getValue() : null);
    dto.setName(taskInstance.getName().getValue());
    dto.setExternalId(taskInstance.getExternalId().getValue());
    dto.setCandidateGroups(!taskInstance.getCandidateGroups().isEmpty() ? String.join(",", taskInstance.getCandidateGroups()) : null);
    dto.setProcessInstanceId(taskInstance.getProcessInstanceId().getValue());
    dto.setProcessNumber(taskInstance.getProcessNumber().getValue());
    dto.setProcessKey(taskInstance.getProcessKey() != null ? taskInstance.getProcessKey().getValue() : null);
    dto.setBusinessKey(taskInstance.getBusinessKey()!=null ? taskInstance.getBusinessKey().getValue() : null);
    dto.setProcessName(taskInstance.getProcessName() != null ? taskInstance.getProcessName().getValue() : null);
    dto.setApplicationBase(taskInstance.getApplicationBase().getValue());
    dto.setStatus(taskInstance.getStatus());
    dto.setStatusDesc(taskInstance.getStatus().getDescription());
    dto.setSearchTerms(taskInstance.getSearchTerms());
    dto.setPriority(taskInstance.getPriority());
    dto.setStartedAt(taskInstance.getStartedAt());
    dto.setStartedBy(taskInstance.getStartedBy().getValue());
    dto.setAssignedBy(taskInstance.getAssignedBy()!=null?taskInstance.getAssignedBy().getValue():null);
    dto.setAssignedAt(taskInstance.getAssignedAt());
    dto.setEndedBy(taskInstance.getEndedBy()!=null?taskInstance.getEndedBy().getValue():null);
    dto.setEndedAt(taskInstance.getEndedAt());
    dto.setTaskInstanceEvents(eventMapper.toEventListDTO(taskInstance.getTaskInstanceEvents()));
    dto.setVariables(toProcessVariableDTO(taskInstance.getVariables()));
    return dto;
  }


  public TaskInstanceFilter toFilter(ListTaskInstancesQuery query) {
    return TaskInstanceFilter.builder()
        .processInstanceId(query.getProcessInstanceId() != null ? Identifier.create(query.getProcessInstanceId()) : null)
        .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
        .processName((query.getProcessName() != null && !query.getProcessName().isBlank()) ? Name.create(query.getProcessName().trim()) : null)
        .candidateGroups(query.getCandidateGroups() != null ? Code.create(query.getCandidateGroups()) : null)
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
        .processName((query.getProcessName() != null && !query.getProcessName().isBlank()) ? Name.create(query.getProcessName().trim()) : null)
        .candidateGroups(query.getCandidateGroups() != null ? Code.create(query.getCandidateGroups()) : null)
        .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
        .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
        .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
        .page(query.getPage())
        .size(query.getSize())
        .build();
  }


  public List<TaskVariableDTO> toTaskVariableListDTO(Map<String,Object> variables) {
    return variables==null ? List.of() : variables.entrySet().stream()
        .map(e-> new TaskVariableDTO(e.getKey(),e.getValue()))
        .toList();
  }


  public List<ProcessVariableDTO> toProcessVariableDTO(Map<String,Object> variables){
    return variables==null ? List.of() : variables.entrySet().stream()
        .map(e-> new ProcessVariableDTO(e.getKey(),e.getValue()))
        .toList();
  }


  public TaskInstanceStatsDTO toTaskInstanceStatsDto(TaskStatistics taskStatistics) {
    return new TaskInstanceStatsDTO(
        taskStatistics.getTotalTaskInstances(),
        taskStatistics.getTotalAvailableTasks(),
        taskStatistics.getTotalAssignedTasks(),
        taskStatistics.getTotalSuspendedTasks(),
        taskStatistics.getTotalCompletedTasks(),
        taskStatistics.getTotalCanceledTasks()
    );
  }

}

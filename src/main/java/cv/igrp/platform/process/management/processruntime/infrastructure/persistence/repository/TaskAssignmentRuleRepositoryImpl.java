package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRule;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskAssignmentRuleRepository;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskAssignmentRuleEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskAssignmentRuleEntityRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TaskAssignmentRuleRepositoryImpl implements TaskAssignmentRuleRepository {

  private final TaskAssignmentRuleEntityRepository repository;

  public TaskAssignmentRuleRepositoryImpl(TaskAssignmentRuleEntityRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(TaskAssignmentRule rule) {
    repository.save(toEntity(rule));
  }

  private TaskAssignmentRuleEntity toEntity(TaskAssignmentRule rule) {
    var entity = new TaskAssignmentRuleEntity();
    entity.setId(rule.getId().getValue());
    entity.setProcessDefinitionKey(rule.getProcessDefinitionKey().getValue());
    entity.setProcessInstanceId(toProcessInstanceEntity(rule));
    entity.setTaskDefinitionKey(rule.getTaskDefinitionKey().getValue());
    entity.setAssignee(rule.getAssignee() != null ? rule.getAssignee().getValue() : null);
    entity.getCandidateUsers().clear();
    entity.getCandidateUsers().addAll(rule.getCandidateUsers());
    entity.setAssignmentMode(rule.getAssignmentMode());
    entity.setConsumed(rule.isConsumed());
    entity.setActive(rule.isActive());
    entity.setCreatedByTask(toTaskInstanceEntity(rule));
    return entity;
  }

  private ProcessInstanceEntity toProcessInstanceEntity(TaskAssignmentRule rule) {
    if (rule.getProcessInstanceId() == null) {
      return null;
    }
    var processInstance = new ProcessInstanceEntity();
    processInstance.setId(rule.getProcessInstanceId().getValue());
    return processInstance;
  }

  private TaskInstanceEntity toTaskInstanceEntity(TaskAssignmentRule rule) {
    if (rule.getCreatedByTask() == null) {
      return null;
    }
    var taskInstance = new TaskInstanceEntity();
    taskInstance.setId(rule.getCreatedByTask().getValue());
    return taskInstance;
  }
}

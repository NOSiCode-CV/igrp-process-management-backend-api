package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRule;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRuleFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskAssignmentRuleRepository;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

@Service
public class TaskAssignmentRuleService {

  private final TaskAssignmentRuleRepository repository;

  public TaskAssignmentRuleService(TaskAssignmentRuleRepository repository) {
    this.repository = repository;
  }

  public PageableLista<TaskAssignmentRule> getAll(TaskAssignmentRuleFilter filter) {
    return repository.findAll(filter);
  }
}

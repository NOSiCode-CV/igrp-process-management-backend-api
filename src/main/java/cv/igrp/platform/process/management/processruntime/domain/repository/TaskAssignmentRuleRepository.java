package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRule;

public interface TaskAssignmentRuleRepository {

  void save(TaskAssignmentRule rule);

}

package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.TaskPriority;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;

import java.util.List;
import java.util.Optional;

public interface TaskPriorityRepository {

  TaskPriority savePriority(TaskPriority taskPriority);

  List<TaskPriority> findAllPriority(Code processDefinitionKey);

  Optional<TaskPriority> findPriorityById(Identifier id);

  void deletePriority(TaskPriority taskPriority);

}

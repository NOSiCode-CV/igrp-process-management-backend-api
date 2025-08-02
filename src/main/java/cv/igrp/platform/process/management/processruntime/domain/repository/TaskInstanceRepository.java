package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceInfo;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {

  Optional<TaskInstanceInfo> findById(UUID id);

  String getExternalIdByTaskId(UUID id);

  TaskInstanceInfo create(TaskInstance taskInstance);

  void updateTask(TaskInstance taskInstance);

  TaskInstanceInfo completeTask(TaskInstance taskInstance);

  PageableLista<TaskInstanceInfo> findAll(TaskInstanceFilter filter);
}

package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {

  TaskInstance create(TaskInstance taskInstance);

  PageableLista<TaskInstance> findAll(TaskInstanceFilter filter);

  Optional<TaskInstance> findById(UUID id);

  void updateStatus(UUID id, TaskInstanceStatus newStatus);

  void assigneTask(UUID id, Code user, TaskInstanceStatus status, LocalDateTime date);

  void releaseTask(UUID id, TaskInstanceStatus status);

}

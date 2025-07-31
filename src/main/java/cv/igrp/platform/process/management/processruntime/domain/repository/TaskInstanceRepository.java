package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {


  Optional<TaskInstance> findById(UUID id);

  String getExternalIdByTaskId(UUID id);

  TaskInstance create(TaskInstance taskInstance, Code externalId, TaskEventType taskEventType);

  void updateTask(UUID id, TaskEventType taskEventType, Code user,
                  TaskInstanceStatus taskInstanceStatus, LocalDateTime dateTime);

  TaskInstance completeTask(UUID id, TaskEventType taskEventType, Code user,
                            TaskInstanceStatus taskInstanceStatus, LocalDateTime dateTime);

  PageableLista<TaskInstance> findAll(TaskInstanceFilter filter);
}

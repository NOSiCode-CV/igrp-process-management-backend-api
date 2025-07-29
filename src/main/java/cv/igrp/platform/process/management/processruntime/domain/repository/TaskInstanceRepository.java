package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {

  TaskInstance save(TaskInstance taskInstance);

  PageableLista<TaskInstance> findAll(TaskInstanceFilter filter);

  Optional<TaskInstance> findById(UUID id);

  void updateStatus(UUID id, TaskInstanceStatus newStatus);

}

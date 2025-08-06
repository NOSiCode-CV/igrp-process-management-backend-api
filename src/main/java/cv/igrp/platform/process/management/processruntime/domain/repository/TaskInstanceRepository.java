package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {

  Optional<TaskInstance> findById(UUID id);

  Optional<TaskInstance> findByIdWihEvents(UUID id);

  TaskInstance create(TaskInstance taskInstance);

  TaskInstance update(TaskInstance taskInstance);

  PageableLista<TaskInstance> findAll(TaskInstanceFilter filter);
}

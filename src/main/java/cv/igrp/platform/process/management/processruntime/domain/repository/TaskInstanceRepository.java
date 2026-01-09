package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskStatistics;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceRepository {

  Optional<TaskInstance> findById(UUID id);

  Optional<TaskInstance> findByIdWithEvents(UUID id);

  void create(TaskInstance taskInstance);

  void update(TaskInstance taskInstance);

  PageableLista<TaskInstance> findAll(TaskInstanceFilter filter);

  TaskStatistics getGlobalTaskStatistics();

  TaskStatistics getTaskStatisticsByUser(Code user);

  Optional<TaskInstance> findByExternalId(String id);
}

package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceEventRepository {

  Optional<TaskInstanceEvent> findById(UUID id);

  List<TaskInstanceEvent> findAll(TaskInstanceFilter filter);

}

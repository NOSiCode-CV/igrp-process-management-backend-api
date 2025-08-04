package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceEventRepository {

    Optional<TaskInstanceEvent> findById(UUID id);

    public void save( TaskInstanceEvent taskInstanceEvent);

    List<TaskInstanceEvent> findAll(TaskInstanceFilter filter);

    PageableLista<TaskInstanceEvent> getTaskHistory(TaskInstanceFilter filter);
}

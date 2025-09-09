package cv.igrp.platform.process.management.processruntime.domain.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;

import java.util.Optional;
import java.util.UUID;

public interface TaskInstanceEventRepository {

    Optional<TaskInstanceEvent> findById(UUID id);

    void save( TaskInstanceEvent taskInstanceEvent);

}

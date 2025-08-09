package cv.igrp.platform.process.management.processruntime.domain.repository;


import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface ProcessInstanceRepository {

  ProcessInstance save(ProcessInstance area);

  PageableLista<ProcessInstance> findAll(ProcessInstanceFilter filter);

  Optional<ProcessInstance> findById(UUID id);

}

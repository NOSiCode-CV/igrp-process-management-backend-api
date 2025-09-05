package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;

import java.util.Optional;

public interface ProcessSequenceRepository {

  Optional<ProcessSequence> findByProcessAndApplication(String processDefinitionKey, String applicationCode);

  Optional<ProcessSequence> findForUpdate(String processDefinitionKey, String applicationCode);

  ProcessSequence save(ProcessSequence model);

}

package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;

import java.util.Optional;

public interface ProcessSequenceRepository {

  Optional<ProcessSequence> findByProcessDefinitionKey(String processDefinitionKey);

  Optional<ProcessSequence> findForUpdate(String processDefinitionKey);

  ProcessSequence save(ProcessSequence model);

}

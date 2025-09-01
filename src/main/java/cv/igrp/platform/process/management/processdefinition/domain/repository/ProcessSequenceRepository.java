package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;

import java.util.Optional;

public interface ProcessSequenceRepository {

  Optional<ProcessSequence> findByProcessDefinitionId(String id);

  Optional<ProcessSequence> findByProcessDefinitionIdForUpdate(String id);

  ProcessSequence save(ProcessSequence model);

}

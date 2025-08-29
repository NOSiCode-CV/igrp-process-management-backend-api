package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;

import java.util.Optional;
import java.util.UUID;

public interface ProcessSequenceRepository {

  Optional<ProcessSequence> getFindById(UUID id);

  ProcessSequence save(ProcessSequence model);

}

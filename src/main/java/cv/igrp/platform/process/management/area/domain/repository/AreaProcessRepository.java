package cv.igrp.platform.process.management.area.domain.repository;

import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;

public interface AreaProcessRepository {

  AreaProcess save(AreaProcess area);

  PageableLista<AreaProcess> findAll(AreaProcessFilter filter);

  Optional<AreaProcess> findById(UUID id);

  void updateStatus(UUID id, Status newStatus);

}

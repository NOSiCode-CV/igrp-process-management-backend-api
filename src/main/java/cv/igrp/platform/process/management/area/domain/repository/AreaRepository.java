package cv.igrp.platform.process.management.area.domain.repository;


import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaFilter;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;

import java.util.Optional;
import java.util.UUID;


public interface AreaRepository {

  Area save(Area area);

  PageableLista<Area> findAll(AreaFilter filter);

  Optional<Area> findById(UUID id);

  void updateStatus(UUID id, Status newStatus);

  void deleteArea(UUID id);

}

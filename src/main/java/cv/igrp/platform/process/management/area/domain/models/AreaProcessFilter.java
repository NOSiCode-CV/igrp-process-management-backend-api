package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;


@Getter
public class AreaProcessFilter {

  private final Code processKey;
  private final Code releaseId;
  private final Status status;
  private final Identifier areaId;
  private final Integer page;
  private final Integer size;

  @Builder
  private AreaProcessFilter(Code processKey,
                           Code releaseId,
                           Status status,
                           Identifier areaId,
                           Integer page,
                           Integer size) {
    this.processKey = processKey;
    this.releaseId = releaseId;
    this.status = status == null ? Status.ACTIVE : status;
    this.areaId = areaId;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }

}

package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;


@Getter
public class AreaFilter {

  private final Code code;
  private final Name name;
  private final Code applicationBase;
  private final Status status;
  private final Identifier parentId;
  private final Integer page;
  private final Integer size;

  @Builder
  private AreaFilter(Code code,
                    Name name,
                    Code applicationBase,
                    Status status,
                    Identifier parentId,
                    Integer page,
                    Integer size) {
    this.code = code;
    this.name = name;
    this.applicationBase = applicationBase;
    this.status = status == null ? Status.ACTIVE : status;
    this.parentId = parentId;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }

  // Factory method goes here :-)

}

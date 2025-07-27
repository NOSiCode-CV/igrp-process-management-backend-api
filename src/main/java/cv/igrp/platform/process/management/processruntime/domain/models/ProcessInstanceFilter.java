package cv.igrp.platform.process.management.processruntime.domain.models;


import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProcessInstanceFilter {

  private final Code number;
  private final Code procReleaseKey;
  private final Code procReleaseId;
  private final ProcessInstanceStatus status;
  private final Integer page;
  private final Integer size;

  @Builder
  private ProcessInstanceFilter(Code number,
                               Code procReleaseKey,
                               Code procReleaseId,
                               ProcessInstanceStatus status,
                               Integer page,
                               Integer size) {
    this.number = number;
    this.procReleaseKey = procReleaseKey;
    this.procReleaseId = procReleaseId;
    this.status = status;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }

  // Factory method goes here :-)

}

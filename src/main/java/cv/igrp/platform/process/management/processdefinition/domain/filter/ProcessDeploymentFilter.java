package cv.igrp.platform.process.management.processdefinition.domain.filter;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcessDeploymentFilter {

  private String processName;
  private final Code applicationBase;
  private Integer pageNumber;
  private Integer pageSize;
}

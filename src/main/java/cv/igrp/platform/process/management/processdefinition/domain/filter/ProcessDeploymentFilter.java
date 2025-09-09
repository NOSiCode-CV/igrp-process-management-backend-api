package cv.igrp.platform.process.management.processdefinition.domain.filter;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class ProcessDeploymentFilter {

  private String processName;
  private final Code applicationBase;
  private Integer pageNumber;
  private Integer pageSize;

  @Builder
  public ProcessDeploymentFilter(String processName, Code applicationBase, Integer pageNumber, Integer pageSize) {
    this.processName = processName;
    this.applicationBase = applicationBase;
    this.pageNumber = pageNumber == null ? 0 : pageNumber;
    this.pageSize = pageSize == null ? 20 : pageSize;
  }

}

package cv.igrp.platform.process.management.processruntime.domain.models;


import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProcessInstanceFilter {

  private final Code number;
  private final Code procReleaseKey;
  private final Code procReleaseId;
  private final ProcessInstanceStatus status;
  private final Code applicationBase;
  private final Integer page;
  private final Integer size;
  private final List<VariablesExpression> variablesExpressions;

  @Builder
  private ProcessInstanceFilter(Code number,
                                Code procReleaseKey,
                                Code procReleaseId,
                                ProcessInstanceStatus status,
                                Code applicationBase,
                                Integer page,
                                Integer size,
                                List<VariablesExpression> variablesExpressions) {
    this.number = number;
    this.procReleaseKey = procReleaseKey;
    this.procReleaseId = procReleaseId;
    this.status = status;
    this.applicationBase = applicationBase;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
    this.variablesExpressions = variablesExpressions ==  null ? new ArrayList<>() : variablesExpressions;
  }

  // Factory method goes here :-)

}

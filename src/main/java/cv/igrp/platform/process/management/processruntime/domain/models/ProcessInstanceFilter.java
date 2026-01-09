package cv.igrp.platform.process.management.processruntime.domain.models;


import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
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
  private final List<String> includeProcessNumbers;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final Name name;

  @Builder
  private ProcessInstanceFilter(Code number,
                                Code procReleaseKey,
                                Code procReleaseId,
                                ProcessInstanceStatus status,
                                Code applicationBase,
                                Integer page,
                                Integer size,
                                List<VariablesExpression> variablesExpressions,
                                List<String> includeProcessNumbers,
                                LocalDate dateFrom,
                                LocalDate dateTo,
                                Name name) {
    this.number = number;
    this.procReleaseKey = procReleaseKey;
    this.procReleaseId = procReleaseId;
    this.status = status;
    this.applicationBase = applicationBase;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
    this.variablesExpressions = variablesExpressions ==  null ? new ArrayList<>() : variablesExpressions;
    this.includeProcessNumbers = includeProcessNumbers == null ? new ArrayList<>() : includeProcessNumbers;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.name = name;
  }

  public void includeProcessNumber(String processNumber){
    this.includeProcessNumbers.add(processNumber);
  }

  // Factory method goes here :-)

}

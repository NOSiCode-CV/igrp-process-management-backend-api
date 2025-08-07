package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskInstanceFilter {
  private final Identifier processInstanceId;
  private final Code processNumber;
  private final String processName;
  private final Code user;
  private final TaskInstanceStatus status;
  private final String searchTerms;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final Integer page;
  private final Integer size;

  @Builder
  private TaskInstanceFilter(
                             Identifier processInstanceId,
                             Code processNumber,
                             String processName,
                             Code user,
                             TaskInstanceStatus status,
                             String searchTerms,
                             LocalDate dateFrom,
                             LocalDate dateTo,
                             Integer page,
                             Integer size) {
    this.processInstanceId = processInstanceId;
    this.processNumber = processNumber;
    this.processName = processName;
    this.user = user;
    this.status = status;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.searchTerms = searchTerms;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }

}

package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskInstanceFilter {
  private final Code processNumber;
  private final Code processKey;
  private final Code user;
  private final TaskInstanceStatus status;
  private final String searchTerms;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final Integer page;
  private final Integer size;

  @Builder
  private TaskInstanceFilter(Code processNumber,
                             Code processKey,
                             Code user,
                             TaskInstanceStatus status,
                             String searchTerms,
                             LocalDate dateFrom,
                             LocalDate dateTo,
                             Integer page,
                             Integer size) {
    this.processNumber = processNumber;
    this.processKey = processKey;
    this.user = user;
    this.status = status;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.searchTerms = searchTerms;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }

}

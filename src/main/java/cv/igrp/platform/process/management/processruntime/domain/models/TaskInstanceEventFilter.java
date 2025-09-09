package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskInstanceEventFilter {
  private final Identifier taskInstanceId;
  private final Code taskExternalId;
  private final Code taskKey;
  private final Name taskName;
  private final Code eventType;
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
  public TaskInstanceEventFilter(Identifier taskInstanceId,
                                 Code taskExternalId,
                                 Code taskKey,
                                 Name taskName,
                                 Code eventType,
                                 Code processNumber,
                                 Code processKey,
                                 Code user,
                                 TaskInstanceStatus status,
                                 String searchTerms,
                                 LocalDate dateFrom,
                                 LocalDate dateTo,
                                 Integer page,
                                 Integer size) {
    this.taskInstanceId = taskInstanceId;
    this.taskExternalId = taskExternalId;
    this.taskKey = taskKey;
    this.taskName = taskName;
    this.eventType = eventType;
    this.processNumber = processNumber;
    this.processKey = processKey;
    this.user = user;
    this.status = status;
    this.searchTerms = searchTerms;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
  }
}

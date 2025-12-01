package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskInstanceFilter {
  private final Identifier processInstanceId;
  private final Code processNumber;
  private final Code applicationBase;
  private final Name processName;
  private final Code candidateGroups;
  private final TaskInstanceStatus status;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final Integer page;
  private final Integer size;
  @Setter
  private Code user;
  private List<VariablesExpression> variablesExpressions;
  private List<String> includeTaskIds;

  @Builder
  private TaskInstanceFilter(
                             Identifier processInstanceId,
                             Code processNumber,
                             Code applicationBase,
                             Name processName,
                             Code candidateGroups,
                             Code user,
                             TaskInstanceStatus status,
                             String searchTerms,
                             LocalDate dateFrom,
                             LocalDate dateTo,
                             Integer page,
                             Integer size,
                             List<VariablesExpression> variablesExpressions,
                             List<String> includeTaskIds
                             ) {
    this.processInstanceId = processInstanceId;
    this.applicationBase = applicationBase;
    this.processNumber = processNumber;
    this.processName = processName;
    this.candidateGroups = candidateGroups;
    this.user = user;
    this.status = status;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
    this.variablesExpressions = variablesExpressions ==  null ? new ArrayList<>() : variablesExpressions;
    this.includeTaskIds = includeTaskIds == null ? new ArrayList<>() : includeTaskIds;
  }

  public void includeTaskId(String taskId){
    this.includeTaskIds.add(taskId);
  }

}

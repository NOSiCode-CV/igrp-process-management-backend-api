package cv.igrp.platform.process.management.processdefinition.domain.filter;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ProcessDeploymentFilter {

  private String processName;
  private final Code applicationBase;
  private Integer pageNumber;
  private Integer pageSize;

  private boolean filterByCurrentUser;
  private Set<String> contextGroups;
  private Set<String> groups;

  private boolean isSuspended;

  @Builder
  public ProcessDeploymentFilter(String processName,
                                 Code applicationBase,
                                 Integer pageNumber,
                                 Integer pageSize,
                                 boolean filterByCurrentUser,
                                 Set<String> contextGroups,
                                 Set<String> groups,
                                 boolean isSuspended
  ) {
    this.processName = processName;
    this.applicationBase = applicationBase;
    this.pageNumber = pageNumber == null ? 0 : pageNumber;
    this.pageSize = pageSize == null ? 20 : pageSize;
    this.filterByCurrentUser = filterByCurrentUser;
    this.contextGroups = contextGroups == null ? new HashSet<>() : contextGroups;
    this.groups = groups == null ? new HashSet<>() : groups;
    this.isSuspended = isSuspended;
  }

  public void addContextGroup(String contextGroup) {
    this.contextGroups.add(contextGroup);
  }

}

package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class ProcessInstance {

  private final Identifier id;
  private final Code procReleaseKey;
  private final Code procReleaseId;
  private Code number;
  private Code businessKey;
  private String version;
  private String searchTerms;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
  private LocalDateTime canceledAt;
  private String startedBy;
  private String endedBy;
  private String canceledBy;
  private String obsCancel;
  private ProcessInstanceStatus status;

  @Builder
  public ProcessInstance(Identifier id,
                         Code procReleaseKey,
                         Code procReleaseId,
                         Code number,
                         Code businessKey,
                         String version,
                         String searchTerms,
                         LocalDateTime startedAt,
                         LocalDateTime endedAt,
                         LocalDateTime canceledAt,
                         String startedBy,
                         String endedBy,
                         String canceledBy,
                         String obsCancel,
                         ProcessInstanceStatus status) {
    this.id = id == null ? Identifier.generate() : id;
    this.procReleaseKey = Objects.requireNonNull(procReleaseKey, "Process release key cannot be null");
    this.procReleaseId = Objects.requireNonNull(procReleaseId, "Process release id cannot be null");;
    this.number = number;
    this.businessKey = businessKey;
    this.version = version;
    this.searchTerms = searchTerms;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.canceledAt = canceledAt;
    this.startedBy = startedBy;
    this.endedBy = endedBy;
    this.canceledBy = canceledBy;
    this.obsCancel = obsCancel;
    this.status = status == null ? ProcessInstanceStatus.CREATED : status;
  }

  public void start(){
    this.status = ProcessInstanceStatus.RUNNING;
    this.startedAt = LocalDateTime.now();
  }

}

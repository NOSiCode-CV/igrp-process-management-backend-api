package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
public class ProcessInstance {

  private final Identifier id;
  private final Code procReleaseKey;
  private final Code procReleaseId;
  private Code number;
  private Code businessKey;
  private Code applicationBase;
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
  private Map<String, Object> variables;
  private String name;
  private String progress;

  @Builder
  public ProcessInstance(Identifier id,
                         Code procReleaseKey,
                         Code procReleaseId,
                         Code number,
                         Code businessKey,
                         Code applicationBase,
                         String version,
                         String searchTerms,
                         LocalDateTime startedAt,
                         LocalDateTime endedAt,
                         LocalDateTime canceledAt,
                         String startedBy,
                         String endedBy,
                         String canceledBy,
                         String obsCancel,
                         ProcessInstanceStatus status,
                         Map<String, Object> variables,
                         String name,
                         String progress
                         ) {
    this.id = id == null ? Identifier.generate() : id;
    this.procReleaseKey = Objects.requireNonNull(procReleaseKey, "Process release key cannot be null");
    this.procReleaseId = Objects.requireNonNull(procReleaseId, "Process release id cannot be null");
    this.number = number;
    this.businessKey = businessKey == null ? Code.create(generateBusinessKey()) : businessKey;
    this.applicationBase = Objects.requireNonNull(applicationBase, "Application base cannot be null");;
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
    this.variables = variables == null ? new HashMap<>() : variables;
    this.name = name;
    this.progress = progress;
  }

  public void start(Code processInstanceId, String version, String processName, String startedBy){
    if(this.status != ProcessInstanceStatus.CREATED && this.status != ProcessInstanceStatus.SUSPENDED){
      throw new IllegalStateException("The status of the process instance must be CREATED or SUSPENDED");
    }
    if(startedBy == null || startedBy.isBlank()){
      throw new IllegalStateException("The started by (user) of the process instance cannot be null or blank");
    }
    this.status = ProcessInstanceStatus.RUNNING;
    this.startedAt = LocalDateTime.now();
    this.number = processInstanceId;
    this.version = version == null || version.isBlank() ? "1.0" : version;
    this.name = processName;
    this.startedBy = startedBy;
  }

  public void complete(LocalDateTime endedAt, String endedBy){
    if(endedBy == null || endedBy.isBlank()){
      throw new IllegalStateException("The endedBy (user) of the process instance cannot be null or blank");
    }
    this.status = ProcessInstanceStatus.COMPLETED;
    this.endedBy = endedBy;
    this.endedAt = endedAt == null ? LocalDateTime.now() : endedAt;
  }

  public static String generateBusinessKey(){
    return UUID.randomUUID().toString();
  }

  public void setProgress(int totalTasks, int completedTasks){
    this.progress = String.format("%d/%d", completedTasks, totalTasks);
  }

}

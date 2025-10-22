package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
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

  public static final Integer DEFAULT_PRIORITY = 0;

  private final Identifier id;
  private final Code procReleaseKey;
  private final Code procReleaseId;
  private ProcessNumber number;
  private Code engineProcessNumber;
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
  private String name;
  private String progress;
  private Map<String, Object> variables;
  private Integer priority;

  @Builder
  public ProcessInstance(Identifier id,
                         Code procReleaseKey,
                         Code procReleaseId,
                         ProcessNumber number,
                         Code engineProcessNumber,
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
                         String name,
                         String progress,
                         Map<String, Object> variables,
                         Integer priority
                         ) {
    this.id = id == null ? Identifier.generate() : id;
    this.procReleaseKey = Objects.requireNonNull(procReleaseKey, "Process release key cannot be null");
    this.procReleaseId = Objects.requireNonNull(procReleaseId, "Process release id cannot be null");
    this.number = number;
    this.engineProcessNumber = engineProcessNumber;
    this.businessKey = businessKey == null ? Code.create(generateBusinessKey()) : businessKey;
    //this.applicationBase = Objects.requireNonNull(applicationBase, "Application base cannot be null");
    this.applicationBase = applicationBase;
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
    this.name = name;
    this.progress = progress;
    this.variables = variables == null ? new HashMap<>() : variables;
    this.priority = priority == null ? DEFAULT_PRIORITY : priority;
  }

  public void start(ProcessNumber number, Code engineProcessNumber, String version, String processName, String startedBy){
    if(this.status != ProcessInstanceStatus.CREATED && this.status != ProcessInstanceStatus.SUSPENDED){
      throw new IllegalStateException("The status of the process instance must be CREATED or SUSPENDED");
    }
    if(startedBy == null || startedBy.isBlank()){
      throw new IllegalStateException("The started by (user) of the process instance cannot be null or blank");
    }
    if(number == null){
      throw new IllegalStateException("The started by (user) of the process instance cannot be null or blank");
    }
    this.number = Objects.requireNonNull(number, "Process number cannot be null");
    this.status = ProcessInstanceStatus.RUNNING;
    this.startedAt = LocalDateTime.now();
    this.engineProcessNumber = engineProcessNumber;
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

  public void suspend(){
    this.status = ProcessInstanceStatus.SUSPENDED;
  }

  public static String generateBusinessKey(){
    return UUID.randomUUID().toString();
  }

  public void setProgress(int totalTasks, int completedTasks) {
    this.progress = String.format("%d/%d", completedTasks, totalTasks);
  }

  public void addVariables(Map<String,Object> variables) {
    this.variables.putAll(variables);
  }

}

package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class AreaProcess {

  private final Identifier id;
  private Code processKey;
  private Code releaseId;
  private Identifier areaId;
  private Status status;
  private String version;

  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime removedAt;
  private String removedBy;

  @Builder
  public AreaProcess(Identifier id,
                     Code processKey,
                     Code releaseId,
                     Identifier areaId,
                     Status status,
                     String version,
                     LocalDateTime createdAt,
                     String createdBy,
                     LocalDateTime removedAt,
                     String removedBy) {
    this.id = id == null ? Identifier.generate() : id;
    this.processKey = Objects.requireNonNull(processKey, "The process key cannot be null");
    this.releaseId = Objects.requireNonNull(releaseId, "The process release id cannot be null");
    this.areaId = areaId;
    this.status = status == null ? Status.ACTIVE : status;
    this.version = version;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.createdBy = createdBy;
    this.removedAt = removedAt;
    this.removedBy = removedBy;
  }

  public void bindToArea(Area area) {
    if(area == null) {
      throw new IllegalArgumentException("The area must not be null");
    }
    this.areaId = area.getId();
  }

  public void unbindFromArea(Area area) {
    if(area == null) {
      throw new IllegalArgumentException("The area must not be null");
    }
    this.areaId = null;
    this.status = Status.INACTIVE;
  }

}

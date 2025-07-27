package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Area {

  private final Identifier id;
  private Code code;
  private Name name;
  private Code applicationBase;
  private Identifier areaId;
  private Status status;

  private final List<AreaProcess> process;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;

  @Builder
  public Area(Identifier id,
              Code code,
              Name name,
              Code applicationBase,
              Identifier areaId,
              Status status,
              List<AreaProcess> projects,
              LocalDateTime createdAt,
              LocalDateTime updatedAt,
              String createdBy,
              String updatedBy) {
    this.id = id == null ? Identifier.generate() : id;
    this.code = Objects.requireNonNull(code, "The code of the area must not be null");
    this.name = name;
    this.applicationBase = Objects.requireNonNull(applicationBase, "The application code must not be null");
    ;
    this.areaId = areaId;
    this.status = status == null ? Status.ACTIVE : status;
    this.process = projects == null ? new ArrayList<>() : projects;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }

  public void add(AreaProcess processDefinition) {
    if (processDefinition == null) {
      throw new IllegalArgumentException("The process definition must not be null");
    }
    process.add(processDefinition);
    processDefinition.bindToArea(this);
  }

  public void remove(AreaProcess processDefinition) {
    boolean result = process.removeIf(obj -> obj.getId().equals(processDefinition.getId()));
    if (result) {
      processDefinition.unbindFromArea(this);
    }
  }

  public void delete() {
    this.status = Status.INACTIVE;
  }

  public void update(Area newArea) {
    this.code = newArea.getCode();
    this.name = newArea.getName();
    this.applicationBase = newArea.getApplicationBase();
    if(newArea.getAreaId() != null) {
      this.areaId = newArea.getAreaId();
    }
  }

}

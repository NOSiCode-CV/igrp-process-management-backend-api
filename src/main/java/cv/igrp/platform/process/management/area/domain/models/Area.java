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
import java.util.Optional;

@Getter
public class Area {

  private final Identifier id;
  private Code code;
  private Name name;
  private Code applicationBase;
  private String description;
  private Identifier areaId;
  private Status status;

  private final List<AreaProcess> process;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;

  private String color;

  @Builder
  public Area(Identifier id,
              Code code,
              Name name,
              Code applicationBase,
              String description,
              Identifier areaId,
              Status status,
              List<AreaProcess> process,
              LocalDateTime createdAt,
              LocalDateTime updatedAt,
              String createdBy,
              String updatedBy,
              String color
  ) {
    this.id = id == null ? Identifier.generate() : id;
    this.code = Objects.requireNonNull(code, "The code of the area must not be null");
    this.name = name;
    this.applicationBase = Objects.requireNonNull(applicationBase, "The application code must not be null");
    this.description = description;
    this.areaId = areaId;
    this.status = status == null ? Status.ACTIVE : status;
    this.process = process == null ? new ArrayList<>() : process;
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
    this.color = color;
  }

  public void add(AreaProcess processDefinition) {
    if (processDefinition == null) {
      throw new IllegalArgumentException("The process definition must not be null");
    }
    process.add(processDefinition);
    processDefinition.bindToArea(this);
  }

  public Optional<AreaProcess> getProcess(Code releaseId) {
    return process.stream().filter(processDefinition -> processDefinition.getReleaseId().equals(releaseId))
        .findFirst();
  }

  public void remove(AreaProcess processDefinition) {
    boolean result = process.removeIf(areaProcess -> areaProcess.getId().equals(processDefinition.getId()));
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
    this.areaId = newArea.getAreaId() != null ? newArea.getAreaId() : this.areaId;
    this.description = newArea.getDescription() != null ? newArea.getDescription() : this.description;
    this.color = newArea.getColor() != null ? newArea.getColor() : this.color;
  }

}

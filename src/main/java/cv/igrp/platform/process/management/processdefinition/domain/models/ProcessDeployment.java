package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class ProcessDeployment {

  private Identifier id;
  private Code key;
  private Name name;
  private String description;
  private Name resourceName;
  private BpmnXml bpmnXml;
  private String version ;
  private String bpmnUrl ;
  private String bpmnSourceType;
  private boolean deployed ;
  private String deploymentId ;
  private LocalDateTime deployedAt ;

  private Code procReleaseKey;
  private Code procReleaseId;
  private Code applicationBase;

  @Builder
  public ProcessDeployment(Identifier id,
                           Code key,
                           Name name,
                           String description,
                           Name resourceName,
                           BpmnXml bpmnXml,
                           String version,
                           String bpmnUrl,
                           String bpmnSourceType,
                           boolean deployed,
                           String deploymentId,
                           LocalDateTime deployedAt,
                           Code procReleaseKey,
                           Code procReleaseId,
                           Code applicationBase) {
    this.id = id == null ? Identifier.generate() : id;
    this.key = Objects.requireNonNull(key, "Key cannot be null");
    this.name = name;
    this.description = description;
    this.resourceName = resourceName;
    this.bpmnXml = bpmnXml;
    this.version = version;
    this.bpmnUrl = bpmnUrl;
    this.bpmnSourceType = bpmnSourceType;
    this.deployed = deployed;
    this.deploymentId = deploymentId;
    this.deployedAt = deployedAt;
    this.procReleaseKey = procReleaseKey;
    this.procReleaseId = procReleaseId;
    this.applicationBase = Objects.requireNonNull(applicationBase, "Application Code cannot be null");;
  }

  public void deploy() {
    if(this.bpmnXml == null) {
      throw new IllegalStateException("BPMN xml cannot be null");
    }
    if(this.resourceName == null) {
      throw new IllegalStateException("ResourceName cannot be null");
    }
    if(this.applicationBase == null) {
      throw new IllegalStateException("Application Code cannot be null");
    }
    this.deployed = true;
    this.deployedAt = LocalDateTime.now();
  }

}

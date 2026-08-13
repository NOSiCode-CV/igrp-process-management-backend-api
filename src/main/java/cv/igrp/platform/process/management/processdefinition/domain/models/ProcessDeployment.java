package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class ProcessDeployment {

  private String id;
  private Code key;
  private Name name;
  private String description;
  private ResourceName resourceName;
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

  private Set<String> candidateGroups;

  @Builder
  public ProcessDeployment(String id,
                           Code key,
                           Name name,
                           String description,
                           ResourceName resourceName,
                           BpmnXml bpmnXml,
                           String version,
                           String bpmnUrl,
                           String bpmnSourceType,
                           boolean deployed,
                           String deploymentId,
                           LocalDateTime deployedAt,
                           Code procReleaseKey,
                           Code procReleaseId,
                           Code applicationBase,
                           Set<String> candidateGroups
  ) {
    this.id = id;
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
    this.candidateGroups = candidateGroups == null ? new HashSet<>() : candidateGroups;
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

  public void addCandidateGroups(String group) {
    this.candidateGroups.add(group);
  }

}

package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.util.*;


@Getter
public class ProcessPackage {

  private final Code processId;
  private final Code processKey;
  private final Name processName;
  private final String processVersion;
  private String processDescription;
  private BpmnXml bpmnXml;
  private Code applicationBase;
  private List<ProcessArtifact> artifacts;
  private ProcessSequence sequence;
  private Set<String> candidateGroups;

  @Builder
  public ProcessPackage(Code processId,
                        Code processKey,
                        Name processName,
                        String processVersion,
                        String processDescription,
                        BpmnXml bpmnXml,
                        Code applicationBase,
                        List<ProcessArtifact> artifacts,
                        ProcessSequence sequence,
                        Set<String> candidateGroups) {
    this.processId = Objects.requireNonNull(processId, "Process Id cannot be null!");
    this.processKey = Objects.requireNonNull(processKey, "Process Key cannot be null!");
    this.processName = Objects.requireNonNull(processName, "Process Name cannot be null!");
    this.processVersion = processVersion;
    this.processDescription = processDescription;
    this.bpmnXml = bpmnXml;
    this.applicationBase = applicationBase;
    this.artifacts = artifacts == null ? new ArrayList<>() : artifacts;
    this.sequence = sequence;
    this.candidateGroups = candidateGroups == null ? new HashSet<>() : candidateGroups;
  }

  public void addArtifact(ProcessArtifact artifact) {
    this.artifacts.add(artifact);
  }

  public void addGroup(String group) {
    this.candidateGroups.add(group);
  }

  public void addSequence(ProcessSequence sequence) {
    this.sequence = sequence;
  }

}

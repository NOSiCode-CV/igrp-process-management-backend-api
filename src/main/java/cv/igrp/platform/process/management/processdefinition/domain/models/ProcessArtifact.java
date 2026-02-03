package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ProcessArtifact {

  private final Identifier id;
  private final Name name;
  private final Code key;
  private Code formKey;
  private final Code processDefinitionId;
  private List<String> candidateGroups;

  @Builder
  public ProcessArtifact(Identifier id,
                         Name name,
                         Code key,
                         Code formKey,
                         Code processDefinitionId,
                         List<String> candidateGroups
  ) {
    this.id = id ==  null ? Identifier.generate() : id;
    this.name = Objects.requireNonNull(name, "The Name of the task cannot be null!");
    this.key = Objects.requireNonNull(key, "Task Key Id cannot be null!");
    this.formKey = Objects.requireNonNull(formKey, "Form Key Id cannot be null!");
    this.processDefinitionId = Objects.requireNonNull(processDefinitionId, "ProcessDefinition Id cannot be null!");
    this.candidateGroups = candidateGroups == null ? new ArrayList<>() : candidateGroups;
  }

  public void update(ProcessArtifact processArtifact) {
    this.formKey = processArtifact.formKey != null ? processArtifact.formKey : this.formKey;
    this.candidateGroups = processArtifact.candidateGroups != null && !processArtifact.candidateGroups.isEmpty() ? processArtifact.candidateGroups : this.candidateGroups;
  }

}

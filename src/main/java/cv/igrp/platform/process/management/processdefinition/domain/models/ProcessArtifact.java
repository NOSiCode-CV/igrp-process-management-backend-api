package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ProcessArtifact {

  private final Identifier id;
  private final String name;
  private final Code key;
  private final Code formKey;
  private final Code processDefinitionId;

  @Builder
  public ProcessArtifact(Identifier id,
                         String name,
                         Code key,
                         Code formKey,
                         Code processDefinitionId) {
    this.id = id ==  null ? Identifier.generate() : id;
    this.name = Objects.requireNonNull(name, "The Name of the task cannot be null!");
    this.key = Objects.requireNonNull(key, "Task Key Id cannot be null!");
    this.formKey = Objects.requireNonNull(formKey, "Form Key Id cannot be null!");
    this.processDefinitionId = Objects.requireNonNull(processDefinitionId, "ProcessDefinition Id cannot be null!");
  }

}

package cv.igrp.platform.process.management.shared.domain.models;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ArtifactContext {

  private final Map<String, ProcessArtifact> artifactsByTaskKey;
  private final Map<String, Code> formKeysByTaskKey;

  private ArtifactContext(List<ProcessArtifact> artifacts) {

    this.artifactsByTaskKey = artifacts.stream()
        .collect(Collectors.toUnmodifiableMap(
            processArtifact -> processArtifact.getKey().getValue(),
            Function.identity()
        ));

    this.formKeysByTaskKey = artifacts.stream()
        .filter(a -> a.getFormKey() != null)
        .collect(Collectors.toUnmodifiableMap(
            processArtifact -> processArtifact.getKey().getValue(),
            a -> Code.create(a.getFormKey())
        ));
  }

  public static ArtifactContext from(List<ProcessArtifact> artifacts) {
    Objects.requireNonNull(artifacts, "Artifacts list cannot be null");
    return new ArtifactContext(artifacts);
  }

  public Optional<ProcessArtifact> findArtifact(String taskKey) {
    return Optional.ofNullable(artifactsByTaskKey.get(taskKey));
  }

  public Optional<Code> findFormKey(String taskKey) {
    return Optional.ofNullable(formKeysByTaskKey.get(taskKey));
  }

  public boolean hasArtifact(String taskKey) {
    return artifactsByTaskKey.containsKey(taskKey);
  }

  public Set<String> taskKeys() {
    return artifactsByTaskKey.keySet();
  }

}

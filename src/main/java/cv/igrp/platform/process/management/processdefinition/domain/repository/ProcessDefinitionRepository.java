package cv.igrp.platform.process.management.processdefinition.domain.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;

import java.util.List;
import java.util.Optional;

public interface ProcessDefinitionRepository {

  ProcessArtifact saveArtifact(ProcessArtifact area);

  List<ProcessArtifact> findAllArtifacts(Code processDefinitionId);

  Optional<ProcessArtifact> findArtifactById(Identifier id);

  void deleteArtifact(ProcessArtifact area);

  Optional<ProcessArtifact> findArtifactByProcessDefinitionIdAndKey(Code processDefinitionId, Code key);

}

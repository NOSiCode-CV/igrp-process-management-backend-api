package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessArtifactMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;

@Component
public class GetDeployedArtifactsByProcessDefinitionIdQueryHandler implements QueryHandler<GetDeployedArtifactsByProcessDefinitionIdQuery, ResponseEntity<List<ProcessArtifactDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetDeployedArtifactsByProcessDefinitionIdQueryHandler.class);

  private final ProcessDeploymentService processDeploymentService;
  private final ProcessArtifactMapper mapper;

  public GetDeployedArtifactsByProcessDefinitionIdQueryHandler(ProcessDeploymentService processDeploymentService,
                                                               ProcessArtifactMapper mapper) {
    this.processDeploymentService = processDeploymentService;
    this.mapper = mapper;
  }


  @IgrpQueryHandler
  public ResponseEntity<List<ProcessArtifactDTO>> handle(GetDeployedArtifactsByProcessDefinitionIdQuery query) {
    List<ProcessArtifact> processArtifacts = processDeploymentService
        .getDeployedArtifactsByProcessDefinitionId(Code.create(query.getId()));
    return ResponseEntity.ok(mapper.toDTO(processArtifacts));
  }

}

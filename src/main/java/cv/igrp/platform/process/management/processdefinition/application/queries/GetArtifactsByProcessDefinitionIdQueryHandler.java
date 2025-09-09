package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
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
public class GetArtifactsByProcessDefinitionIdQueryHandler implements QueryHandler<GetArtifactsByProcessDefinitionIdQuery, ResponseEntity<List<ProcessArtifactDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetArtifactsByProcessDefinitionIdQueryHandler.class);

  private final ProcessArtifactService processArtifactService;
  private final ProcessArtifactMapper mapper;

  public GetArtifactsByProcessDefinitionIdQueryHandler(ProcessArtifactService processArtifactService,
                                                       ProcessArtifactMapper mapper) {
    this.processArtifactService = processArtifactService;
    this.mapper = mapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<List<ProcessArtifactDTO>> handle(GetArtifactsByProcessDefinitionIdQuery query) {
    return ResponseEntity.ok(mapper.toDTO(
        processArtifactService.getArtifactsByProcessDefinitionId(Code.create(query.getId()))));
  }

}

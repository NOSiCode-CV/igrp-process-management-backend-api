package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessPackageDTO;

@Component
public class ExportProcessDefinitionQueryHandler implements QueryHandler<ExportProcessDefinitionQuery, ResponseEntity<ProcessPackageDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExportProcessDefinitionQueryHandler.class);

  private final ProcessDeploymentService processDeploymentService;
  private final ProcessPackageMapper mapper;

  public ExportProcessDefinitionQueryHandler(ProcessDeploymentService processDeploymentService,
                                             ProcessPackageMapper mapper) {
    this.processDeploymentService = processDeploymentService;
    this.mapper = mapper;
  }


  @IgrpQueryHandler
  public ResponseEntity<ProcessPackageDTO> handle(ExportProcessDefinitionQuery query) {
    LOGGER.info("Exporting process definition [{}]", query.getId());
    return ResponseEntity.ok(mapper.toDTO(processDeploymentService.exportProcessDefinition(query.getId())));
  }

}

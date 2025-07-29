package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;

@Component
public class ListDeploymentsQueryHandler implements QueryHandler<ListDeploymentsQuery, ResponseEntity<ProcessDeploymentListPageDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListDeploymentsQueryHandler.class);

  private final ProcessDeploymentService processDeploymentService;
  private final ProcessDeploymentMapper mapper;

  public ListDeploymentsQueryHandler(ProcessDeploymentService processDeploymentService,
                                     ProcessDeploymentMapper mapper) {
    this.processDeploymentService = processDeploymentService;
    this.mapper = mapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<ProcessDeploymentListPageDTO> handle(ListDeploymentsQuery query) {
    PageableLista<ProcessDeployment> deployments = processDeploymentService.getAllDeployments(
        query.getApplicationBase() != null ? Code.create(query.getApplicationBase()) : null
    );
    return ResponseEntity.ok(mapper.toDTO(deployments));
  }

}

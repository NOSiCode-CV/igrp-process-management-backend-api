package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
    var filter = ProcessDeploymentFilter.builder()
        .processName(query.getProcessName()!=null ? query.getProcessName() : null)
        .applicationBase(query.getApplicationBase() != null ? Code.create(query.getApplicationBase()) : null)
        .pageNumber(query.getPage())
        .pageSize(query.getSize())
        .build();
    PageableLista<ProcessDeployment> deployments = processDeploymentService.getAllDeployments(filter);
    return ResponseEntity.ok(mapper.toDTO(deployments));
  }

}

package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionListPageDTO;

@Component
public class ListProcessDefinitionsQueryHandler implements QueryHandler<ListProcessDefinitionsQuery, ResponseEntity<ProcessDefinitionListPageDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProcessDefinitionsQueryHandler.class);

  private final AreaService areaService;
  private final AreaProcessMapper areaProcessMapper;

  public ListProcessDefinitionsQueryHandler(AreaService areaService, AreaProcessMapper areaProcessMapper) {
    this.areaService = areaService;
    this.areaProcessMapper = areaProcessMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<ProcessDefinitionListPageDTO> handle(ListProcessDefinitionsQuery query) {
    AreaProcessFilter filter = AreaProcessFilter.builder()
        .processKey(query.getProcessKey() != null ? Code.create(query.getProcessKey()) : null)
        .releaseId(query.getReleaseId() != null ? Code.create(query.getReleaseId()) : null)
        .areaId(query.getAreaId() != null ? Identifier.create(query.getAreaId()) : null)
        .status(query.getStatus() != null ? Status.valueOf(query.getStatus()) : null)
        .page(query.getPage())
        .size(query.getSize())
        .build();
    PageableLista<AreaProcess> areaProcessPageableLista = areaService.getAllAreaProcess(filter);
    return ResponseEntity.ok(areaProcessMapper.toDTO(areaProcessPageableLista));
  }

}

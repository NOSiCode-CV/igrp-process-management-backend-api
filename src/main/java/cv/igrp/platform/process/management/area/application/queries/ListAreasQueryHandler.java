package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaFilter;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.area.application.dto.AreaListaPageDTO;

@Component
public class ListAreasQueryHandler implements QueryHandler<ListAreasQuery, ResponseEntity<AreaListaPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListAreasQueryHandler.class);

  private final AreaService areaService;
  private final AreaMapper areaMapper;

  public ListAreasQueryHandler(AreaService areaService, AreaMapper areaMapper) {
    this.areaService = areaService;
    this.areaMapper = areaMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<AreaListaPageDTO> handle(ListAreasQuery query) {
    AreaFilter filter = AreaFilter.builder()
        .name(query.getName() != null ? Name.create(query.getName()) : null)
        .applicationBase(query.getApplicationBase() != null ? Code.create(query.getApplicationBase()) : null)
        .code(query.getCode() != null ? Code.create(query.getCode()) : null)
        .parentId(query.getParentId() != null ? Identifier.create(query.getParentId()) : null)
        .status(query.getStatus() != null ? Status.valueOf(query.getStatus()) : null)
        .size(query.getSize())
        .page(query.getPage())
        .build();
    PageableLista<Area> areaPageableLista =  areaService.getAllAreas(filter);
    return ResponseEntity.ok(areaMapper.toDTO(areaPageableLista));
  }

}

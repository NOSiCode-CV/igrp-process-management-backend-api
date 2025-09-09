package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;

import java.util.UUID;

@Component
public class GetAreaByIdQueryHandler implements QueryHandler<GetAreaByIdQuery, ResponseEntity<AreaDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetAreaByIdQueryHandler.class);

  private final AreaService areaService;
  private final AreaMapper areaMapper;

  public GetAreaByIdQueryHandler(AreaService areaService,
                                 AreaMapper areaMapper) {
    this.areaService = areaService;
    this.areaMapper = areaMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<AreaDTO> handle(GetAreaByIdQuery query) {
    Area area = areaService.getAreaById(UUID.fromString(query.getId()));
    return ResponseEntity.ok(areaMapper.toDTO(area));
  }

}

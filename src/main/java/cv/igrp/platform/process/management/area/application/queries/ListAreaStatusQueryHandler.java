package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.platform.process.management.shared.domain.service.ConfigParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;

@Component
public class ListAreaStatusQueryHandler implements QueryHandler<ListAreaStatusQuery, ResponseEntity<List<ConfigParameterDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListAreaStatusQueryHandler.class);

  private final ConfigParameterService configParameterService;

  public ListAreaStatusQueryHandler(ConfigParameterService configParameterService) {
    this.configParameterService = configParameterService;
  }

  @IgrpQueryHandler
  public ResponseEntity<List<ConfigParameterDTO>> handle(ListAreaStatusQuery query) {
    List<ConfigParameterDTO> result = configParameterService.getAreaProcessStatus().stream()
        .flatMap(map -> map.entrySet().stream())
        .map(entry -> new ConfigParameterDTO(entry.getValue(), entry.getKey().toString()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }

}

package cv.igrp.platform.process.management.processruntime.application.queries;

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
public class ListProcessInstanceStatusQueryHandler implements QueryHandler<ListProcessInstanceStatusQuery, ResponseEntity<List<ConfigParameterDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProcessInstanceStatusQueryHandler.class);

  private final ConfigParameterService configParameterService;

  public ListProcessInstanceStatusQueryHandler(ConfigParameterService configParameterService) {
    this.configParameterService = configParameterService;
  }

  @IgrpQueryHandler
  public ResponseEntity<List<ConfigParameterDTO>> handle(ListProcessInstanceStatusQuery query) {
    List<ConfigParameterDTO> result = configParameterService.getProcessInstanceStatus().stream()
        .flatMap(map -> map.entrySet().stream())
        .map(entry -> new ConfigParameterDTO(entry.getValue(), entry.getKey().toString()))
        .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }

}

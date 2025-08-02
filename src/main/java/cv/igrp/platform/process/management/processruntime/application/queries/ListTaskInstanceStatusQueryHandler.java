package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;
import cv.igrp.platform.process.management.shared.domain.service.ConfigParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListTaskInstanceStatusQueryHandler implements QueryHandler<ListTaskInstanceStatusQuery, ResponseEntity<List<ConfigParameterDTO>>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListTaskInstanceStatusQueryHandler.class);

  private final ConfigParameterService configParameterService;

  public ListTaskInstanceStatusQueryHandler(ConfigParameterService configParameterService) {
    this.configParameterService = configParameterService;
  }

   @IgrpQueryHandler
  public ResponseEntity<List<ConfigParameterDTO>> handle(ListTaskInstanceStatusQuery query) {
     List<ConfigParameterDTO> result = configParameterService.getTaskInstanceStatus().stream()
         .flatMap(map -> map.entrySet().stream())
         .map(entry -> new ConfigParameterDTO(entry.getValue(), entry.getKey().toString()))
         .collect(Collectors.toList());
     return ResponseEntity.ok(result);
  }

}

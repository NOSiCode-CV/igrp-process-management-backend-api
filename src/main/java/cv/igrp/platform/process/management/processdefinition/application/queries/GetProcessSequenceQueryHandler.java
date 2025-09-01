package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessSequenceService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetProcessSequenceQueryHandler implements QueryHandler<GetProcessSequenceQuery, ResponseEntity<ProcessSequenceDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetProcessSequenceQueryHandler.class);

  private final ProcessSequenceService processSequenceService;
  private final ProcessSequenceMapper processSequenceMapper;
  private final UserContext userContext;

  public GetProcessSequenceQueryHandler(ProcessSequenceService processSequenceService,
                                            ProcessSequenceMapper processSequenceMapper,
                                            UserContext userContext) {
    this.processSequenceService = processSequenceService;
    this.processSequenceMapper = processSequenceMapper;
    this.userContext = userContext;
  }

  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<ProcessSequenceDTO> handle(GetProcessSequenceQuery query) {

     final var currentUser = userContext.getCurrentUser();

     LOGGER.debug("User [{}] requested process sequence by id [{}]", currentUser.getValue(), query.getId());

     final var taskInstance = processSequenceService.getSequenceByProcessDefinitionId(Code.create(query.getId()));

     LOGGER.debug("Retrieved process sequence [{}] for user [{}]", taskInstance.getId(), currentUser.getValue());

     return ResponseEntity.ok(processSequenceMapper.toDTO(taskInstance));

  }

}

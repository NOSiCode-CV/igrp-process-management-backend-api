package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessSequenceService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateProcessSequenceCommandHandler implements CommandHandler<CreateProcessSequenceCommand, ResponseEntity<ProcessSequenceDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(CreateProcessSequenceCommandHandler.class);

  private final ProcessSequenceService processSequenceService;
  private final ProcessSequenceMapper processSequenceMapper;
  private final UserContext userContext;

   public CreateProcessSequenceCommandHandler(ProcessSequenceService processSequenceService, ProcessSequenceMapper processSequenceMapper, UserContext userContext) {

     this.processSequenceService = processSequenceService;
     this.processSequenceMapper = processSequenceMapper;
     this.userContext = userContext;
   }

   @IgrpCommandHandler
   public ResponseEntity<ProcessSequenceDTO> handle(CreateProcessSequenceCommand command) {
     final var currentUser = userContext.getCurrentUser();

     LOGGER.info("User [{}] started creating sequence for process definition key [{}]", currentUser.getValue(), command.getKey());

     var sequenceReq = ProcessSequence.builder()
         .processDefinitionKey(Code.create(command.getKey()))
         .name(Name.create(command.getSequencerequestdto().getName()))
         .prefix(Code.create(command.getSequencerequestdto().getPrefix()))
         .dateFormat(command.getSequencerequestdto().getDateFormat())
         .padding(command.getSequencerequestdto().getPadding())
         .checkDigitSize(command.getSequencerequestdto().getCheckDigitSize())
         .numberIncrement(command.getSequencerequestdto().getNumberIncrement())
         .build();

     var sequenceResp = processSequenceService.save(sequenceReq);

     LOGGER.info("User [{}] finished creating sequence for process definition key [{}]", currentUser.getValue(), command.getKey());

     return ResponseEntity.ok(processSequenceMapper.toDTO(sequenceResp));
   }

}

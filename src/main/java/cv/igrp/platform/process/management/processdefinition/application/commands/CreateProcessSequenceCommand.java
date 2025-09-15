package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import cv.igrp.platform.process.management.processdefinition.application.dto.SequenceRequestDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProcessSequenceCommand implements Command {


  private SequenceRequestDTO sequencerequestdto;
  @NotBlank(message = "The field <processDefinitionKey> is required")
  private String processDefinitionKey;

}

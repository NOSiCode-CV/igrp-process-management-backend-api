package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnClaimTaskCommand implements Command {

  @NotBlank(message = "The field <note> is required")
  private String note;
  @NotBlank(message = "The field <id> is required")
  private String id;

}
package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteArtifactCommand implements Command {

  @NotBlank(message = "The field <id> is required")
  private String id;

}
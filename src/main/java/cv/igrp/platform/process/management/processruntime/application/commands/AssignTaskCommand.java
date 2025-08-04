package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskCommand implements Command {

  @NotBlank(message = "The field <user_perform> is required.")
  private String user_perform;
  @NotBlank(message = "The field <user_assigned> is required.")
  private String user_assigned;
  @NotBlank(message = "The field <note> is required.")
  private String note;
  @NotBlank(message = "The field <id> is required.")
  private String id;

}

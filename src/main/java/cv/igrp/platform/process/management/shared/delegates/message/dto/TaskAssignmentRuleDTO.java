package cv.igrp.platform.process.management.shared.delegates.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentRuleDTO {

  private String taskKey;
  private String assignee;
  private String candidateUsers;
  private String assignmentMode;
  private Integer priority;
}

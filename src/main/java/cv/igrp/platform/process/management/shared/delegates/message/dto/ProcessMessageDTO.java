package cv.igrp.platform.process.management.shared.delegates.message.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ProcessMessageDTO {

  private String businessKey;
  private String processKey;
  private String processName;
  private long timestamp;
  private Map<String, Object> variables;
  private List<TaskMessageDTO> tasks;

  public ProcessMessageDTO() {
    this.tasks = new ArrayList<>();
    this.variables = new HashMap<>();
  }

  @Getter
  @Setter
  public static class TaskMessageDTO {

    private String taskKey;
    private String taskName;
    private Map<String, Object> variables;

    public TaskMessageDTO() {
      this.variables = new HashMap<>();
    }

  }

}

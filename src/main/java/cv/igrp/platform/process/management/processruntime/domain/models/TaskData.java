package cv.igrp.platform.process.management.processruntime.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class TaskData {
  private final Map<String,Object> variables;
  private final Map<String,Object> forms;

  @Builder
  public TaskData(Map<String,Object> variables, Map<String,Object> forms){
    this.variables = variables!=null ? variables : Map.of();
    this.forms = forms!=null ? forms : Map.of();
  }

}

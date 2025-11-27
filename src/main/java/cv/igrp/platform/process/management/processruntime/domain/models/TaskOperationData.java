package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;

@Getter
@ToString
public class TaskOperationData {
  private final Identifier id;
  private final Code currentUser;
  private final Code targetUser;
  private final Integer priority;
  private final String note;
  private final Map<String,Object> variables;
  private final Map<String,Object> forms;
  private final Code candidateGroup;

  @Builder
  public TaskOperationData(String id,
                           Code currentUser,
                           Integer priority,
                           String targetUser,
                           String note,
                           Map<String,Object> variables,
                           Map<String,Object> forms,
                           String candidateGroup) {
    this.id = Identifier.create(id);
    this.currentUser = Objects.requireNonNull(currentUser,"Current User can't be null!");
    this.targetUser = targetUser!=null ? Code.create(targetUser) : null;
    this.priority = (priority!=null && priority>0) ? priority: null;
    this.note = (note!=null && !note.isBlank()) ? note.trim() : note;
    this.variables = (variables!=null) ? variables : Map.of();
    this.forms = (forms!=null) ? forms : Map.of();
    this.candidateGroup = candidateGroup != null ? Code.create(candidateGroup) : null;;
  }

  public void validateSubmitedVariablesAndForms() {
    variables.forEach((k,v)->{
      if(k==null || k.isBlank() || v==null)
        throw IgrpResponseStatusException.badRequest("Invalid param for variable [name:"+ k +"] [value:"+ v +"]");
    });
    forms.forEach((k,v)->{
      if(k==null || k.isBlank() || v==null)
        throw IgrpResponseStatusException.badRequest("Invalid param for form [name:"+ k +"] [value:"+ v +"]");
    });
  }

}

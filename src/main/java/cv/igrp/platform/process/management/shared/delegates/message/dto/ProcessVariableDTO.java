package cv.igrp.platform.process.management.shared.delegates.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVariableDTO {

  private String name;
  private Object value;

}

package cv.igrp.platform.process.management.shared.delegates.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessEventDTO {

  private String messageName; // optional
  private String businessKey;
  private Map<String, Object> variables; // optional

}

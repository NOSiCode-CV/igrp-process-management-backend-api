package cv.igrp.platform.process.management.shared.domain.service;


import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ConfigParameterService {


    public List<Map<? , String>> getProcessInstanceStatus() {
        return Arrays.stream(ProcessInstanceStatus.values())
                .map(status -> Map.of(status.getCode(), status.getDescription()))
                .collect(Collectors.toList());
    }

  public List<Map<? , String>> getTaskInstanceStatus() {
    return Arrays.stream(TaskInstanceStatus.values())
        .map(status -> Map.of(status.getCode(), status.getDescription()))
        .collect(Collectors.toList());
  }

  public List<Map<? , String>> getAreaProcessStatus() {
    return Arrays.stream(Status.values())
        .map(status -> Map.of(status.getCode(), status.getDescription()))
        .collect(Collectors.toList());
  }

}

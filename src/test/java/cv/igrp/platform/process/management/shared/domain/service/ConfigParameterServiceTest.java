package cv.igrp.platform.process.management.shared.domain.service;

import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigParameterServiceTest {

  private ConfigParameterService service;

  @BeforeEach
  void setUp() {
    service = new ConfigParameterService();
  }

  @Test
  void testGetProcessInstanceStatus() {
    List<Map<?, String>> result = service.getProcessInstanceStatus();
    assertNotNull(result);
    assertEquals(ProcessInstanceStatus.values().length, result.size());

    for (int i = 0; i < result.size(); i++) {
      Map<?, String> map = result.get(i);
      ProcessInstanceStatus status = ProcessInstanceStatus.values()[i];
      assertTrue(map.containsKey(status.getCode()));
      assertEquals(status.getDescription(), map.get(status.getCode()));
    }
  }

  @Test
  void testGetAreaProcessStatus() {
    List<Map<?, String>> result = service.getAreaProcessStatus();
    assertNotNull(result);
    assertEquals(Status.values().length, result.size());

    for (int i = 0; i < result.size(); i++) {
      Map<?, String> map = result.get(i);
      Status status = Status.values()[i];
      assertTrue(map.containsKey(status.getCode()));
      assertEquals(status.getDescription(), map.get(status.getCode()));
    }
  }

  @Test
  void testGetTaskInstanceStatus() {
    List<Map<?, String>> result = service.getTaskInstanceStatus();
    assertNotNull(result);
    assertEquals(TaskInstanceStatus.values().length, result.size());

    for (int i = 0; i < result.size(); i++) {
      Map<?, String> map = result.get(i);
      TaskInstanceStatus status = TaskInstanceStatus.values()[i];
      assertTrue(map.containsKey(status.getCode()));
      assertEquals(status.getDescription(), map.get(status.getCode()));
    }
  }

  @Test
  void testGetTaskEventType() {
    List<Map<?, String>> result = service.getTaskEventType();
    assertNotNull(result);
    assertEquals(TaskEventType.values().length, result.size());

    for (int i = 0; i < result.size(); i++) {
      Map<?, String> map = result.get(i);
      TaskEventType eventType = TaskEventType.values()[i];
      assertTrue(map.containsKey(eventType.getCode()));
      assertEquals(eventType.getDescription(), map.get(eventType.getCode()));
    }
  }

}

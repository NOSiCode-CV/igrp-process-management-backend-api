package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskInstanceTest {

  private TaskInstance task;

  @Mock
  private Principal principal;


  @BeforeEach
  void setUp() {
    when(principal.getName()).thenReturn("current-user");

    task = TaskInstance.builder()
        .id(Identifier.generate())
        .taskKey(Code.create("T1"))
        .formKey(Code.create("F1"))
        .name(Name.create("Task Test"))
        .externalId(Code.create("EXT"))
        .processInstanceId(Identifier.generate())
        .processNumber(Code.create("P123"))
        .applicationBase(Code.create("APP"))
        .processName(Code.create("PROC"))
        .taskInstanceEvents(List.of())
        .build();
  }

  @Test
  void testCreate_ShouldGenerateCreateEvent() {
    task.create();

    assertEquals(TaskInstanceStatus.CREATED, task.getStatus());
    assertNotNull(task.getStartedAt());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().get(0);
    assertEquals(TaskEventType.CREATE, event.getEventType());
    assertEquals(TaskInstanceStatus.CREATED, event.getStatus());
  }

  @Test
  void testClaim_ShouldGenerateClaimEvent() {
    task.create();
    task.claim(Code.create("current-user"),"Claiming task");

    assertEquals(TaskInstanceStatus.ASSIGNED, task.getStatus());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().get(0);
    assertEquals(TaskEventType.CLAIM, event.getEventType());
    assertEquals("Claiming task", event.getNote());
  }

  @Test
  void testAssign_ShouldThrowIfUserIsNull() {
    assertThrows(IllegalStateException.class, () -> task.assign(Code.create("current-user"), Code.create("demo@nosi.cv"),"note"));
  }

  @Test
  void testComplete_ShouldGenerateCompleteEvent() {
    task.create();
    task.complete(Code.create("current-user"));

    assertEquals(TaskInstanceStatus.COMPLETED, task.getStatus());
    assertNotNull(task.getEndedAt());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().get(0);
    assertEquals(TaskEventType.COMPLETE, event.getEventType());
  }

}

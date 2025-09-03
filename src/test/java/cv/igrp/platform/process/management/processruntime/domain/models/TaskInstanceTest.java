package cv.igrp.platform.process.management.processruntime.domain.models;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskInstanceTest {
/*
  private TaskInstance task;

  @Mock
  private Code currentUser;


  @BeforeEach
  void setUp() {

    currentUser = Code.create("demo@nosi.cv");

    task = TaskInstance.builder()
        .id(Identifier.generate())
        .taskKey(Code.create("T1"))
        .formKey(Code.create("F1"))
        .name(Name.create("Task Test"))
        .externalId(Code.create("EXT"))
        .processInstanceId(Identifier.generate())
        .processNumber(ProcessNumber.create("P123"))
        .applicationBase(Code.create("APP"))
        .processName(Code.create("PROC"))
        .taskInstanceEvents(List.of())
        .build();

    currentUser = Code.create("demo@nosi.cv");
  }

  @Test
  void testCreate_ShouldGenerateCreateEvent() {

    task.create();

    assertEquals(TaskInstanceStatus.CREATED, task.getStatus());
    assertNotNull(task.getStartedAt());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().getFirst();
    assertEquals(TaskEventType.CREATE, event.getEventType());
    assertEquals(TaskInstanceStatus.CREATED, event.getStatus());
  }

  @Test
  void testClaim_ShouldGenerateClaimEvent() {

    task.claim(currentUser,"Claiming task");

    assertEquals(TaskInstanceStatus.ASSIGNED, task.getStatus());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().getLast();
    assertEquals(TaskEventType.CLAIM, event.getEventType());
    assertEquals("Claiming task", event.getNote());
  }

//  @Test
//  void testAssign_ShouldThrowIfUserIsNull() {
//    assertThrows( IllegalStateException.class,
//        () -> task.assign(currentUser, Code.create("igrp@nosi.cv"),"Claiming task"));
//  }

  @Test
  void testComplete_ShouldGenerateCompleteEvent() {

    task.complete(currentUser);

    assertEquals(TaskInstanceStatus.COMPLETED, task.getStatus());
    assertNotNull(task.getEndedAt());
    assertEquals(1, task.getTaskInstanceEvents().size());

    TaskInstanceEvent event = task.getTaskInstanceEvents().getLast();
    assertEquals(TaskEventType.COMPLETE, event.getEventType());
  }

  @Test
  void withProperties_shouldReturnNewTaskInstanceWithUpdatedFields() {
    TaskInstance original = TaskInstance.builder()
        .id(Identifier.generate())
        .taskKey(Code.create("task-key"))
        .externalId(Code.create("ext-id"))
        .name(Name.create("My Task"))
        .formKey(Code.create("oldForm"))
        .startedAt(LocalDateTime.now())
        .build();

    Code newFormKey = Code.create("newForm");
    Code newAppBase = Code.create("APP");
    ProcessNumber newProcNumber = ProcessNumber.create("P123");
    Code newProcName = Code.create("procName");
    Code newBizKey = Code.create("bizKey");
    Identifier procInstId = Identifier.generate();
    Code startedBy = Code.create("user1");

    TaskInstance result = original.withProperties(
        newAppBase, newProcNumber, newProcName,
        newBizKey, procInstId, newFormKey, startedBy
    );

    assertEquals(newFormKey, result.getFormKey());
    assertEquals(newAppBase, result.getApplicationBase());
    assertEquals(newProcNumber, result.getProcessNumber());
    assertEquals(newProcName, result.getProcessName());
    assertEquals(newBizKey, result.getBusinessKey());
    assertEquals(procInstId, result.getProcessInstanceId());
    assertEquals(startedBy, result.getStartedBy());

    assertNotEquals(original, result);
    assertEquals("oldForm", original.getFormKey().getValue());
  }*/

}

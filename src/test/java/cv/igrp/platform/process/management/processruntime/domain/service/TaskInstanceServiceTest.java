package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessArtifactEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskInstanceServiceTest {

  @Mock
  private TaskInstanceRepository taskInstanceRepository;

  @Mock
  private TaskInstanceEventRepository taskInstanceEventRepository;

  @Mock
  private RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  @Mock
  private ProcessInstanceRepository processInstanceRepository;

  @Mock
  private ProcessArtifactEntityRepository processArtifactEntityRepository;

  @InjectMocks
  private TaskInstanceService taskInstanceService;

  private Code currentUser;

  @BeforeEach
  void setup() {
    currentUser = Code.create("demo@nosi.cv");
  }


  @Test
  void testCreateTaskInstancesByProcess() {/*
    // Arrange
    Identifier processInstanceId = Identifier.create(UUID.randomUUID());
    ProcessNumber processNumber = ProcessNumber.create("PROC-123");
    Code engineProcessNumber = Code.create("ENG-PROC-123");
    String processName = "TYPE-A";
    Code businessKey = Code.create("BK-001");
    Code applicationBase = Code.create("APP-XYZ");
    String startedBy = "igrp@nosi.cv";
    String taskKey = "task-1";
    String taskName = "Task-1";
    String formKey = "FORM-001";
    String externalId = UUID.randomUUID().toString();
    var date = LocalDateTime.now();

    //
    ProcessInstance processInstance = mock(ProcessInstance.class);
    when(processInstance.getId()).thenReturn(processInstanceId);
    when(processInstance.getEngineProcessNumber()).thenReturn(engineProcessNumber);
    when(processInstance.getNumber()).thenReturn(processNumber);
    when(processInstance.getName()).thenReturn(processName);
    when(processInstance.getBusinessKey()).thenReturn(businessKey);
    when(processInstance.getApplicationBase()).thenReturn(applicationBase);
    when(processInstance.getStartedBy()).thenReturn(startedBy);

    //
    TaskInstance task = TaskInstance.builder()
        .taskKey(Code.create(taskKey))
        .externalId(Code.create(externalId))
        .formKey(Code.create(formKey))
        .name(Name.create( (taskName!=null) ? taskName : "NOT SET"))
        .startedAt(date)
        .build();
    TaskInstance originalMock = Mockito.spy(task);

    when(runtimeProcessEngineRepository.getActiveTaskInstances(engineProcessNumber.getValue()))
        .thenReturn(List.of(originalMock));

    //
    ProcessArtifactEntity artifactEntity = new ProcessArtifactEntity();
    artifactEntity.setKey(taskKey);
    artifactEntity.setFormKey(formKey);
    ProcessArtifactEntity artifactEntityMock = Mockito.spy(artifactEntity);

    when(processArtifactEntityRepository.findAllByProcessDefinitionId(processInstanceId.getValue().toString()))
        .thenReturn(List.of(artifactEntityMock));

    //
    TaskInstance finalTask = TaskInstance.builder()
        .id(originalMock.getId())
        .taskKey(originalMock.getTaskKey())
        .externalId(originalMock.getExternalId())
        .name(originalMock.getName())
        .startedAt(date)
        .formKey(Code.create(formKey))
        .applicationBase(applicationBase)
        .processNumber(processNumber)
        .processName(Code.create(processName))
        .businessKey(businessKey)
        .processInstanceId(processInstanceId)
        .startedBy(Code.create(startedBy))
        .build();
    TaskInstance finalMockTask = Mockito.spy(finalTask);

    /*doReturn(finalMockTask).when(originalMock).withProperties(
        eq(applicationBase),
        eq(processNumber),
        eq(Code.create(processName)),
        eq(businessKey),
        eq(processInstanceId),
        any(), // aceita null ou Code
        eq(Code.create(startedBy))
    );*/
/*
    //
    doNothing().when(finalMockTask).create();

    when(finalMockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));


    // when
    taskInstanceService.createTaskInstancesByProcess(processInstance);

    // then
//    verify(originalMock).withProperties(
//        eq(applicationBase),
//        eq(processNumber),
//        eq(Code.create(processName)),
//        eq(businessKey),
//        eq(processInstanceId),
//        any(),
//        eq(Code.create(startedBy))
//    );
    verify(finalMockTask).create();
    verify(taskInstanceRepository).create(finalMockTask);
    verify(taskInstanceEventRepository).save(any(TaskInstanceEvent.class));

  }


  private TaskInstance getMockTaskInstance(UUID id,boolean withEvents){
    return TaskInstance.builder()
        .id(Identifier.create(id))
        .name(Name.create("Task1"))
        .taskKey(Code.create("task_key1"))
        .externalId(Code.create(UUID.randomUUID().toString()))
        .processNumber(ProcessNumber.create("PROC_123456"))
        .taskInstanceEvents(withEvents ? List.of(mock(TaskInstanceEvent.class)) : null)
        .build();
  }


  @Test
  void getByIdWihEvents_shouldReturnTaskInstance_whenFound() {
    UUID id = UUID.randomUUID();
    final var mockTaskInstance = getMockTaskInstance(id,true);

    when(taskInstanceRepository.findByIdWihEvents(id))
        .thenReturn(Optional.of(mockTaskInstance));

    TaskInstance result = taskInstanceService.getByIdWihEvents(id);

    assertNotNull(result);
    assertEquals(mockTaskInstance, result);
  }

  @Test
  void getByIdWihEvents_shouldThrowException_whenNotFound() {
    UUID id = UUID.randomUUID();

    when(taskInstanceRepository.findByIdWihEvents(id))
        .thenReturn(Optional.empty());

    IgrpResponseStatusException ex = assertThrows(
        IgrpResponseStatusException.class,
        () -> taskInstanceService.getByIdWihEvents(id)
    );

    assertTrue(ex.getMessage().contains("No Task Instance found with id"));
    assertEquals(HttpStatus.NOT_FOUND.value(), ex.getStatusCode().value());
  }



  @Test
  void getById_shouldReturnTaskInstance_whenFound() {
    UUID id = UUID.randomUUID();
    final var mockTaskInstance = getMockTaskInstance(id,false);

    when(taskInstanceRepository.findById(id))
        .thenReturn(Optional.of(mockTaskInstance));

    TaskInstance result = taskInstanceService.getById(id);

    assertNotNull(result);
    assertEquals(mockTaskInstance, result);
  }

  @Test
  void getById_shouldThrowNotFound_whenNotFound() {
    UUID taskId = UUID.randomUUID();

    when(taskInstanceRepository.findById(taskId)).thenReturn(Optional.empty());

    IgrpResponseStatusException ex = assertThrows(IgrpResponseStatusException.class,
        () -> taskInstanceService.getById(taskId));

    assertTrue(ex.getMessage().contains("No Task Instance found with id"));
    verify(taskInstanceRepository, times(1)).findById(taskId);
  }



  @Test
  void claimTask_shouldClaimTaskAndCallRepositories() {
    final UUID taskId = UUID.randomUUID();
    final UUID externalId = UUID.randomUUID();
    final String note = "Claim task";

    TaskInstance mockTask = mock(TaskInstance.class);
    List<TaskInstanceEvent> mockEvents = List.of(mock(TaskInstanceEvent.class));

    when(mockTask.getTaskInstanceEvents()).thenReturn(mockEvents);
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId.toString()));
    when(mockTask.getAssignedBy()).thenReturn(currentUser);

    doReturn(mockTask).when(taskInstanceService).getByIdWihEvents(taskId);

    taskInstanceService.claimTask(taskId, currentUser, note);

    verify(mockTask, times(1)).claim(currentUser,note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvents.getLast());

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .claimTask(externalId.toString(), currentUser.getValue());
  }

*/

  /*@Test
  void assignTask_shouldAssignTaskAndCallRepositories() {
    final UUID taskId = UUID.randomUUID();
    final UUID externalId = UUID.randomUUID();
    final String note = "Assigning task";
    final Code targetUser = Code.create("igrp@nosi.cv");

    TaskInstance mockTask = mock(TaskInstance.class);
    List<TaskInstanceEvent> mockEvents = List.of(mock(TaskInstanceEvent.class));

    when(mockTask.getTaskInstanceEvents()).thenReturn(mockEvents);
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId.toString()));

    doReturn(mockTask).when(taskInstanceService).getByIdWihEvents(taskId);

    taskInstanceService.assignTask(taskId, currentUser, targetUser, note);

    verify(mockTask, times(1)).assign(currentUser, targetUser, note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvents.getLast());

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .assignTask(externalId.toString(), targetUser.getValue(), note);
  }*/

/*
  @Test
  void unClaimTask_shouldUnClaimTaskAndCallRepositories() {
    final UUID taskId = UUID.randomUUID();
    final UUID externalId = UUID.randomUUID();
    final String note = "Unclaiming task";

    TaskInstance mockTask = mock(TaskInstance.class);
    List<TaskInstanceEvent> mockEvents = List.of(mock(TaskInstanceEvent.class));

    when(mockTask.getTaskInstanceEvents()).thenReturn(mockEvents);
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId.toString()));

    doReturn(mockTask).when(taskInstanceService).getByIdWihEvents(taskId);

    taskInstanceService.unClaimTask(taskId, currentUser, note);

    verify(mockTask, times(1)).unClaim(currentUser, note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvents.getLast());

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .unClaimTask(externalId.toString());
  }


  @Test
  void shouldCompleteTaskSuccessfully() {
    // Arrange
    var externalId = UUID.randomUUID().toString();
    var taskId = UUID.randomUUID();
    Map<String, Object> forms = Map.of("Name", "Maria","Age",18);
    Map<String, Object> variables = Map.of("v1", "val1");

    taskInstanceService = spy(taskInstanceService);

    // --- mocks principais ---
    TaskInstance mockTask = mock(TaskInstance.class);
    ProcessInstance mockProc = mock(ProcessInstance.class);
    ProcessInstance mockActivityProc = mock(ProcessInstance.class);

    // stub de getById
    doReturn(mockTask).when(taskInstanceService).getByIdWihEvents(taskId);

    // taskInstance stubs
    when(mockTask.getProcessInstanceId()).thenReturn(Identifier.generate());
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));
    when(mockTask.getProcessNumber()).thenReturn(new ProcessNumber("PN-1"));
    doNothing().when(mockTask).complete(any());
    when(mockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));

    // repositorio de processInstance
    when(processInstanceRepository.findById(any())).thenReturn(Optional.of(mockProc));

    // engine completeTask apenas ignora
    doNothing().when(runtimeProcessEngineRepository).completeTask(any(), any(), any());

    // retorno do engine activityProcess
    when(runtimeProcessEngineRepository.getProcessInstanceById(any()))
        .thenReturn(mockActivityProc);

    // mock activityProcess status COMPLETED
    when(mockActivityProc.getStatus()).thenReturn(ProcessInstanceStatus.COMPLETED);
    when(mockActivityProc.getEndedAt()).thenReturn(LocalDateTime.now());
    when(mockActivityProc.getEndedBy()).thenReturn(null);

    // comportamento do processInstance
    when(mockProc.getEngineProcessNumber()).thenReturn(Code.create("ENG-PROC-123"));
    when(mockProc.getBusinessKey()).thenReturn(Code.create("BK-1"));
    doNothing().when(mockProc).complete(any(), any());
    when(processInstanceRepository.save(any())).thenReturn(mockProc);


    // salvar task
    when(taskInstanceService.save(mockTask)).thenReturn(mockTask);

    // evitar efeitos em createNextTaskInstances
    doNothing().when(taskInstanceService).createNextTaskInstances(
        any(), any());

    // Act
    TaskInstance result = taskInstanceService.completeTask(taskId, currentUser, forms, variables);

    // Assert
    assertThat(result).isEqualTo(mockTask);

    verify(runtimeProcessEngineRepository).completeTask(eq(externalId), eq(forms), eq(variables));
    verify(mockTask).complete(eq(currentUser));
    verify(processInstanceRepository).save(eq(mockProc));
    verify(taskInstanceService).createNextTaskInstances(any(), any());
  }


  @Test
  void getAllTaskInstances_shouldReturnPageableList() {

    TaskInstanceFilter filter = TaskInstanceFilter.builder().page(0).size(10).build();
    PageableLista<TaskInstance> expected = PageableLista.<TaskInstance>builder()
        .pageNumber(0)
        .pageSize(10)
        .totalElements(0L)
        .totalPages(0)
        .content(List.of())
        .first(true)
        .last(true)
        .build();

    when(taskInstanceRepository.findAll(filter)).thenReturn(expected);

    PageableLista<TaskInstance> result = taskInstanceService.getAllTaskInstances(filter);

    assertEquals(expected, result);
    verify(taskInstanceRepository, times(1)).findAll(filter);

  }



  @Test
  void getTaskVariables_shouldReturnVariablesFromRuntime() {
    final UUID taskId = UUID.randomUUID();
    final var externalId = UUID.randomUUID().toString();
    final TaskInstance mockTask = mock(TaskInstance.class);

    Map<String, Object> variables = new HashMap<>(Map.of(
        "date", "25-11-2010", "name", "Maria", "age", 35));

    doReturn(mockTask).when(taskInstanceService).getById(taskId);
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));
    when(runtimeProcessEngineRepository.getTaskVariables(externalId)).thenReturn(variables);

    Map<String,Object> result = taskInstanceService.getTaskVariables(taskId);

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("Maria", result.get("name"));
    assertEquals(35, result.get("age"));

    verify(taskInstanceService, times(1)).getById(taskId);
    verify(mockTask, times(1)).getExternalId();
    verify(runtimeProcessEngineRepository, times(1)).getTaskVariables(externalId);

  }



  @Test
  void testGetGlobalTaskStatistics(){
    TaskStatistics mockStats = TaskStatistics.builder()
        .totalTaskInstances(100L)
        .totalAvailableTasks(52L)
        .totalAssignedTasks(18L)
        .totalSuspendedTasks(5L)
        .totalCompletedTasks(25L)
        .totalCanceledTasks(7L)
        .build();

    when(taskInstanceRepository.getGlobalTaskStatistics()).thenReturn(mockStats);

    TaskStatistics stats = taskInstanceService.getGlobalTaskStatistics();

    assertNotNull(stats);
    assertEquals(100L, stats.getTotalTaskInstances());
    assertEquals(52L, stats.getTotalAvailableTasks());
    assertEquals(18L, stats.getTotalAssignedTasks());
    assertEquals(5L, stats.getTotalSuspendedTasks());
    assertEquals(25L, stats.getTotalCompletedTasks());
    assertEquals(7L, stats.getTotalCanceledTasks());

    verify(taskInstanceRepository).getGlobalTaskStatistics();

  }


  @Test
  void testGetTaskStatisticsByUser(){

    TaskStatistics mockStats = TaskStatistics.builder()
        .totalTaskInstances(100L)
        .totalAvailableTasks(52L)
        .totalAssignedTasks(18L)
        .totalSuspendedTasks(5L)
        .totalCompletedTasks(25L)
        .totalCanceledTasks(7L)
        .build();

    when(taskInstanceRepository.getTaskStatisticsByUser(any(Code.class))).thenReturn(mockStats);

    TaskStatistics stats = taskInstanceService.getTaskStatisticsByUser(currentUser);

    assertNotNull(stats);
    assertEquals(100L, stats.getTotalTaskInstances());
    assertEquals(52L, stats.getTotalAvailableTasks());
    assertEquals(18L, stats.getTotalAssignedTasks());
    assertEquals(5L, stats.getTotalSuspendedTasks());
    assertEquals(25L, stats.getTotalCompletedTasks());
    assertEquals(7L, stats.getTotalCanceledTasks());

    verify(taskInstanceRepository).getTaskStatisticsByUser(currentUser);
*/
  }

}

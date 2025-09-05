package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.*;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessArtifactEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  @Spy
  @InjectMocks
  private TaskInstanceService taskInstanceService;

  private Code currentUser;

  @BeforeEach
  void setup() {
    currentUser = Code.create("demo@nosi.cv");
  }


  @Test
  void testCreateTaskInstancesByProcess() {
    // Arrange
    Identifier processInstanceId = Identifier.create(UUID.randomUUID());
    Code engineProcessNumber = Code.create("ENG-PROC-123");
    String startedBy = "igrp@nosi.cv";
    String taskKey = "task-1";
    String formKey = "FORM-001";
    String externalId = UUID.randomUUID().toString();
    var date = LocalDateTime.now();

    // Mock ProcessInstance
    ProcessInstance processInstance = mock(ProcessInstance.class);
    when(processInstance.getId()).thenReturn(processInstanceId);
    when(processInstance.getEngineProcessNumber()).thenReturn(engineProcessNumber);
    when(processInstance.getStartedBy()).thenReturn(startedBy);

    // Activiti TaskInstance
    TaskInstance task = TaskInstance.builder()
        .taskKey(Code.create(taskKey))
        .externalId(Code.create(externalId))
        .formKey(Code.create(formKey))
        .name(Name.create("Task-1"))
        .startedAt(date)
        .build();
    TaskInstance spyTask = Mockito.spy(task);

    when(runtimeProcessEngineRepository.getActiveTaskInstances(engineProcessNumber.getValue()))
        .thenReturn(List.of(spyTask));

    // Stub ProcessArtifactEntityRepository
    ProcessArtifactEntity artifact = new ProcessArtifactEntity();
    artifact.setKey(taskKey);
    artifact.setFormKey(formKey);
    when(processArtifactEntityRepository.findAllByProcessDefinitionId(processInstanceId.getValue().toString()))
        .thenReturn(List.of(artifact));

    // generic stub withProperties, just for execution
    doReturn(spyTask).when(spyTask).withProperties(any(), any(), any());
    doNothing().when(spyTask).create();
    when(spyTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));

    // when
    taskInstanceService.createTaskInstancesByProcess(processInstance);

    // then just check if arguments are correct
    verify(spyTask).withProperties(
        eq(processInstance),
        argThat(c -> (c==null || c.getValue().equals(formKey))),
        argThat(c -> c.getValue().equals(startedBy))
    );

    // verify
    verify(spyTask).create();
    verify(taskInstanceRepository).create(spyTask);
    verify(taskInstanceEventRepository).save(any(TaskInstanceEvent.class));

  }


  private TaskInstance getMockTaskInstance(Identifier id,boolean withEvents){
    return TaskInstance.builder()
        .id(id)
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
    final var mockTaskInstance = getMockTaskInstance(Identifier.create(id),true);

    when(taskInstanceRepository.findByIdWihEvents(id))
        .thenReturn(Optional.of(mockTaskInstance));

    TaskInstance result = taskInstanceService.getByIdWihEvents(Identifier.create(id));

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
        () -> taskInstanceService.getByIdWihEvents(Identifier.create(id))
    );

    assertTrue(ex.getMessage().contains("No Task Instance found with id"));
    assertEquals(HttpStatus.NOT_FOUND.value(), ex.getStatusCode().value());
  }



  @Test
  void getById_shouldReturnTaskInstance_whenFound() {
    UUID id = UUID.randomUUID();
    final var mockTaskInstance = getMockTaskInstance(Identifier.create(id),false);

    when(taskInstanceRepository.findById(id))
        .thenReturn(Optional.of(mockTaskInstance));

    TaskInstance result = taskInstanceService.getById(Identifier.create(id));

    assertNotNull(result);
    assertEquals(mockTaskInstance, result);
  }

  @Test
  void getById_shouldThrowNotFound_whenNotFound() {
    UUID taskId = UUID.randomUUID();

    when(taskInstanceRepository.findById(taskId)).thenReturn(Optional.empty());

    IgrpResponseStatusException ex = assertThrows(IgrpResponseStatusException.class,
        () -> taskInstanceService.getById(Identifier.create(taskId)));

    assertTrue(ex.getMessage().contains("No Task Instance found with id"));
    verify(taskInstanceRepository, times(1)).findById(taskId);

  }



  @Test
  void claimTask_shouldClaimTaskAndCallRepositories() {
    final UUID taskId = UUID.randomUUID();
    final String externalId = UUID.randomUUID().toString();
    TaskInstance mockTask = mock(TaskInstance.class);

    when(mockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));
    when(mockTask.getAssignedBy()).thenReturn(currentUser);

    var operation = TaskOperationData.builder()
        .id(taskId.toString())
        .currentUser(currentUser)
        .build();

    when(taskInstanceRepository.findByIdWihEvents(taskId))
        .thenReturn(Optional.of(mockTask));

    taskInstanceService.claimTask(operation);

    verify(mockTask, times(1)).claim(operation);
    verify(taskInstanceEventRepository, times(1)).save(any(TaskInstanceEvent.class));
    verify(taskInstanceRepository, times(1)).update(mockTask);
    verify(runtimeProcessEngineRepository, times(1))
        .claimTask(eq(externalId), eq(currentUser.getValue()));
  }



  @Test
  void assignTask_shouldAssignTaskAndCallRepositories() {

    final UUID taskId = UUID.randomUUID();
    final String externalId = UUID.randomUUID().toString();
    TaskInstance mockTask = mock(TaskInstance.class);
    final String note = "Assigning task";
    final String targetUser = "igrp@nosi.cv";

    when(mockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));
    when(mockTask.getAssignedBy()).thenReturn(Code.create(targetUser));

    var operation = TaskOperationData.builder()
        .id(taskId.toString())
        .currentUser(currentUser)
        .targetUser(targetUser)
        .note(note)
        .build();

    when(taskInstanceRepository.findByIdWihEvents(taskId))
        .thenReturn(Optional.of(mockTask));

    taskInstanceService.assignTask(operation);

    verify(mockTask, times(1)).assign(operation);
    verify(taskInstanceEventRepository, times(1)).save(any(TaskInstanceEvent.class));
    verify(taskInstanceRepository, times(1)).update(mockTask);
    verify(runtimeProcessEngineRepository, times(1))
        .assignTask(eq(externalId), eq(targetUser),eq(note));

  }


  @Test
  void unClaimTask_shouldUnClaimTaskAndCallRepositories() {

    final UUID taskId = UUID.randomUUID();
    final String externalId = UUID.randomUUID().toString();
    TaskInstance mockTask = mock(TaskInstance.class);

    when(mockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));

    var operation = TaskOperationData.builder()
        .id(taskId.toString())
        .currentUser(currentUser)
        .build();

    when(taskInstanceRepository.findByIdWihEvents(taskId))
        .thenReturn(Optional.of(mockTask));

    taskInstanceService.unClaimTask(operation);

    verify(mockTask, times(1)).unClaim(operation);
    verify(taskInstanceEventRepository, times(1)).save(any(TaskInstanceEvent.class));
    verify(taskInstanceRepository, times(1)).update(mockTask);
    verify(runtimeProcessEngineRepository, times(1))
        .unClaimTask(eq(externalId));

  }


  @Test
  void shouldCompleteTaskSuccessfully() {
    // Arrange
    var taskId = UUID.randomUUID();
    var externalId = UUID.randomUUID().toString();
    Map<String, Object> forms = Map.of("Name", "Maria","Age",18);
    Map<String, Object> variables = Map.of("v1", "val1");

    var mockOperation = mock(TaskOperationData.class);
    when(mockOperation.getId()).thenReturn(Identifier.create(taskId));
    when(mockOperation.getCurrentUser()).thenReturn(currentUser);
    when(mockOperation.getVariables()).thenReturn(variables);
    when(mockOperation.getForms()).thenReturn(forms);
    doNothing().when(mockOperation).validateSubmitedVariablesAndForms();

    // mocks
    TaskInstance mockTask = mock(TaskInstance.class);
    ProcessInstance mockProc = mock(ProcessInstance.class);
    ProcessInstance mockActivityProc = mock(ProcessInstance.class);

    doReturn(mockTask).when(taskInstanceService).getByIdWihEvents(Identifier.create(taskId));

    when(mockTask.getProcessInstanceId()).thenReturn(Identifier.generate());
    when(mockTask.getExternalId()).thenReturn(Code.create(externalId));
    doNothing().when(mockTask).complete(any());
    when(mockTask.getTaskInstanceEvents()).thenReturn(List.of(mock(TaskInstanceEvent.class)));

    when(processInstanceRepository.findById(any())).thenReturn(Optional.of(mockProc));

    doNothing().when(runtimeProcessEngineRepository).completeTask(any(), any(), any());

    when(runtimeProcessEngineRepository.getProcessInstanceById(any()))
        .thenReturn(mockActivityProc);

    when(mockActivityProc.getStatus()).thenReturn(ProcessInstanceStatus.COMPLETED);
    when(mockActivityProc.getEndedAt()).thenReturn(LocalDateTime.now());
    when(mockActivityProc.getEndedBy()).thenReturn(null);

    when(mockProc.getEngineProcessNumber()).thenReturn(Code.create("ENG-PROC-123"));
    doNothing().when(mockProc).complete(any(), any());
    when(processInstanceRepository.save(any())).thenReturn(mockProc);

    when(taskInstanceService.save(mockTask)).thenReturn(mockTask);
    doNothing().when(taskInstanceService).createNextTaskInstances(
        mockProc, currentUser);

    // Act
    TaskInstance result = taskInstanceService.completeTask(mockOperation);

    // Assert
    assertThat(result).isEqualTo(mockTask);
    verify(runtimeProcessEngineRepository).completeTask(externalId,forms,variables);
    verify(mockTask).complete(mockOperation);
    verify(mockOperation).validateSubmitedVariablesAndForms();
    verify(processInstanceRepository).save(mockProc);
    verify(taskInstanceService).createNextTaskInstances(mockProc, currentUser);

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
    final var taskId = Identifier.generate();
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

  }

}

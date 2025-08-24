package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessArtifactEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

  @Mock
  private Principal principal;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    when(principal.getName()).thenReturn("current-user");
  }


  @Test
  void testCreateTaskInstancesByProcess() {
    // Arrange
    Identifier processInstanceId = Identifier.create(UUID.randomUUID());
    Code processNumber = Code.create("PROC-123");
    String processName = "TYPE-A";
    Code businessKey = Code.create("BK-001");
    Code applicationBase = Code.create("APP-XYZ");
    String startedBy = "current-user";

    ProcessInstance processInstance = mock(ProcessInstance.class);
    when(processInstance.getId()).thenReturn(processInstanceId);
    when(processInstance.getNumber()).thenReturn(processNumber);
    when(processInstance.getName()).thenReturn(processName);
    when(processInstance.getBusinessKey()).thenReturn(businessKey);
    when(processInstance.getApplicationBase()).thenReturn(applicationBase);
    when(processInstance.getStartedBy()).thenReturn(startedBy);

    // Mock runtime active tasks
    TaskInstance mockTask = mock(TaskInstance.class);
    when(mockTask.withProperties(any(), any(), any(), any(), any(), any()))
        .thenReturn(mockTask);
    when(mockTask.getTaskKey()).thenReturn(Code.create("task-1"));
    when(mockTask.getTaskInstanceEvents())
        .thenReturn(new LinkedList<>(List.of(mock(TaskInstanceEvent.class))));

    when(runtimeProcessEngineRepository.getActiveTaskInstances("PROC-123"))
        .thenReturn(List.of(mockTask));

    ProcessArtifactEntity artifactEntity = new ProcessArtifactEntity();
    artifactEntity.setKey("task-1");
    artifactEntity.setFormKey("FORM-001");
    when(processArtifactEntityRepository.findAllByProcessDefinitionId(processInstanceId.getValue().toString()))
        .thenReturn(List.of(artifactEntity));

    // Act
    taskInstanceService.createTaskInstancesByProcess(processInstance);

    // Assert
    verify(mockTask, times(1)).create();
    verify(mockTask, times(1)).withProperties(
        eq(applicationBase),
        eq(Code.create(processName)),
        eq(businessKey),
        eq(processInstanceId),
        eq(Code.create("FORM-001")),
        eq(Code.create(startedBy))
    );
    verify(taskInstanceRepository, times(1)).create(mockTask);
    verify(taskInstanceEventRepository, times(1)).save(any(TaskInstanceEvent.class));
  }


  @Test
  void getById_shouldReturnTaskInstance_whenFound() {
    UUID taskId = UUID.randomUUID();
    TaskInstance mockTask = mock(TaskInstance.class);

    when(taskInstanceRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

    TaskInstance result = taskInstanceService.getByIdWihEvents(taskId);

    assertEquals(mockTask, result);
    verify(taskInstanceRepository, times(1)).findById(taskId);
  }


  @Test
  void getById_shouldThrowNotFound_whenNotFound() {
    UUID taskId = UUID.randomUUID();

    when(taskInstanceRepository.findById(taskId)).thenReturn(Optional.empty());

    IgrpResponseStatusException ex = assertThrows(IgrpResponseStatusException.class,
        () -> taskInstanceService.getByIdWihEvents(taskId));

    assertTrue(ex.getMessage().contains("No Task Instance found with id"));
    verify(taskInstanceRepository, times(1)).findById(taskId);
  }


  @Test
  void claimTask_shouldAssignTaskAndCallRepositories() {
    UUID taskId = UUID.randomUUID();
    String note = "Claim task";

    TaskInstance mockTask = mock(TaskInstance.class);
    TaskInstanceEvent mockEvent = mock(TaskInstanceEvent.class);

    when(mockTask.getTaskInstanceEvents()).thenReturn(new java.util.LinkedList<>(List.of(mockEvent)));

    when(taskInstanceService.getByIdWihEvents(taskId)).thenReturn(mockTask);

    taskInstanceService.claimTask(Code.create("current-user"), taskId, note);

    verify(mockTask, times(1)).claim(Code.create("current-user"),note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvent);

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .claimTask(anyString(), eq(note));
  }


  @Test
  void assignTask_shouldAssignTaskAndCallRepositories() {
    UUID taskId = UUID.randomUUID();
    Code userToAssign = Code.create("USER-001");
    String note = "Assigning task";

    TaskInstance mockTask = mock(TaskInstance.class);
    TaskInstanceEvent mockEvent = mock(TaskInstanceEvent.class);

    when(mockTask.getTaskInstanceEvents()).thenReturn(new java.util.LinkedList<>(List.of(mockEvent)));

    when(taskInstanceService.getByIdWihEvents(taskId)).thenReturn(mockTask);

    taskInstanceService.assignTask(Code.create("current-user"), taskId, userToAssign, note);

    verify(mockTask, times(1)).assign(Code.create("current-user"), userToAssign, note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvent);

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .assignTask(anyString(), eq(userToAssign.getValue()), eq(note));
  }


  @Test
  void unClaimTask_shouldAssignTaskAndCallRepositories() {
    UUID taskId = UUID.randomUUID();
    String note = "Assigning task";

    TaskInstance mockTask = mock(TaskInstance.class);
    TaskInstanceEvent mockEvent = mock(TaskInstanceEvent.class);

    when(mockTask.getTaskInstanceEvents()).thenReturn(new java.util.LinkedList<>(List.of(mockEvent)));

    when(taskInstanceService.getByIdWihEvents(taskId)).thenReturn(mockTask);

    taskInstanceService.unClaimTask(Code.create("current-user"), taskId, note);

    verify(mockTask, times(1)).unClaim(Code.create("current-user"), note);

    verify(taskInstanceEventRepository, times(1)).save(mockEvent);

    verify(taskInstanceRepository, times(1)).update(mockTask);

    verify(runtimeProcessEngineRepository, times(1))
        .unClaimTask(anyString());
  }


  @Test
  void completeTask_shouldCompleteAndCreateNextTasks() {
    // Arrange
    UUID taskId = UUID.randomUUID();
    Map<String,Object> variables = Map.of("key","value");

    TaskInstance mockTask = mock(TaskInstance.class);
    ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
    ProcessInstance mockActivityProcess = mock(ProcessInstance.class);

    // Mock taskInstanceService behavior
    when(taskInstanceService.getByIdWihEvents(taskId)).thenReturn(mockTask);
    when(processInstanceRepository.findById(any())).thenReturn(Optional.of(mockProcessInstance));
    when(runtimeProcessEngineRepository.getProcessInstanceById(anyString()))
        .thenReturn(mockActivityProcess);

    when(mockTask.getExternalId()).thenReturn(Code.create("EXT-123"));
    when(mockTask.getProcessInstanceId()).thenReturn(Identifier.create(UUID.randomUUID()));
    when(mockTask.getProcessNumber()).thenReturn(Code.create("PROC-123"));
    when(mockTask.getApplicationBase()).thenReturn(Code.create("APP-XYZ"));
    when(mockTask.getTaskInstanceEvents())
        .thenReturn(new LinkedList<>(List.of(mock(TaskInstanceEvent.class))));

    when(mockActivityProcess.getName()).thenReturn("Process Name");
    when(mockActivityProcess.getStatus()).thenReturn(ProcessInstanceStatus.COMPLETED);
    when(mockProcessInstance.getBusinessKey()).thenReturn(Code.create("BK-001"));
    when(mockProcessInstance.getApplicationBase()).thenReturn(Code.create("APP-XYZ"));
    when(mockProcessInstance.getStartedBy()).thenReturn("current-user");

    // --- Mock dependências dos novos métodos ---
    var activeTask = mock(TaskInstance.class);
    when(runtimeProcessEngineRepository.getActiveTaskInstances("PROC-123"))
        .thenReturn(List.of(activeTask));

    var processArtifact = mock(ProcessArtifactEntity.class);
    when(processArtifact.getKey()).thenReturn("taskKey");
    when(processArtifact.getFormKey()).thenReturn("formKey");
    when(processArtifactEntityRepository.findAllByProcessDefinitionId(anyString()))
        .thenReturn(List.of(processArtifact));

    // Spy para verificar createTask()
    TaskInstanceService spyService = spy(taskInstanceService);

    verify(taskInstanceRepository).create(any(TaskInstance.class));
    verify(taskInstanceEventRepository).save(any());

    // Act
    TaskInstance result = spyService.completeTask(Code.create("current-user"), taskId, variables);

    // Assert
    verify(runtimeProcessEngineRepository, times(1)).completeTask("EXT-123", variables);
    verify(mockTask, times(1)).complete(Code.create("current-user"));
    verify(taskInstanceRepository, times(1)).update(mockTask);
    verify(taskInstanceEventRepository, times(1)).save(any());

    // Verifica que as tasks ativas foram criadas
    verify(taskInstanceRepository).create(any(TaskInstance.class));
    verify(taskInstanceEventRepository).save(any());

    assertEquals(mockTask, result);
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

    UUID taskId = UUID.randomUUID();
    TaskInstance mockTask = mock(TaskInstance.class);
    Map<String, Object> variables = Map.of("var1", "value1", "var2", 123);

    when(taskInstanceService.getByIdWihEvents(taskId)).thenReturn(mockTask);
    when(mockTask.getExternalId()).thenReturn(Code.create("EXT-001"));
    when(runtimeProcessEngineRepository.getTaskVariables("EXT-001")).thenReturn(variables);

    Map<String, Object> result = taskInstanceService.getTaskVariables(taskId);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("value1", result.get("var1"));
    assertEquals(123, result.get("var2"));

    verify(taskInstanceService, times(1)).getByIdWihEvents(taskId);
    verify(mockTask, times(1)).getExternalId();
    verify(runtimeProcessEngineRepository, times(1)).getTaskVariables("EXT-001");
  }

}

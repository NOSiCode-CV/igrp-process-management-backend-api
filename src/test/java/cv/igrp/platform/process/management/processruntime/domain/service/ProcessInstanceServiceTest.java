package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessStatistics;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessInstanceServiceTest {

  @Mock
  private RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  @Mock
  private ProcessInstanceRepository processInstanceRepository;

  @InjectMocks
  private ProcessInstanceService processInstanceService;


  @BeforeEach
  void setup() {

  }



  @Test
  void testGetAllProcessInstances() {
    // todo
  }



  @Test
  void testGetProcessInstanceById() {
    // todo
  }



  @Test
  void testStartProcessInstance() {
    Code currentUser = Code.create("demo@nosi.cv");
    // todo
  }



  @Test
  void testGetProcessInstanceTaskStatus() {

    var processId = UUID.randomUUID();
    ProcessInstance mockInstance = mock(ProcessInstance.class);
    Code mockEngProcNumber = mock(Code.class);

    when(mockEngProcNumber.getValue()).thenReturn("PROC-123");
    when(mockInstance.getEngineProcessNumber()).thenReturn(mockEngProcNumber);

    // spy para sobrescrever getProcessInstanceById
    ProcessInstanceService spyService = spy(processInstanceService);
    doReturn(mockInstance).when(spyService).getProcessInstanceById(processId);

    // mock do repositório externo (usar PROC-123, não processId.toString())
    List<ProcessInstanceTaskStatus> mockStatus = List.of(
        ProcessInstanceTaskStatus.builder().taskKey(Code.create("Task1"))
            .taskName(Name.create("Task1Name")).status(TaskInstanceStatus.CREATED)
            .processInstanceId(Code.create(processId.toString())).build(),
        ProcessInstanceTaskStatus.builder().taskKey(Code.create("Task2"))
            .taskName(Name.create("Task2Name")).status(TaskInstanceStatus.COMPLETED)
            .processInstanceId(Code.create(processId.toString())).build()
    );
    when(runtimeProcessEngineRepository.getProcessInstanceTaskStatus("PROC-123"))
        .thenReturn(mockStatus);

    // execução
    List<ProcessInstanceTaskStatus> result = spyService.getProcessInstanceTaskStatus(processId);

    // asserts
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Task1Name", result.get(0).getTaskName().getValue());
    assertEquals(TaskInstanceStatus.CREATED, result.get(0).getStatus());
    assertEquals("Task2Name", result.get(1).getTaskName().getValue());
    assertEquals(TaskInstanceStatus.COMPLETED, result.get(1).getStatus());

    // verify
    verify(spyService).getProcessInstanceById(processId);
    verify(runtimeProcessEngineRepository).getProcessInstanceTaskStatus("PROC-123");

  }



  @Test
  void testGetTaskStatisticsByUser(){

    ProcessStatistics mockStats = ProcessStatistics.builder()
        .totalProcessInstances(100L)
        .totalCreatedProcess(52L)
        .totalRunningProcess(18L)
        .totalCompletedProcess(5L)
        .totalSuspendedProcess(25L)
        .totalCanceledProcess(7L)
        .build();

    when(processInstanceRepository.getProcessInstanceStatistics()).thenReturn(mockStats);

    ProcessStatistics stats = processInstanceService.getProcessInstanceStatistics();

    assertNotNull(stats);
    assertEquals(100L, stats.getTotalProcessInstances());
    assertEquals(52L, stats.getTotalCreatedProcess());
    assertEquals(18L, stats.getTotalRunningProcess());
    assertEquals(5L, stats.getTotalCompletedProcess());
    assertEquals(25L, stats.getTotalSuspendedProcess());
    assertEquals(7L, stats.getTotalCanceledProcess());

    verify(processInstanceRepository).getProcessInstanceStatistics();

  }

}

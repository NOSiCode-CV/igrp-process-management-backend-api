package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceStatsDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessStatistics;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetProcessInstanceStatisticsQueryHandlerTest {

  @Mock
  private ProcessInstanceService service;

  @Mock
  private ProcessInstanceMapper mapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private GetProcessInstanceStatisticsQueryHandler handler;

  private ProcessStatistics mockStats;
  private ProcessInstanceStatsDTO mockDto;
  private GetProcessInstanceStatisticsQuery query;

  @BeforeEach
  void setUp() {

    query = new GetProcessInstanceStatisticsQuery();

    mockStats = ProcessStatistics.builder()
        .totalProcessInstances(103L)
        .totalCreatedProcess(52L)
        .totalRunningProcess(18L)
        .totalCompletedProcess(25L)
        .totalSuspendedProcess(5L)
        .totalCanceledProcess(7L)
        .build();

    mockDto = new ProcessInstanceStatsDTO(
        mockStats.getTotalProcessInstances(),
        mockStats.getTotalCreatedProcess(),
        mockStats.getTotalRunningProcess(),
        mockStats.getTotalCompletedProcess(),
        mockStats.getTotalSuspendedProcess(),
        mockStats.getTotalCanceledProcess()
    );

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
    when(service.getProcessInstanceStatistics()).thenReturn(mockStats);
    when(mapper.toProcessInstanceStatsDto(mockStats)).thenReturn(mockDto);
  }

  @Test
  void testHandleGetProcessInstanceStatisticsQuery() {
    // When
    ResponseEntity<ProcessInstanceStatsDTO> response = handler.handle(query);

    // Then
    assertNotNull(response);
    assertEquals(ResponseEntity.ok(mockDto), response);

    verify(userContext).getCurrentUser();
    verify(service).getProcessInstanceStatistics();
    verify(mapper).toProcessInstanceStatsDto(mockStats);
  }

}

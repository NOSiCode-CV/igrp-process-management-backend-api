package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceStatsDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskStatistics;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMyTaskInstanceStatisticsQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private GetMyTaskInstanceStatisticsQueryHandler handler;

  private TaskStatistics mockStats;
  private TaskInstanceStatsDTO mockDto;
  private GetMyTaskInstanceStatisticsQuery query;

  @BeforeEach
  void setUp() {

    query = new GetMyTaskInstanceStatisticsQuery();

    mockStats = TaskStatistics.builder()
        .totalTaskInstances(100L)
        .totalAvailableTasks(52L)
        .totalAssignedTasks(18L)
        .totalCompletedTasks(25L)
        .totalSuspendedTasks(5L)
        .totalCanceledTasks(7L)
        .build();

    mockDto = new TaskInstanceStatsDTO(
        mockStats.getTotalTaskInstances(),
        mockStats.getTotalAvailableTasks(),
        mockStats.getTotalAssignedTasks(),
        mockStats.getTotalCompletedTasks(),
        mockStats.getTotalSuspendedTasks(),
        mockStats.getTotalCanceledTasks()
    );

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
    when(taskInstanceService.getTaskStatisticsByUser(any(Code.class))).thenReturn(mockStats);
    when(taskInstanceMapper.toTaskInstanceStatsDto(mockStats)).thenReturn(mockDto);

  }


  @Test
  void testHandleGetMyTaskInstanceStatisticsQuery() {

    // When
    ResponseEntity<TaskInstanceStatsDTO> response = handler.handle(query);

    // Then
    assertNotNull(response);
    assertEquals(ResponseEntity.ok(mockDto), response);

    verify(userContext).getCurrentUser();
    verify(taskInstanceService).getTaskStatisticsByUser(eq(Code.create("demo@nosi.cv")));
    verify(taskInstanceMapper).toTaskInstanceStatsDto(mockStats);

  }

}

package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceTaskStatusMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTaskStatusQueryHandlerTest {

  @Mock
  private ProcessInstanceService processInstanceService;

  @Mock
  private ProcessInstanceTaskStatusMapper mapper;

  @InjectMocks
  private GetTaskStatusQueryHandler handler;

  private GetTaskStatusQuery query;
  private UUID queryId;
  private List<ProcessInstanceTaskStatus> taskStatusList;
  private List<ProcessInstanceTaskStatusDTO> dtoList;

  @BeforeEach
  void setUp() {
    queryId = UUID.randomUUID();
    query = new GetTaskStatusQuery(queryId.toString());

    // Criar mocks para os retornos do service e mapper
    taskStatusList = List.of(mock(ProcessInstanceTaskStatus.class));
    dtoList = List.of(mock(ProcessInstanceTaskStatusDTO.class));

    when(processInstanceService.getProcessInstanceTaskStatus(queryId))
        .thenReturn(taskStatusList);

    when(mapper.toDTO(taskStatusList))
        .thenReturn(dtoList);
  }

  @Test
  void testHandleGetTaskStatusQuery() {
    // When
    ResponseEntity<List<ProcessInstanceTaskStatusDTO>> response = handler.handle(query);

    // Then
    assertNotNull(response);
    assertEquals(dtoList, response.getBody());

    verify(processInstanceService).getProcessInstanceTaskStatus(queryId);
    verify(mapper).toDTO(taskStatusList);
  }
}

package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTaskVariablesByIdQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @InjectMocks
  private GetTaskVariablesByIdQueryHandler handler;

  private GetTaskVariablesByIdQuery query;
  private UUID taskId;
  private Map<String, Object> variablesMap;
  private Set<TaskVariableDTO> taskVariableDTOSet;

  @BeforeEach
  void setUp() {
    taskId = UUID.randomUUID();
    query = new GetTaskVariablesByIdQuery(taskId.toString());

    variablesMap = Map.of(
        "code", "ABC",
        "key", "123456"
    );

    TaskVariableDTO dto1 = new TaskVariableDTO("code", "ABC");
    TaskVariableDTO dto2 = new TaskVariableDTO("key", "123456");
    taskVariableDTOSet = Set.of(dto1, dto2);

    when(taskInstanceService.getTaskVariables(taskId)).thenReturn(variablesMap);
    when(taskInstanceMapper.toTaskVariableListDTO(variablesMap)).thenReturn(taskVariableDTOSet);
  }

  @Test
  void testHandleGetTaskVariablesByIdQuery() {

    ResponseEntity<Set<TaskVariableDTO>> response = handler.handle(query);

    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(taskVariableDTOSet, response.getBody());

    verify(taskInstanceService).getTaskVariables(taskId);
    verify(taskInstanceMapper).toTaskVariableListDTO(variablesMap);
  }
}

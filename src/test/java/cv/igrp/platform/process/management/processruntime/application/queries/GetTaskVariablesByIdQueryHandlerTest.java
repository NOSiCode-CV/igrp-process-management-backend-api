package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTaskVariablesByIdQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private GetTaskVariablesByIdQueryHandler handler;

  private GetTaskVariablesByIdQuery query;
  private Identifier taskId;
  private List<TaskVariableDTO> taskVariablesListDTO;
  private Map<String,Object> taskVariables;

  @BeforeEach
  void setUp() {

    taskId = Identifier.generate();
    query = new GetTaskVariablesByIdQuery(taskId.getValue().toString());

    // Simula o objeto de domínio retornado pelo service
    taskVariables = new HashMap<>(Map.of("name", "Maria", "age", 35));

    // Simula o DTO retornado pelo mapper
    taskVariablesListDTO = new ArrayList<>(List.of(
        new TaskVariableDTO("name", "Maria"),
        new TaskVariableDTO("age", 35)));

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
    when(taskInstanceService.getTaskVariables(taskId)).thenReturn(taskVariables);
    when(taskInstanceMapper.toTaskVariableListDTO(taskVariables)).thenReturn(taskVariablesListDTO);

  }


  @Test
  void testHandleGetTaskVariablesByIdQuery() {

    ResponseEntity<List<TaskVariableDTO>> response = handler.handle(query);

    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(taskVariablesListDTO, response.getBody());

    verify(userContext).getCurrentUser();
    verify(taskInstanceService).getTaskVariables(taskId);
    verify(taskInstanceMapper).toTaskVariableListDTO(taskVariables);

  }

}

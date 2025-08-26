package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskDataDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

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
  private UUID taskId;
  private Map<String, Object> variablesMap;
  private TaskDataDTO taskDataDTO;

  @BeforeEach
  void setUp() {
    /*taskId = UUID.randomUUID();
    query = new GetTaskVariablesByIdQuery(taskId);

    variablesMap = Map.of(
        "code", "ABC",
        "key", "123456"
    );

    TaskVariableDTO decision = new TaskVariableDTO("decision", "A");
    taskDataDTO = new TaskDataDTO();
    taskDataDTO.setVariables(new ArrayList<>(List.of(decision)));
    taskDataDTO.setForms(new ArrayList<>(List.of(
        new TaskVariableDTO("name", "Maria"),
        new TaskVariableDTO("age", 35))));

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));

    when(taskInstanceService.getTaskVariables(taskId)).thenReturn(taskDataDTO);
    when(taskInstanceMapper.toTaskVariableListDTO(variablesMap)).thenReturn(taskDataDTO);*/

  }

  @Test
  void testHandleGetTaskVariablesByIdQuery() {

    /*ResponseEntity<TaskDataDTO> response = handler.handle(query);

    assertNotNull(response);
    assertTrue(response.getStatusCode().is2xxSuccessful());
    assertEquals(taskDataDTO, response.getBody());

    verify(taskInstanceService).getTaskVariables(taskId);
    verify(taskInstanceMapper).toTaskVariableListDTO(variablesMap);*/
  }

}

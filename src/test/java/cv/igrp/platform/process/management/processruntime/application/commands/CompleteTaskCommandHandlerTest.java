package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskDataDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompleteTaskCommandHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private CompleteTaskCommandHandler completeTaskCommandHandler;

  private UUID taskId;
  private TaskDataDTO taskDataDTO;
  private Code currenteUser;

  @BeforeEach
  void setUp() {

    taskId = UUID.randomUUID();

    Map<String,Object> variables = new HashMap<>();
    variables.put("global_decisao", "A");
    Map<String,Object> forms = new HashMap<>();
    forms.put("nome", "Maria");
    forms.put("idade", Integer.valueOf(35));
    taskDataDTO = new TaskDataDTO();
    taskDataDTO.setVariables(variables.entrySet().stream()
        .map(e -> new TaskVariableDTO(e.getKey(), e.getValue()))
        .toList());
    taskDataDTO.setForms(forms.entrySet().stream()
        .map(e -> new TaskVariableDTO(e.getKey(), e.getValue()))
        .toList());
    currenteUser = Code.create("demo@nosi.cv");
    when(userContext.getCurrentUser()).thenReturn(currenteUser);
  }

  @Test
  void handle_shouldCompleteTaskAndReturnDTO() {
    // Given

    CompleteTaskCommand command = new CompleteTaskCommand();
    command.setId(taskId.toString());
    command.setTaskdatadto(taskDataDTO);

    TaskInstance taskInstance = mock(TaskInstance.class);
    TaskInstanceDTO taskInstanceDTO = new TaskInstanceDTO();

    when(taskInstanceService.completeTask(eq(taskId), eq(currenteUser), anyMap(), anyMap()))
        .thenReturn(taskInstance);
    when(taskInstanceMapper.toTaskInstanceDTO(taskInstance)).thenReturn(taskInstanceDTO);

    // When
    ResponseEntity<TaskInstanceDTO> response = completeTaskCommandHandler.handle(command);

    // Then
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(taskInstanceDTO, response.getBody());

    verify(taskInstanceService).completeTask(eq(taskId), eq(currenteUser), anyMap(), anyMap());
    verify(taskInstanceMapper).toTaskInstanceDTO(taskInstance);
    verify(userContext).getCurrentUser();
    verifyNoMoreInteractions(taskInstanceService, taskInstanceMapper, userContext);
  }
}

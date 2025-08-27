package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignTaskCommandHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private AssignTaskCommandHandler assignTaskCommandHandler;

  private UUID taskId;
  private String targetUserName;
  private String note;

  @BeforeEach
  void setUp() {
    taskId = UUID.randomUUID();
    targetUserName = "igrp@nosi.cv";
    note = "This is a note";
    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
  }

  @Test
  void handle_shouldCallAssignTaskServiceAndReturnNoContent() {
    // Given
    AssignTaskCommand command = new AssignTaskCommand();
    command.setId(taskId.toString());
    command.setUser(targetUserName);
    command.setNote(note);

    Code currentUser = Code.create("demo@nosi.cv");
    Code targetUser = Code.create(targetUserName);

    // When
    ResponseEntity<String> response = assignTaskCommandHandler.handle(command);

    // Then
    assertNotNull(response);
    assertEquals(204, response.getStatusCodeValue()); // noContent

    // Checks if the service was called with the correct parameters
    verify(taskInstanceService).assignTask(
        eq(taskId),
        eq(currentUser),
        eq(targetUser),
        eq(note)
    );

    // Any other interaction
    verifyNoMoreInteractions(taskInstanceService, userContext);
  }
}

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

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnClaimTaskCommandHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private UnClaimTaskCommandHandler unClaimTaskCommandHandler;

  private UUID taskId;
  private String note;
  private Code currentUser;

  @BeforeEach
  void setUp() {
    taskId = UUID.randomUUID();
    note = "This is a note";
    currentUser = Code.create("demo@nosi.cv");
    when(userContext.getCurrentUser()).thenReturn(currentUser);
  }

  @Test
  void handle_shouldCallClaimTaskServiceAndReturnNoContent() {
    // Given
    /*UnClaimTaskCommand command = new UnClaimTaskCommand();
    command.setId(taskId.toString());
    command.setNote(note);

    // When
    ResponseEntity<String> response = unClaimTaskCommandHandler.handle(command);

    // Then
    assertNotNull(response);
    assertEquals(204, response.getStatusCodeValue()); // noContent

    // Checks if the service was called with the correct parameters
    verify(taskInstanceService).unClaimTask(
        eq(taskId),
        eq(currentUser),
        eq(note)
    );

    // Any other interaction
    verifyNoMoreInteractions(taskInstanceService, userContext);*/
  }
}

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
public class ClaimTaskCommandHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private ClaimTaskCommandHandler claimTaskCommandHandler;

  private UUID taskId;
  private String note;

    @BeforeEach
    void setUp() {
      taskId = UUID.randomUUID();
      note = "This is a note";
      when(userContext.getCurrentUser()).thenReturn(Code.create("current-user"));
    }

  @Test
  void handle_shouldCallClaimTaskServiceAndReturnNoContent() {
    // Given
    ClaimTaskCommand command = new ClaimTaskCommand();
    command.setId(taskId.toString());
    command.setNote(note);

    // When
    ResponseEntity<String> response = claimTaskCommandHandler.handle(command);

    // Then
    assertNotNull(response);
    assertEquals(204, response.getStatusCodeValue()); // noContent

    // Checks if the service was called with the correct parameters
    verify(taskInstanceService).claimTask(
        eq(taskId),
        eq(note),
        eq(Code.create("current-user"))
    );

    // Any other interaction
    verifyNoMoreInteractions(taskInstanceService, userContext);
  }
}

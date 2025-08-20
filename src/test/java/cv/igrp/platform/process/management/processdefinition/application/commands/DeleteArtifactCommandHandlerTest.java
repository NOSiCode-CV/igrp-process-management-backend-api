package cv.igrp.platform.process.management.processdefinition.application.commands;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class DeleteArtifactCommandHandlerTest {

  @Mock
  private ProcessArtifactService processArtifactService;

  @InjectMocks
  private DeleteArtifactCommandHandler handler;

  @BeforeEach
  void setUp() {

  }

  @Test
  void handle_ShouldDeleteArtifact_AndReturnNoContent() {
    // Arrange
    String artifactId = UUID.randomUUID().toString();
    DeleteArtifactCommand command = new DeleteArtifactCommand(artifactId);

    // Act
    ResponseEntity<String> response = handler.handle(command);

    // Assert
    verify(processArtifactService).deleteArtifact(Identifier.create(artifactId));

    assertEquals(204, response.getStatusCode().value());
    assertNull(response.getBody());

  }

}

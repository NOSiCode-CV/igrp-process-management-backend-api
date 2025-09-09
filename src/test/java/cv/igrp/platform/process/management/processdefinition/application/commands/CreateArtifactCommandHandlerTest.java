package cv.igrp.platform.process.management.processdefinition.application.commands;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessArtifactMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class CreateArtifactCommandHandlerTest {

  @Mock
  private ProcessArtifactService processArtifactService;

  private CreateArtifactCommandHandler handler;

  @BeforeEach
  void setUp() {
    ProcessArtifactMapper mapper = new ProcessArtifactMapper();
    handler = new CreateArtifactCommandHandler(processArtifactService, mapper);
  }

  @Test
  void handle_shouldCreateArtifactAndReturn201Response() {
    // Arrange
    ProcessArtifactRequestDTO requestDto = new ProcessArtifactRequestDTO();
    requestDto.setName("Task 1");
    requestDto.setKey("task_1");
    requestDto.setFormKey("/path/to/form/task_1");

    String processDefinitionId = "12345678";
    CreateArtifactCommand command = new CreateArtifactCommand(requestDto, processDefinitionId);

    ProcessArtifact artifact = ProcessArtifact.builder()
        .id(Identifier.generate())
        .name(Name.create("Task 1"))
        .processDefinitionId(Code.create(processDefinitionId))
        .key(Code.create("task_1"))
        .formKey(Code.create("/path/to/form/task_1"))
        .build();

    when(processArtifactService.create(any(ProcessArtifact.class))).thenReturn(artifact);

    // Act
    ResponseEntity<ProcessArtifactDTO> response = handler.handle(command);

    // Assert
    assertNotNull(response);
    assertEquals(201, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(requestDto.getName(), response.getBody().getName());
    assertEquals(requestDto.getKey(), response.getBody().getKey());
    assertEquals(requestDto.getFormKey(), response.getBody().getFormKey());
    assertEquals(processDefinitionId, response.getBody().getProcessDefinitionId());

    // Capture argument
    ArgumentCaptor<ProcessArtifact> captor = ArgumentCaptor.forClass(ProcessArtifact.class);
    verify(processArtifactService).create(captor.capture());
    ProcessArtifact passed = captor.getValue();
    assertEquals(requestDto.getName(), passed.getName().getValue());
    assertEquals(requestDto.getKey(), passed.getKey().getValue());
    assertEquals(requestDto.getFormKey(), passed.getFormKey().getValue());
    assertEquals(processDefinitionId, passed.getProcessDefinitionId().getValue());

    verify(processArtifactService).create(any(ProcessArtifact.class));

  }

}

package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.StartProcessRequestDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartProcessInstanceCommandHandlerTest {

  @Mock
  private ProcessInstanceService service;

  @Mock
  private ProcessInstanceMapper mapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private StartProcessInstanceCommandHandler handler;

  private StartProcessInstanceCommand command;
  private String currentUserName;
  private String definitionId;

  private ProcessInstanceDTO mockProcessInstanceDTO;

  @BeforeEach
  void setUp() {
    definitionId = UUID.randomUUID().toString();
    currentUserName = "demo@nosi.cv";
    String instanceId = UUID.randomUUID().toString();

    when(userContext.getCurrentUser()).thenReturn(Code.create(currentUserName));

    StartProcessRequestDTO payloadDto = new StartProcessRequestDTO();
    payloadDto.setProcessDefinitionId(definitionId);

    command = new StartProcessInstanceCommand();
    command.setStartprocessrequestdto(payloadDto);

    var mockProcessInstance = mock(ProcessInstance.class);
    mockProcessInstanceDTO = mock(ProcessInstanceDTO.class);

    when(mapper.toModel(payloadDto)).thenReturn(mockProcessInstance);
    when(service.startProcessInstance(mockProcessInstance, currentUserName)).thenReturn(mockProcessInstance);
    when(mockProcessInstance.getId()).thenReturn(Identifier.create(instanceId));
    when(mockProcessInstance.getProcReleaseId()).thenReturn(Code.create(definitionId));
    when(mapper.toDTO(mockProcessInstance)).thenReturn(mockProcessInstanceDTO);
  }

  @Test
  void testHandle() {
    // When
    ResponseEntity<ProcessInstanceDTO> response = handler.handle(command);

    // Then
    assertNotNull(response);
    assertEquals(201, response.getStatusCodeValue());
    assertSame(mockProcessInstanceDTO, response.getBody());

    // Verify
    verify(service).startProcessInstance(
        argThat(p -> p.getProcReleaseId().equals(Code.create(definitionId))),
        eq(currentUserName)
    );

    verifyNoMoreInteractions(service, userContext, mapper);
  }
}

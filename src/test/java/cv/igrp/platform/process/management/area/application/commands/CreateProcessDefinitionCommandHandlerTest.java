package cv.igrp.platform.process.management.area.application.commands;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateProcessDefinitionCommandHandlerTest {

  @Mock
  private AreaService areaService;

  private AreaProcessMapper areaProcessMapper;

  private CreateProcessDefinitionCommandHandler handler;

  @BeforeEach
  void setUp() {
    areaProcessMapper = new AreaProcessMapper();
    handler = new CreateProcessDefinitionCommandHandler(areaService, areaProcessMapper);
  }

  @Test
  void testHandle_createsProcessDefinitionAndReturnsCreatedDTO() {

    UUID areaId = UUID.randomUUID();

    ProcessDefinitionRequestDTO requestDTO = new ProcessDefinitionRequestDTO();
    requestDTO.setProcessKey("processKey123");
    requestDTO.setReleaseId("releaseId123");
    requestDTO.setVersion("1");
    requestDTO.setName("Process Name");

    CreateProcessDefinitionCommand command = new CreateProcessDefinitionCommand(
        requestDTO,
        areaId.toString()
    );

    AreaProcess areaProcessModel = areaProcessMapper.toModel(requestDTO);

    when(areaService.createProcessDefinition(eq(areaId), any(AreaProcess.class)))
        .thenReturn(areaProcessModel);

    ResponseEntity<ProcessDefinitionDTO> response = handler.handle(command);

    verify(areaService).createProcessDefinition(eq(areaId), any(AreaProcess.class));

    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    ProcessDefinitionDTO dto = response.getBody();
    assertNotNull(dto);
    assertEquals(requestDTO.getProcessKey(), dto.getProcessKey());
    assertEquals(requestDTO.getReleaseId(), dto.getReleaseId());
    assertEquals(requestDTO.getVersion(), dto.getVersion());
    assertEquals(requestDTO.getName(), dto.getName());
  }

}

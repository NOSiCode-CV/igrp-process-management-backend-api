package cv.igrp.platform.process.management.area.application.commands;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UpdateAreaCommandHandlerTest {

  @Mock
  private AreaService areaService;

  private AreaMapper areaMapper;

  private UpdateAreaCommandHandler handler;

  @BeforeEach
  void setUp() {
    areaMapper = new AreaMapper(new AreaProcessMapper());

    handler = new UpdateAreaCommandHandler(areaService, areaMapper);
  }

  @Test
  void testHandle_updatesAreaAndReturnsDTO() {
    UUID id = UUID.randomUUID();

    AreaRequestDTO requestDTO = new AreaRequestDTO();
    requestDTO.setCode("CODE123");
    requestDTO.setName("Area Name");
    requestDTO.setApplicationBase("AppBase");
    requestDTO.setParentId(null);
    requestDTO.setDescription("Description");

    UpdateAreaCommand command = new UpdateAreaCommand(requestDTO, id.toString());

    Area updatedArea = Area.builder()
        .id(Identifier.create(id))
        .code(Code.create("CODE123"))
        .name(Name.create("Area 1"))
        .applicationBase(Code.create("igrp-app"))
        .description("Test")
        .build();

    when(areaService.updateArea(eq(id), any(Area.class))).thenReturn(updatedArea);

    ResponseEntity<AreaDTO> response = handler.handle(command);

    verify(areaService).updateArea(eq(id), any(Area.class));
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

    AreaDTO body = response.getBody();
    assertNotNull(body);

    assertEquals("CODE123", body.getCode());
    assertEquals("Area 1", body.getName());
    assertEquals("Test", body.getDescription());
    assertEquals("igrp-app", body.getApplicationBase());
    assertEquals(id, body.getId());

  }

}

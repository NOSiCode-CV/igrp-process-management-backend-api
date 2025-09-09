package cv.igrp.platform.process.management.area.application.queries;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
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
@ExtendWith(MockitoExtension.class)
public class GetAreaByIdQueryHandlerTest {

  @Mock
  private AreaService areaService;

  private GetAreaByIdQueryHandler handler;

  private Area area;
  private UUID areaId;

  @BeforeEach
  void setUp() {

    AreaProcessMapper areaProcessMapper = new AreaProcessMapper();
    AreaMapper areaMapper = new AreaMapper(areaProcessMapper);
    handler = new GetAreaByIdQueryHandler(areaService, areaMapper);

    areaId = UUID.randomUUID();

    area = Area.builder()
        .id(Identifier.create(areaId))
        .code(Code.create("CODE123"))
        .name(Name.create("Area Name"))
        .applicationBase(Code.create("AppBase"))
        .description("Description")
        .build();
  }

  @Test
  void testHandle_returnsAreaDTO() {
    GetAreaByIdQuery query = new GetAreaByIdQuery(areaId.toString());

    when(areaService.getAreaById(areaId)).thenReturn(area);

    ResponseEntity<AreaDTO> response = handler.handle(query);

    verify(areaService).getAreaById(areaId);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

    AreaDTO body = response.getBody();
    assertNotNull(body);
    assertEquals(areaId, body.getId());
    assertEquals("CODE123", body.getCode());
    assertEquals("Area Name", body.getName());
    assertEquals("AppBase", body.getApplicationBase());
    assertEquals("Description", body.getDescription());
  }

}

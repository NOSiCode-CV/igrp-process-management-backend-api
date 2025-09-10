package cv.igrp.platform.process.management.area.application.queries;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaListPageDTO;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaFilter;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import cv.igrp.platform.process.management.area.application.queries.*;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ListAreasQueryHandlerTest {

  @Mock
  private AreaService areaService;

  private AreaMapper areaMapper;

  private ListAreasQueryHandler handler;

  @BeforeEach
  void setUp() {

    areaMapper = new AreaMapper(new AreaProcessMapper());

    handler = new ListAreasQueryHandler(areaService, areaMapper);
  }

  @Test
  void testHandle_returnsAreaListPageDTO() {

    ListAreasQuery query = new ListAreasQuery();
    query.setName("AreaName");
    query.setApplicationBase("AppBase");
    query.setCode("CODE123");
    query.setParentId(null);
    query.setStatus("ACTIVE");
    query.setPage(0);
    query.setSize(1);

    Area sampleArea = Area.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .name(Name.create("AreaName"))
        .code(Code.create("CODE123"))
        .applicationBase(Code.create("AppBase"))
        .status(Status.ACTIVE)
        .description("Sample description")
        .build();

    PageableLista<Area> pageableLista = PageableLista.<Area>builder()
        .pageNumber(0)
        .pageSize(1)
        .totalElements(1L)
        .totalPages(1)
        .first(true)
        .last(true)
        .content(List.of(sampleArea))
        .build();

    when(areaService.getAllAreas(any(AreaFilter.class))).thenReturn(pageableLista);

    // Act
    ResponseEntity<AreaListPageDTO> response = handler.handle(query);

    // Verify
    verify(areaService).getAllAreas(any(AreaFilter.class));

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

    AreaListPageDTO body = response.getBody();
    assertNotNull(body);
    assertEquals(1, body.getContent().size());

    AreaDTO dto = body.getContent().getFirst();
    assertEquals(sampleArea.getName().getValue(), dto.getName());
    assertEquals(sampleArea.getCode().getValue(), dto.getCode());
    assertEquals(sampleArea.getApplicationBase().getValue(), dto.getApplicationBase());
    assertEquals(sampleArea.getStatus(), dto.getStatus());
  }

}

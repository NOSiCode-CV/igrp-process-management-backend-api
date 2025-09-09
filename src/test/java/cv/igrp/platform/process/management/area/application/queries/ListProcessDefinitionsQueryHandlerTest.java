package cv.igrp.platform.process.management.area.application.queries;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionListPageDTO;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
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
public class ListProcessDefinitionsQueryHandlerTest {

  @Mock
  private AreaService areaService;

  private AreaProcessMapper areaProcessMapper;

  private ListProcessDefinitionsQueryHandler handler;

  @BeforeEach
  void setUp() {
    areaProcessMapper = new AreaProcessMapper();
    handler = new ListProcessDefinitionsQueryHandler(areaService, areaProcessMapper);
  }

  @Test
  void testHandle_returnsProcessDefinitionListPageDTO() {
    // Arrange
    ListProcessDefinitionsQuery query = new ListProcessDefinitionsQuery();
    query.setProcessKey("procKey");
    query.setReleaseId("relId");
    query.setAreaId(null);
    query.setStatus("ACTIVE");
    query.setPage(0);
    query.setSize(1);

    AreaProcess sampleAreaProcess = AreaProcess.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .processKey(Code.create("procKey"))
        .releaseId(Code.create("relId"))
        .status(Status.ACTIVE)
        .name("Sample Process")
        .version("1.0")
        .build();

    PageableLista<AreaProcess> pageableLista = PageableLista.<AreaProcess>builder()
        .pageNumber(0)
        .pageSize(1)
        .totalElements(1L)
        .totalPages(1)
        .first(true)
        .last(true)
        .content(List.of(sampleAreaProcess))
        .build();

    when(areaService.getAllAreaProcess(any(AreaProcessFilter.class))).thenReturn(pageableLista);

    // Act
    ResponseEntity<ProcessDefinitionListPageDTO> response = handler.handle(query);

    // Verify
    verify(areaService).getAllAreaProcess(any(AreaProcessFilter.class));

    // Assertions
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

    ProcessDefinitionListPageDTO dto = response.getBody();
    assertNotNull(dto);
    assertEquals(1, dto.getContent().size());

    ProcessDefinitionDTO processDTO = dto.getContent().getFirst();
    assertEquals(sampleAreaProcess.getName(), processDTO.getName());
    assertEquals(sampleAreaProcess.getProcessKey().getValue(), processDTO.getProcessKey());
    assertEquals(sampleAreaProcess.getReleaseId().getValue(), processDTO.getReleaseId());
    assertEquals(sampleAreaProcess.getStatus(), processDTO.getStatus());
    assertEquals(sampleAreaProcess.getId().getValue(), processDTO.getId());
    assertEquals(sampleAreaProcess.getVersion(), processDTO.getVersion());

  }

}

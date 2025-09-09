package cv.igrp.platform.process.management.area.application.queries;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;
import cv.igrp.platform.process.management.shared.domain.service.ConfigParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ListAreaStatusQueryHandlerTest {

  @Mock
  private ConfigParameterService configParameterService;

  @InjectMocks
  private ListAreaStatusQueryHandler handler;

  @BeforeEach
  void setUp() {

  }

  @Test
  void testHandle_returnsConfigParameterDTOList() {

    List<Map<?, String>> statusList = List.of(
        Map.of(Status.ACTIVE.getCode(), Status.ACTIVE.getDescription()),
        Map.of(Status.INACTIVE.getCode(), Status.INACTIVE.getDescription())
    );

    when(configParameterService.getAreaProcessStatus()).thenReturn(statusList);

    ListAreaStatusQuery query = new ListAreaStatusQuery();

    ResponseEntity<List<ConfigParameterDTO>> response = handler.handle(query);

    verify(configParameterService).getAreaProcessStatus();

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());

    List<ConfigParameterDTO> body = response.getBody();
    assertNotNull(body);
    assertEquals(2, body.size());

    assertEquals(Status.ACTIVE.getDescription(), body.get(0).getLabel());
    assertEquals(Status.ACTIVE.getCode(), body.get(0).getValue());

    assertEquals(Status.INACTIVE.getDescription(), body.get(1).getLabel());
    assertEquals(Status.INACTIVE.getCode(), body.get(1).getValue());

  }

}

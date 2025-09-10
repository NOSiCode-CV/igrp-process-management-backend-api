package cv.igrp.platform.process.management.processruntime.application.queries;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListTaskInstanceStatusQueryHandlerTest {

  @Mock
  private ConfigParameterService configParameterService;

  private ListTaskInstanceStatusQuery query;

  @InjectMocks
  private ListTaskInstanceStatusQueryHandler listTaskInstanceStatusQueryHandler;

  @BeforeEach
  void setUp() {
    query = new ListTaskInstanceStatusQuery();

    // Simula retorno do service com uma lista de mapas
    List<Map<?, String>> mockResult = List.of(
        Map.of("CREATED", "Criada"),
        Map.of("COMPLETED", "Concluída")
    );

    when(configParameterService.getTaskInstanceStatus())
        .thenReturn(mockResult);
  }

  @Test
  void testHandleListTaskInstanceStatusQuery() {

    ResponseEntity<List<ConfigParameterDTO>> response = listTaskInstanceStatusQueryHandler.handle(query);

    assertNotNull(response);
    assertEquals(2, response.getBody().size());

    ConfigParameterDTO first = response.getBody().get(0);
    assertEquals("Criada", first.getLabel());
    assertEquals("CREATED", first.getValue());

    ConfigParameterDTO second = response.getBody().get(1);
    assertEquals("Concluída", second.getLabel());
    assertEquals("COMPLETED", second.getValue());

    verify(configParameterService).getTaskInstanceStatus();
  }

}

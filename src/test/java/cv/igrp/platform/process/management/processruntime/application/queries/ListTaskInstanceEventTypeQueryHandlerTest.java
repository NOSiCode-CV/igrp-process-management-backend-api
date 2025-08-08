package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.shared.domain.service.ConfigParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListTaskInstanceEventTypeQueryHandlerTest {

  @Mock
  private ConfigParameterService configParameterService;

  @InjectMocks
  private ListTaskInstanceEventTypeQueryHandler handler;


  @BeforeEach
  void setUp() {

    Map<String, String> map1 = Map.of("KEY1", "Value1");
    Map<String, String> map2 = Map.of("KEY2", "Value2");

    when(configParameterService.getTaskEventType())
        .thenReturn(List.of(map1, map2));

  }


  @Test
  void testHandleListTaskInstanceEventTypeQuery() {

    var query = new ListTaskInstanceEventTypeQuery();
    // When
    var response = handler.handle(query);

    // Then
    assertNotNull(response);
    assertEquals(2, response.getBody().size());

    assertEquals("Value1", response.getBody().get(0).getLabel());
    assertEquals("KEY1", response.getBody().get(0).getValue());

    assertEquals("Value2", response.getBody().get(1).getLabel());
    assertEquals("KEY2", response.getBody().get(1).getValue());

    verify(configParameterService, times(1)).getTaskEventType();

  }

}

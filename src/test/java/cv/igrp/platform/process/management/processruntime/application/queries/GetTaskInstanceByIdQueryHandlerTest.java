package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTaskInstanceByIdQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @InjectMocks
  private GetTaskInstanceByIdQueryHandler handler;

  private GetTaskInstanceByIdQuery query;
  private TaskInstance taskInstanceMock;
  private TaskInstanceDTO taskInstanceDTOMock;

  @InjectMocks
  private GetTaskInstanceByIdQueryHandler getTaskInstanceByIdQueryHandler;

  @BeforeEach
  void setUp() {
    query = new GetTaskInstanceByIdQuery(UUID.randomUUID().toString());
    taskInstanceMock = mock(TaskInstance.class);
    taskInstanceDTOMock = mock(TaskInstanceDTO.class);

    when(taskInstanceService.getByIdWihEvents(UUID.fromString(query.getId())))
        .thenReturn(taskInstanceMock);

    when(taskInstanceMapper.toTaskInstanceDTO(taskInstanceMock))
        .thenReturn(taskInstanceDTOMock);
  }

  @Test
  void testHandleGetTaskInstanceByIdQuery() {
    ResponseEntity<TaskInstanceDTO> response = handler.handle(query);

    assertNotNull(response);
    assertEquals(taskInstanceDTOMock, response.getBody());

    verify(taskInstanceService).getByIdWihEvents(UUID.fromString(query.getId()));
    verify(taskInstanceMapper).toTaskInstanceDTO(taskInstanceMock);
  }

}

package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTaskInstanceByIdQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private GetTaskInstanceByIdQueryHandler handler;

  private GetTaskInstanceByIdQuery query;
  private TaskInstance taskInstanceMock;
  private TaskInstanceDTO taskInstanceDTOMock;
  private Identifier taskId;

  @InjectMocks
  private GetTaskInstanceByIdQueryHandler getTaskInstanceByIdQueryHandler;

  @BeforeEach
  void setUp() {
    taskId = Identifier.generate();
    query = new GetTaskInstanceByIdQuery(taskId.getValue().toString());
    taskInstanceMock = mock(TaskInstance.class);
    taskInstanceDTOMock = mock(TaskInstanceDTO.class);

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));

    when(taskInstanceMock.getId()).thenReturn(taskId);

    when(taskInstanceService.getTaskById(taskId))
        .thenReturn(taskInstanceMock);

    when(taskInstanceMapper.toTaskInstanceDTO(taskInstanceMock))
        .thenReturn(taskInstanceDTOMock);
  }

  @Test
  void testHandleGetTaskInstanceByIdQuery() {

    ResponseEntity<TaskInstanceDTO> response = handler.handle(query);

    assertNotNull(response);
    assertEquals(taskInstanceDTOMock, response.getBody());

    verify(userContext).getCurrentUser();
    verify(taskInstanceService).getTaskById(taskId);
    verify(taskInstanceMapper).toTaskInstanceDTO(taskInstanceMock);
  }

}

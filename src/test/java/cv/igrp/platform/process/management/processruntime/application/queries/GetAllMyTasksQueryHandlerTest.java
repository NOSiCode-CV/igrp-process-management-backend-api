package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.util.TempUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllMyTasksQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @InjectMocks
  private GetAllMyTasksQueryHandler getAllMyTasksQueryHandler;

  private GetAllMyTasksQuery query;
  private PageableLista<TaskInstance> pageableListaMock;
  private TaskInstanceListPageDTO pageDTO;

  @BeforeEach
  void setUp() {

    query = new GetAllMyTasksQuery();

    List<TaskInstance> content = List.of(
        TaskInstance.builder().processNumber(Code.create("proc_num_test"))
        .taskKey(Code.create("task_key_test"))
        .externalId(Code.create(UUID.randomUUID().toString()))
        .name(Name.create("My task Name"))
        .id(Identifier.create(UUID.randomUUID()))
        .processInstanceId(Identifier.create(UUID.randomUUID()))
        .processName(Code.create("My Pro test v1"))
        .applicationBase(Code.create("app_test"))
        .status(TaskInstanceStatus.CREATED)
        .startedAt(LocalDateTime.now())
        .startedBy(TempUtil.getCurrentUser())
        .searchTerms("abc")
        .build()
    );

    pageableListaMock = new PageableLista<>(
        0,      // pageNumber
        10,     // pageSize
        1L,     // totalElements
        1,      // totalPages
        true,   // last
        true,   // first
        content // content
    );

    pageDTO = new TaskInstanceListPageDTO();

    when(taskInstanceMapper.toFilter(eq(query),eq(TempUtil.getCurrentUser())))
        .thenReturn(TaskInstanceFilter.builder().status(TaskInstanceStatus.CREATED).build());

    when(taskInstanceService.getAllMyTasks(any(TaskInstanceFilter.class)))
        .thenReturn(pageableListaMock);

    when(taskInstanceMapper.toTaskInstanceListaPageDTO(pageableListaMock))
        .thenReturn(pageDTO);
  }

  @Test
  void testHandleGetAllMyTasksQuery() {

    ResponseEntity<TaskInstanceListPageDTO> response = getAllMyTasksQueryHandler.handle(query);

    assertNotNull(response);
    assertEquals(pageDTO, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    verify(taskInstanceMapper).toFilter(eq(query),eq(TempUtil.getCurrentUser()));
    verify(taskInstanceService).getAllMyTasks(any(TaskInstanceFilter.class));
    verify(taskInstanceMapper).toTaskInstanceListaPageDTO(pageableListaMock);
  }

}

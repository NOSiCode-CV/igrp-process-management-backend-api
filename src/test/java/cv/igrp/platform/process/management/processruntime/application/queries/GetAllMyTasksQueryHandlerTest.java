package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

@ExtendWith(MockitoExtension.class)
class GetAllMyTasksQueryHandlerTest {

  @Mock
  private TaskInstanceService taskInstanceService;

  @Mock
  private TaskInstanceMapper taskInstanceMapper;

  @Mock
  private Principal principal;

  @InjectMocks
  private GetAllMyTasksQueryHandler getAllMyTasksQueryHandler;

  private GetAllMyTasksQuery query;
  private PageableLista<TaskInstance> pageableListaMock;
  private TaskInstanceListPageDTO pageDTO;

  @BeforeEach
  void setUp() {

//    when(principal.getName()).thenReturn("current-user");
//
//    query = new GetAllMyTasksQuery();
//
//    List<TaskInstance> content = List.of(
//        TaskInstance.builder().processNumber(Code.create("proc_num_test"))
//        .taskKey(Code.create("task_key_test"))
//        .externalId(Code.create(UUID.randomUUID().toString()))
//        .name(Name.create("My task Name"))
//        .id(Identifier.create(UUID.randomUUID()))
//        .processInstanceId(Identifier.create(UUID.randomUUID()))
//        .processName(Code.create("My Pro test v1"))
//        .applicationBase(Code.create("app_test"))
//        .status(TaskInstanceStatus.CREATED)
//        .startedAt(LocalDateTime.now())
//        .startedBy(Code.create("current-user"))
//        .searchTerms("abc")
//        .build()
//    );
//
//    pageableListaMock = new PageableLista<>(
//        0,      // pageNumber
//        10,     // pageSize
//        1L,     // totalElements
//        1,      // totalPages
//        true,   // last
//        true,   // first
//        content // content
//    );
//
//    pageDTO = new TaskInstanceListPageDTO();
//
//    when(taskInstanceMapper.toFilter(eq(query),eq(Code.create("current-user"))))
//        .thenReturn(TaskInstanceFilter.builder().status(TaskInstanceStatus.CREATED).build());
//
//    when(taskInstanceService.getAllMyTasks(any(TaskInstanceFilter.class)))
//        .thenReturn(pageableListaMock);
//
//    when(taskInstanceMapper.toTaskInstanceListPageDTO(pageableListaMock))
//        .thenReturn(pageDTO);
  }

  @Test
  void testHandleGetAllMyTasksQuery() {

//    ResponseEntity<TaskInstanceListPageDTO> response = getAllMyTasksQueryHandler.handle(query);
//
//    assertNotNull(response);
//    assertEquals(pageDTO, response.getBody());
//    assertEquals(HttpStatus.OK, response.getStatusCode());
//
//    verify(taskInstanceMapper).toFilter(eq(query),eq(Code.create("current-user")));
//    verify(taskInstanceService).getAllMyTasks(any(TaskInstanceFilter.class));
//    verify(taskInstanceMapper).toTaskInstanceListPageDTO(pageableListaMock);
  }

}

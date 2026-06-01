package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRule;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskAssignmentRuleFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskAssignmentRuleRepository;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskAssignmentRuleServiceTest {

  @Mock
  private TaskAssignmentRuleRepository repository;

  @InjectMocks
  private TaskAssignmentRuleService service;

  @Test
  void getAll_shouldDelegateToRepository() {
    var filter = TaskAssignmentRuleFilter.builder().page(1).size(10).build();
    var expected = PageableLista.<TaskAssignmentRule>builder()
        .pageNumber(1)
        .pageSize(10)
        .totalElements(0L)
        .totalPages(0)
        .content(List.of())
        .build();
    when(repository.findAll(filter)).thenReturn(expected);

    var result = service.getAll(filter);

    assertSame(expected, result);
    verify(repository).findAll(filter);
  }
}

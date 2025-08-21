package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskInstanceRepositoryImplTest {

  @Mock
  private TaskInstanceEntityRepository entityRepository;

  @Mock
  private TaskInstanceMapper mapper;

  @InjectMocks
  private TaskInstanceRepositoryImpl repository;

  private UUID taskId;
  private TaskInstanceEntity entityMock;
  private TaskInstance modelMock;


  @BeforeEach
  void setUp() {
    taskId = UUID.randomUUID();
    entityMock = mock(TaskInstanceEntity.class);
    modelMock = mock(TaskInstance.class);
  }


  @Test
  void create_shouldThrowException_whenMapperFails() {
    when(mapper.toNewTaskEntity(modelMock)).thenThrow(new RuntimeException("Mapper failed"));

    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> repository.create(modelMock));

    assertEquals("Mapper failed", ex.getMessage());
    verify(mapper).toNewTaskEntity(modelMock);
    verifyNoInteractions(entityRepository);
  }


  @Test
  void create_shouldThrowException_whenEntitySaveFails() {
    when(mapper.toNewTaskEntity(modelMock)).thenReturn(entityMock);
    when(entityRepository.save(entityMock)).thenThrow(new RuntimeException("DB error"));

    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> repository.create(modelMock));

    assertEquals("DB error", ex.getMessage());
    verify(mapper).toNewTaskEntity(modelMock);
    verify(entityRepository).save(entityMock);
    verifyNoMoreInteractions(mapper);
  }


  @Test
  void update_shouldThrowNotFound_whenEntityDoesNotExist() {
    when(modelMock.getId()).thenReturn(Identifier.create(taskId));
    when(entityRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(IgrpResponseStatusException.class,
        () -> repository.update(modelMock));

    verify(entityRepository).findById(taskId);
    verifyNoInteractions(mapper);
  }


  @Test
  void update_shouldThrowException_whenMapperFails() {
    when(modelMock.getId()).thenReturn(Identifier.create(taskId));
    when(entityRepository.findById(taskId)).thenReturn(Optional.of(entityMock));
    doThrow(new RuntimeException("Mapper update failed"))
        .when(mapper).toTaskEntity(modelMock, entityMock);

    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> repository.update(modelMock));

    assertEquals("Mapper update failed", ex.getMessage());
    verify(mapper).toTaskEntity(modelMock, entityMock);
    verifyNoMoreInteractions(entityRepository);
  }


  @Test
  void testFindByFilter() {
    // Given
    TaskInstanceFilter filter = TaskInstanceFilter.builder()
        .processName("MyProcess")
        .page(0)
        .size(10)
        .build();

    TaskInstanceEntity entity = new TaskInstanceEntity();
    TaskInstance model = TaskInstance.builder().build();

    Page<TaskInstanceEntity> entityPage = new PageImpl<>(
        List.of(entity),
        PageRequest.of(filter.getPage(), filter.getSize()),
        1
    );

    when(entityRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(entityPage);

    when(mapper.toModel(entity)).thenReturn(model);

    // When
    PageableLista<TaskInstance> result = repository.findAll(filter);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(model, result.getContent().get(0));
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.isFirst());
    assertTrue(result.isLast());

    verify(entityRepository, times(1))
        .findAll(any(Specification.class), any(Pageable.class));
    verify(mapper, times(1)).toModel(entity);
  }

}

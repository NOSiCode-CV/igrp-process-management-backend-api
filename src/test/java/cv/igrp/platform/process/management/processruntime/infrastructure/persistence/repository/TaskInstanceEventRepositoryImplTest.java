package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceEventMapper;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEventEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskInstanceEventRepositoryImplTest {

  @Mock
  private TaskInstanceEventEntityRepository entityRepository;

  @Mock
  private TaskInstanceEventMapper mapper;

  @InjectMocks
  private TaskInstanceEventRepositoryImpl repositoryImpl;

  private UUID eventId;
  private TaskInstanceEventEntity entityMock;
  private TaskInstanceEvent modelMock;

  @BeforeEach
  void setUp() {
    eventId = UUID.randomUUID();
    entityMock = mock(TaskInstanceEventEntity.class);
    modelMock = mock(TaskInstanceEvent.class);
  }


  @Test
  void findById_ShouldReturnMappedModel_WhenEntityExists() {
    when(entityRepository.findById(eventId)).thenReturn(Optional.of(entityMock));
    when(mapper.toEventModel(entityMock)).thenReturn(modelMock);

    Optional<TaskInstanceEvent> result = repositoryImpl.findById(eventId);

    assertTrue(result.isPresent());
    assertEquals(modelMock, result.get());

    verify(entityRepository).findById(eventId);
    verify(mapper).toEventModel(entityMock);
  }


  @Test
  void findById_ShouldReturnEmpty_WhenEntityNotFound() { // empty for not found

    when(entityRepository.findById(eventId)).thenReturn(Optional.empty());

    Optional<TaskInstanceEvent> result = repositoryImpl.findById(eventId);

    assertTrue(result.isEmpty());
    verify(entityRepository).findById(eventId);
    verifyNoInteractions(mapper);
  }


  @Test
  void save() {

    when(mapper.toEventEntity(modelMock)).thenReturn(entityMock);

    repositoryImpl.save(modelMock);

    verify(mapper).toEventEntity(modelMock);
    verify(entityRepository).save(entityMock);
  }


  @Test
  void save_shouldThrowException_whenMapperFails() {
    when(mapper.toEventEntity(modelMock))
        .thenThrow(new RuntimeException("Mapper failed"));

    RuntimeException thrown = assertThrows(
        RuntimeException.class,
        () -> repositoryImpl.save(modelMock)
    );

    assertEquals("Mapper failed", thrown.getMessage());
    verify(mapper).toEventEntity(modelMock);
    verifyNoInteractions(entityRepository);
  }


  @Test
  void save_shouldThrowException_whenEntityRepositoryFails() {
    when(mapper.toEventEntity(modelMock)).thenReturn(entityMock);
    when(entityRepository.save(entityMock))
        .thenThrow(new RuntimeException("Database save failed"));

    RuntimeException thrown = assertThrows(
        RuntimeException.class,
        () -> repositoryImpl.save(modelMock)
    );

    assertEquals("Database save failed", thrown.getMessage());
    verify(mapper).toEventEntity(modelMock);
    verify(entityRepository).save(entityMock);
  }

}

package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessInstanceSequenceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessInstanceSequenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessSequenceRepositoryImplTest {

  @Mock
  private ProcessInstanceSequenceEntityRepository sequenceEntityRepository;

  @Mock
  private ProcessSequenceMapper sequenceMapper;

  @InjectMocks
  private ProcessSequenceRepositoryImpl repository;

  private ProcessInstanceSequenceEntity entity;
  private ProcessSequence model;

  @BeforeEach
  void setUp() {

    entity = new ProcessInstanceSequenceEntity();
    model = ProcessSequence.builder()
        .id(Identifier.generate())
        .name(Name.create("MySequence"))
        .prefix(Code.create("PRX"))
        .checkDigitSize((short) 2)
        .padding((short) 5)
        .dateFormat("yyyyMMdd")
        .nextNumber(1L)
        .numberIncrement((short) 1)
        .processDefinitionId(Code.create("process-123")).build();
  }

  @Test
  void testFindByProcessDefinitionId_found() {
    when(sequenceEntityRepository.findByProcessDefinitionId("process-123"))
        .thenReturn(Optional.of(entity));
    when(sequenceMapper.toModel(entity)).thenReturn(model);

    Optional<ProcessSequence> result = repository.findByProcessDefinitionId("process-123");

    assertTrue(result.isPresent());
    assertEquals("MySequence", result.get().getName().getValue());
    verify(sequenceEntityRepository).findByProcessDefinitionId("process-123");
    verify(sequenceMapper).toModel(entity);
  }

  @Test
  void testFindByProcessDefinitionId_notFound() {
    when(sequenceEntityRepository.findByProcessDefinitionId("process-123"))
        .thenReturn(Optional.empty());

    Optional<ProcessSequence> result = repository.findByProcessDefinitionId("process-123");

    assertTrue(result.isEmpty());
    verify(sequenceEntityRepository).findByProcessDefinitionId("process-123");
    verify(sequenceMapper, never()).toModel(any(ProcessInstanceSequenceEntity.class));
  }

  @Test
  void testSave_success() {
    when(sequenceMapper.toEntity(model)).thenReturn(entity);
    when(sequenceEntityRepository.save(entity)).thenReturn(entity);
    when(sequenceMapper.toModel(entity)).thenReturn(model);

    ProcessSequence result = repository.save(model);

    assertNotNull(result);
    assertEquals("MySequence", result.getName().getValue());
    verify(sequenceMapper).toEntity(model);
    verify(sequenceEntityRepository).save(entity);
    verify(sequenceMapper).toModel(entity);
  }

  @Test
  void testFindByProcessDefinitionIdForUpdate_found() {
    when(sequenceEntityRepository.findByProcessDefinitionIdForUpdate("process-123"))
        .thenReturn(Optional.of(entity));
    when(sequenceMapper.toModel(entity)).thenReturn(model);

    Optional<ProcessSequence> result = repository.findByProcessDefinitionIdForUpdate("process-123");

    assertTrue(result.isPresent());
    assertEquals("MySequence", result.get().getName().getValue());
    verify(sequenceEntityRepository).findByProcessDefinitionIdForUpdate("process-123");
    verify(sequenceMapper).toModel(entity);
  }


  @Test
  void testFindByProcessDefinitionIdForUpdate_notFound() {
    when(sequenceEntityRepository.findByProcessDefinitionIdForUpdate("process-123"))
        .thenReturn(Optional.empty());

    Optional<ProcessSequence> result = repository.findByProcessDefinitionIdForUpdate("process-123");

    assertTrue(result.isEmpty());
    verify(sequenceEntityRepository).findByProcessDefinitionIdForUpdate("process-123");
    verify(sequenceMapper, never()).toModel(any(ProcessInstanceSequenceEntity.class));
  }
}

package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessSequenceRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
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
class ProcessSequenceServiceTest {

  @InjectMocks
  private ProcessSequenceService processSequenceService;

  @Mock
  private ProcessSequenceRepository processSequenceRepository;

  private Code processDefinitionId;
  private ProcessSequence sequence;

  @BeforeEach
  void setUp() {
    processDefinitionId = Code.create("process-123");
    sequence = mock(ProcessSequence.class);
  }


  @Test
  void testGetSequenceByProcessDefinitionId_found() {
    when(processSequenceRepository.findByProcessDefinitionId(processDefinitionId.getValue()))
        .thenReturn(Optional.of(sequence));

    ProcessSequence result = processSequenceService.getSequenceByProcessDefinitionId(processDefinitionId);

    assertNotNull(result);
    assertEquals(sequence, result);
    verify(processSequenceRepository).findByProcessDefinitionId(processDefinitionId.getValue());
  }


  @Test
  void testGetSequenceByProcessDefinitionId_notFound() {
    when(processSequenceRepository.findByProcessDefinitionId(processDefinitionId.getValue()))
        .thenReturn(Optional.empty());

    IgrpResponseStatusException ex = assertThrows(IgrpResponseStatusException.class,
        () -> processSequenceService.getSequenceByProcessDefinitionId(processDefinitionId));

    assertTrue(ex.getMessage().contains("Process Sequence not found"));
    verify(processSequenceRepository).findByProcessDefinitionId(processDefinitionId.getValue());
  }


  @Test
  void testSave_newSequence() {
    // Simula que não existe sequência no banco
    when(processSequenceRepository.findByProcessDefinitionIdForUpdate(processDefinitionId.getValue()))
        .thenReturn(Optional.empty());
    when(sequence.getProcessDefinitionId()).thenReturn(processDefinitionId);
    when(sequence.newInstance()).thenReturn(sequence);
    doNothing().when(sequence).validate();
    when(processSequenceRepository.save(sequence)).thenReturn(sequence);

    ProcessSequence result = processSequenceService.save(sequence);

    assertNotNull(result);
    verify(sequence).validate();
    verify(processSequenceRepository).save(sequence);
  }


  @Test
  void testGetGeneratedProcessNumberByProcessDefinitionId() {
    ProcessNumber processNumber = mock(ProcessNumber.class);

    when(processSequenceRepository.findByProcessDefinitionIdForUpdate(processDefinitionId.getValue()))
        .thenReturn(Optional.of(sequence));
    when(sequence.generateNextProcessNumberAndIncrement()).thenReturn(processNumber);
    when(processSequenceRepository.save(sequence)).thenReturn(sequence);

    ProcessNumber result = processSequenceService.getGeneratedProcessNumberByProcessDefinitionId(processDefinitionId);

    assertNotNull(result);
    assertEquals(processNumber, result);
    verify(sequence).generateNextProcessNumberAndIncrement();
    verify(processSequenceRepository).save(sequence);
  }


}

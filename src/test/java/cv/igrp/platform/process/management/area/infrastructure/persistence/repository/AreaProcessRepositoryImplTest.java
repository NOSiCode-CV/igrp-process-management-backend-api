package cv.igrp.platform.process.management.area.infrastructure.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.*;
import java.util.UUID;

import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaProcessEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.AreaProcessEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class AreaProcessRepositoryImplTest {

  @Mock
  private AreaProcessEntityRepository areaProcessEntityRepository;

  private AreaProcessRepositoryImpl areaProcessRepository;

  private AreaProcess areaProcess;
  private AreaProcessEntity areaProcessEntity;

  @BeforeEach
  void setUp() {
    AreaProcessMapper areaProcessMapper = new AreaProcessMapper();
    areaProcessRepository = new AreaProcessRepositoryImpl(areaProcessEntityRepository, areaProcessMapper);

    UUID areaId = UUID.randomUUID();
    UUID processId = UUID.randomUUID();

    areaProcess = AreaProcess.builder()
        .id(Identifier.create(processId))
        .areaId(Identifier.create(areaId))
        .processKey(Code.create("PROC_KEY"))
        .releaseId(Code.create("REL_1"))
        .version("1.0")
        .status(Status.ACTIVE)
        .name("Process Name")
        .build();

    AreaEntity areaEntity = new AreaEntity();
    areaEntity.setId(areaId);
    areaProcessEntity = new AreaProcessEntity();
    areaProcessEntity.setId(processId);
    areaProcessEntity.setAreaId(areaEntity);
    areaProcessEntity.setProcReleaseKey("PROC_KEY");
    areaProcessEntity.setProcReleaseId("REL_1");
    areaProcessEntity.setVersion("1.0");
    areaProcessEntity.setStatus(Status.ACTIVE);
    areaProcessEntity.setName("Process Name");
  }

  @Test
  void testSave() {
    when(areaProcessEntityRepository.save(any())).thenReturn(areaProcessEntity);

    AreaProcess result = areaProcessRepository.save(areaProcess);

    assertNotNull(result);
    assertEquals(areaProcess.getName(), result.getName());
    verify(areaProcessEntityRepository).save(any(AreaProcessEntity.class));
  }

  @Test
  void testFindAll() {
    PageRequest pageRequest = PageRequest.of(0, 10);
    Page<AreaProcessEntity> page = new PageImpl<>(List.of(areaProcessEntity), pageRequest, 1);

    when(areaProcessEntityRepository.findAll(any(Specification.class), eq(pageRequest)))
        .thenReturn(page);

    AreaProcessFilter filter = AreaProcessFilter.builder()
        .page(0)
        .size(10)
        .build();

    PageableLista<AreaProcess> result = areaProcessRepository.findAll(filter);

    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("Process Name", result.getContent().getFirst().getName());
  }

  @Test
  void testFindById_found() {
    when(areaProcessEntityRepository.findById(any(UUID.class))).thenReturn(Optional.of(areaProcessEntity));

    Optional<AreaProcess> result = areaProcessRepository.findById(UUID.randomUUID());

    assertTrue(result.isPresent());
    assertEquals("Process Name", result.get().getName());
  }

  @Test
  void testFindById_notFound() {
    when(areaProcessEntityRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    Optional<AreaProcess> result = areaProcessRepository.findById(UUID.randomUUID());

    assertFalse(result.isPresent());
  }

  @Test
  void testUpdateStatus_found() {
    when(areaProcessEntityRepository.findById(any(UUID.class))).thenReturn(Optional.of(areaProcessEntity));

    areaProcessRepository.updateStatus(areaProcessEntity.getId(), Status.INACTIVE);

    assertEquals(Status.INACTIVE, areaProcessEntity.getStatus());
    verify(areaProcessEntityRepository).save(areaProcessEntity);
  }

  @Test
  void testUpdateStatus_notFound() {
    when(areaProcessEntityRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(IgrpResponseStatusException.class, () ->
        areaProcessRepository.updateStatus(UUID.randomUUID(), Status.INACTIVE));
  }
}


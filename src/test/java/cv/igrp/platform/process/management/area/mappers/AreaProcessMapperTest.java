package cv.igrp.platform.process.management.area.mappers;

import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionListPageDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaProcessEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.UUID;

class AreaProcessMapperTest {

  private AreaProcessMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new AreaProcessMapper();
  }

  @Test
  void testToModel_fromRequestDTO() {
    ProcessDefinitionRequestDTO dto = new ProcessDefinitionRequestDTO();
    dto.setProcessKey("procKey");
    dto.setReleaseId("releaseId");
    dto.setVersion("2");
    dto.setName("processName");

    AreaProcess model = mapper.toModel(dto);

    assertNotNull(model);
    assertEquals("procKey", model.getProcessKey().getValue());
    assertEquals("releaseId", model.getReleaseId().getValue());
    assertEquals("2", model.getVersion());
    assertEquals("processName", model.getName());
  }

  @Test
  void testToDTO_fromModel() {
    AreaProcess model = AreaProcess.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .areaId(Identifier.create(UUID.randomUUID()))
        .processKey(Code.create("procKey"))
        .releaseId(Code.create("releaseId"))
        .version("3")
        .status(Status.ACTIVE)
        .name("myProcess")
        .build();

    ProcessDefinitionDTO dto = mapper.toDTO(model);

    assertNotNull(dto);
    assertEquals(model.getId().getValue(), dto.getId());
    assertEquals(model.getAreaId().getValue(), dto.getAreaId());
    assertEquals(model.getProcessKey().getValue(), dto.getProcessKey());
    assertEquals(model.getReleaseId().getValue(), dto.getReleaseId());
    assertEquals(model.getVersion(), dto.getVersion());
    assertEquals(model.getStatus(), dto.getStatus());
    assertEquals(model.getName(), dto.getName());
  }

  @Test
  void testToModel_fromEntity() {
    AreaProcessEntity entity = new AreaProcessEntity();
    entity.setId(UUID.randomUUID());
    AreaEntity areaEntity = new AreaEntity();
    areaEntity.setId(UUID.randomUUID());
    entity.setAreaId(areaEntity);
    entity.setProcReleaseKey("key123");
    entity.setProcReleaseId("rel123");
    entity.setVersion("5");
    entity.setStatus(Status.INACTIVE);
    entity.setName("processName");

    AreaProcess model = mapper.toModel(entity);

    assertNotNull(model);
    assertEquals(entity.getId(), model.getId().getValue());
    assertEquals(entity.getAreaId().getId(), model.getAreaId().getValue());
    assertEquals(entity.getProcReleaseKey(), model.getProcessKey().getValue());
    assertEquals(entity.getProcReleaseId(), model.getReleaseId().getValue());
    assertEquals(entity.getVersion(), model.getVersion());
    assertEquals(entity.getStatus(), model.getStatus());
    assertEquals(entity.getName(), model.getName());
  }

  @Test
  void testToEntity_fromModel() {
    AreaProcess model = AreaProcess.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .areaId(Identifier.create(UUID.randomUUID()))
        .processKey(Code.create("procKey"))
        .releaseId(Code.create("releaseId"))
        .version("7")
        .status(Status.ACTIVE)
        .name("modelName")
        .build();

    AreaProcessEntity entity = mapper.toEntity(model);

    assertNotNull(entity);
    assertEquals(model.getId().getValue(), entity.getId());
    assertEquals(model.getStatus(), entity.getStatus());
    assertEquals(model.getProcessKey().getValue(), entity.getProcReleaseKey());
    assertEquals(model.getReleaseId().getValue(), entity.getProcReleaseId());
    assertEquals(model.getVersion(), entity.getVersion());
    assertEquals(model.getName(), entity.getName());
    assertNotNull(entity.getAreaId());
    assertEquals(model.getAreaId().getValue(), entity.getAreaId().getId());
  }

  @Test
  void testToDTO_pageableList() {
    AreaProcess model = AreaProcess.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .processKey(Code.create("procKey"))
        .releaseId(Code.create("releaseId"))
        .version("1")
        .status(Status.ACTIVE)
        .name("procName")
        .build();

    PageableLista<AreaProcess> pageable = PageableLista.<AreaProcess>builder()
        .pageNumber(0)
        .pageSize(10)
        .totalElements(1L)
        .totalPages(1)
        .first(true)
        .last(true)
        .content(List.of(model))
        .build();

    ProcessDefinitionListPageDTO dto = mapper.toDTO(pageable);

    assertNotNull(dto);
    assertEquals(pageable.getPageNumber(), dto.getPageNumber());
    assertEquals(pageable.getPageSize(), dto.getPageSize());
    assertEquals(pageable.getTotalElements(), dto.getTotalElements());
    assertEquals(pageable.getTotalPages(), dto.getTotalPages());
    assertEquals(pageable.isFirst(), dto.isFirst());
    assertEquals(pageable.isLast(), dto.isLast());
    assertEquals(1, dto.getContent().size());
    assertEquals(model.getProcessKey().getValue(), dto.getContent().getFirst().getProcessKey());
  }
}

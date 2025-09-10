package cv.igrp.platform.process.management.area.mappers;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaListPageDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AreaMapperTest {

  private AreaProcessMapper areaProcessMapper;  // real instance
  private AreaMapper areaMapper;

  @BeforeEach
  void setUp() {
    areaProcessMapper = new AreaProcessMapper();
    areaMapper = new AreaMapper(areaProcessMapper);
  }

  @Test
  void testToModel_fromRequestDTO() {
    AreaRequestDTO dto = new AreaRequestDTO();
    dto.setCode("code1");
    dto.setName("name1");
    dto.setApplicationBase("appBase");
    dto.setParentId(null);
    dto.setDescription("desc");

    Area area = areaMapper.toModel(dto);

    assertNotNull(area);
    assertEquals("code1", area.getCode().getValue());
    assertEquals("name1", area.getName().getValue());
    assertEquals("appBase", area.getApplicationBase().getValue());
    assertNull(area.getAreaId());
    assertEquals("desc", area.getDescription());
  }

  @Test
  void testToEntity_and_backToModel() {
    Area area = Area.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .code(Code.create("code2"))
        .name(Name.create("name2"))
        .applicationBase(Code.create("appBase2"))
        .description("desc2")
        .status(Status.ACTIVE)
        .areaId(Identifier.create(UUID.randomUUID()))
        .build();

    AreaEntity entity = areaMapper.toEntity(area);

    assertNotNull(entity);
    assertEquals(area.getCode().getValue(), entity.getCode());
    assertEquals(area.getName().getValue(), entity.getName());
    assertEquals(area.getApplicationBase().getValue(), entity.getApplicationBase());
    assertEquals(area.getDescription(), entity.getDescription());
    assertEquals(area.getStatus(), entity.getStatus());
    assertNotNull(entity.getAreaId());
    assertEquals(area.getAreaId().getValue(), entity.getAreaId().getId());

    Area mappedBack = areaMapper.toModel(entity);
    assertEquals(area.getCode().getValue(), mappedBack.getCode().getValue());
    assertEquals(area.getName().getValue(), mappedBack.getName().getValue());
    assertEquals(area.getApplicationBase().getValue(), mappedBack.getApplicationBase().getValue());
    assertEquals(area.getDescription(), mappedBack.getDescription());
    assertEquals(area.getStatus(), mappedBack.getStatus());
    assertNotNull(mappedBack.getAreaId());
    assertEquals(entity.getAreaId().getId(), mappedBack.getAreaId().getValue());
  }

  @Test
  void testToDTO_includesProcessDefinitions() {
    AreaProcess process = AreaProcess.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .processKey(Code.create("procKey"))
        .releaseId(Code.create("releaseId"))
        .version("1.0")
        .name("processName")
        .status(Status.ACTIVE)
        .build();

    Area area = Area.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .code(Code.create("code3"))
        .name(Name.create("name3"))
        .applicationBase(Code.create("appBase3"))
        .status(Status.ACTIVE)
        .build();
    area.add(process);

    AreaDTO dto = areaMapper.toDTO(area);

    assertNotNull(dto);
    assertEquals(area.getId().getValue(), dto.getId());
    assertEquals(area.getName().getValue(), dto.getName());
    assertEquals(1, dto.getProcess().size());
    assertEquals("procKey", dto.getProcess().getFirst().getProcessKey());
  }

  @Test
  void testToDTO_handlesNullProcessList() {
    Area area = Area.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .code(Code.create("code4"))
        .name(Name.create("name4"))
        .applicationBase(Code.create("appBase4"))
        .status(Status.ACTIVE)
        .build();

    // no processes added
    AreaDTO dto = areaMapper.toDTO(area);

    assertNotNull(dto);
    assertNotNull(dto.getProcess());
    assertTrue(dto.getProcess().isEmpty());
  }

  @Test
  void testToDTOPage() {
    Area area = Area.builder()
        .id(Identifier.create(UUID.randomUUID()))
        .code(Code.create("code5"))
        .name(Name.create("name5"))
        .applicationBase(Code.create("appBase5"))
        .status(Status.ACTIVE)
        .build();

    PageableLista<Area> page = PageableLista.<Area>builder()
        .pageNumber(0)
        .pageSize(10)
        .totalElements(1L)
        .totalPages(1)
        .first(true)
        .last(true)
        .content(List.of(area))
        .build();

    AreaListPageDTO pageDTO = areaMapper.toDTO(page);

    assertEquals(page.getPageNumber(), pageDTO.getPageNumber());
    assertEquals(page.getPageSize(), pageDTO.getPageSize());
    assertEquals(page.getTotalElements(), pageDTO.getTotalElements());
    assertEquals(page.getTotalPages(), pageDTO.getTotalPages());
    assertEquals(page.isFirst(), pageDTO.isFirst());
    assertEquals(page.isLast(), pageDTO.isLast());
    assertEquals(1, pageDTO.getContent().size());
    assertEquals(area.getName().getValue(), pageDTO.getContent().getFirst().getName());
  }
}


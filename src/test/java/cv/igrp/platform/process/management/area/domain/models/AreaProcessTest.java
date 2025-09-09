package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AreaProcessTest {

  private Code processKey;
  private Code releaseId;

  @BeforeEach
  void setUp() {
    processKey = Code.create("Process_1");
    releaseId = Code.create("invoicing_v12342");
  }

  @Test
  void constructor_shouldApplyDefaults() {
    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .build();

    assertNotNull(process.getId());
    assertEquals(Status.ACTIVE, process.getStatus());
    assertNotNull(process.getCreatedAt());
    assertEquals("Process - " + releaseId.getValue(), process.getName());
  }

  @Test
  void constructor_shouldThrowWhenProcessKeyIsNull() {
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        AreaProcess.builder()
            .releaseId(releaseId)
            .build()
    );
    assertEquals("The process key cannot be null", ex.getMessage());
  }

  @Test
  void constructor_shouldThrowWhenReleaseIdIsNull() {
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        AreaProcess.builder()
            .processKey(processKey)
            .build()
    );
    assertEquals("The process release id cannot be null", ex.getMessage());
  }

  @Test
  void constructor_shouldUseProvidedValuesInsteadOfDefaults() {
    Identifier id = Identifier.generate();
    Identifier areaId = Identifier.generate();
    LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 12, 0);
    LocalDateTime removedAt = LocalDateTime.of(2024, 1, 1, 12, 0);

    AreaProcess process = AreaProcess.builder()
        .id(id)
        .processKey(processKey)
        .releaseId(releaseId)
        .areaId(areaId)
        .status(Status.INACTIVE)
        .version("1.0")
        .createdAt(createdAt)
        .createdBy("demo@nosi.cv")
        .removedAt(removedAt)
        .removedBy("demo@nosi.cv")
        .name("Invoicing Process")
        .build();

    assertEquals(id, process.getId());
    assertEquals(areaId, process.getAreaId());
    assertEquals(Status.INACTIVE, process.getStatus());
    assertEquals("1.0", process.getVersion());
    assertEquals(createdAt, process.getCreatedAt());
    assertEquals("demo@nosi.cv", process.getCreatedBy());
    assertEquals(removedAt, process.getRemovedAt());
    assertEquals("demo@nosi.cv", process.getRemovedBy());
    assertEquals("Invoicing Process", process.getName());
  }

  @Test
  void bindToArea_shouldSetAreaId() {
    Area area = mock(Area.class);
    Identifier areaId = Identifier.generate();
    when(area.getId()).thenReturn(areaId);

    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .build();

    process.bindToArea(area);

    assertEquals(areaId, process.getAreaId());
  }

  @Test
  void bindToArea_shouldThrowWhenAreaIsNull() {
    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .build();

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> process.bindToArea(null));
    assertEquals("The area must not be null", ex.getMessage());
  }

  @Test
  void unbindFromArea_shouldClearAreaIdAndSetInactive() {
    Area area = mock(Area.class);
    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .areaId(Identifier.generate())
        .build();

    process.unbindFromArea(area);

    assertNull(process.getAreaId());
    assertEquals(Status.INACTIVE, process.getStatus());
  }

  @Test
  void unbindFromArea_shouldThrowWhenAreaIsNull() {
    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .build();

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> process.unbindFromArea(null));
    assertEquals("The area must not be null", ex.getMessage());
  }

  @Test
  void reActivate_shouldSetStatusActive() {
    AreaProcess process = AreaProcess.builder()
        .processKey(processKey)
        .releaseId(releaseId)
        .status(Status.INACTIVE)
        .build();

    process.reActivate();

    assertEquals(Status.ACTIVE, process.getStatus());
  }

}

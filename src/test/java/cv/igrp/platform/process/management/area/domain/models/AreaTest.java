package cv.igrp.platform.process.management.area.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.Status;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AreaTest {

  private Code code;
  private Code appBase;

  @BeforeEach
  void setUp() {
    code = Code.create("area_1234");
    appBase = Code.create("igrp-app");
  }

  @Test
  void constructor_shouldApplyDefaults() {
    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .build();

    assertNotNull(area.getId());
    assertEquals(Status.ACTIVE, area.getStatus());
    assertNotNull(area.getProcess());
    assertTrue(area.getProcess().isEmpty());
    assertNotNull(area.getCreatedAt());
  }

  @Test
  void constructor_shouldThrowWhenCodeIsNull() {
    Exception ex = assertThrows(NullPointerException.class, () ->
        Area.builder()
            .applicationBase(appBase)
            .build()
    );
    assertEquals("The code of the area must not be null", ex.getMessage());
  }

  @Test
  void constructor_shouldThrowWhenApplicationBaseIsNull() {
    Exception ex = assertThrows(NullPointerException.class, () ->
        Area.builder()
            .code(code)
            .build()
    );
    assertEquals("The application code must not be null", ex.getMessage());
  }

  @Test
  void add_shouldAddProcessAndBind() {
    AreaProcess process = mock(AreaProcess.class);
    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .process(new ArrayList<>())
        .build();

    area.add(process);

    assertEquals(1, area.getProcess().size());
    assertTrue(area.getProcess().contains(process));
    verify(process).bindToArea(area);
  }

  @Test
  void add_shouldThrowWhenProcessIsNull() {
    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .build();

    assertThrows(IllegalArgumentException.class, () -> area.add(null));
  }

  @Test
  void remove_shouldRemoveAndUnbind() {

    Identifier id1 = Identifier.generate();

    AreaProcess process = mock(AreaProcess.class);
    when(process.getId()).thenReturn(id1);

    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .process(new ArrayList<>(List.of(process)))
        .build();

    area.remove(process);

    assertTrue(area.getProcess().isEmpty());
    verify(process).unbindFromArea(area);
  }

  @Test
  void remove_shouldNotUnbindWhenNotFound() {

    Identifier id1 = Identifier.generate();
    Identifier id2 = Identifier.generate();

    AreaProcess processInArea = mock(AreaProcess.class);
    when(processInArea.getId()).thenReturn(id1);

    AreaProcess processToRemove = mock(AreaProcess.class);
    when(processToRemove.getId()).thenReturn(id2);

    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .process(new ArrayList<>(List.of(processInArea)))
        .build();

    area.remove(processToRemove);

    assertEquals(1, area.getProcess().size());
    verify(processToRemove, never()).unbindFromArea(any());
  }

  @Test
  void getProcess_shouldReturnCorrectProcess() {
    Code targetCode = Code.create("Process_1");
    AreaProcess process = mock(AreaProcess.class);
    when(process.getReleaseId()).thenReturn(targetCode);

    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .process(new ArrayList<>(List.of(process)))
        .build();

    Optional<AreaProcess> result = area.getProcess(targetCode);

    assertTrue(result.isPresent());
    assertEquals(process, result.get());
  }

  @Test
  void getProcess_shouldReturnEmptyWhenNotFound() {
    Code targetCode = Code.create("Process_1");

    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .process(new ArrayList<>())
        .build();

    Optional<AreaProcess> result = area.getProcess(targetCode);

    assertTrue(result.isEmpty());
  }

  @Test
  void delete_shouldSetStatusInactive() {
    Area area = Area.builder()
        .code(code)
        .applicationBase(appBase)
        .build();

    area.delete();

    assertEquals(Status.INACTIVE, area.getStatus());
  }

  @Test
  void update_shouldReplaceOnlyNonNullAndNonBlankFields() {
    Identifier oldAreaId = Identifier.generate();
    Area area = Area.builder()
        .code(code)
        .name(Name.create("Old Area 1"))
        .applicationBase(appBase)
        .description("Old Area 1 Desc")
        .areaId(oldAreaId)
        .build();

    Area newArea = Area.builder()
        .code(Code.create("Area_1"))
        .name(Name.create("Area 1"))
        .applicationBase(Code.create("igrp-app"))
        .description("Area 1")
        .build();

    area.update(newArea);

    assertEquals(newArea.getCode().getValue(), area.getCode().getValue());
    assertEquals(newArea.getName().getValue(), area.getName().getValue());
    assertEquals(newArea.getApplicationBase().getValue(), area.getApplicationBase().getValue());
    assertEquals(newArea.getDescription(), area.getDescription());
    assertEquals(oldAreaId, area.getAreaId());
  }

}

package cv.igrp.platform.process.management.shared.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNameTest {

  @Test
  void shouldCreateResourceNameWhenValid() {
    ResourceName resource = ResourceName.create("myProcess.bpmn20.xml");
    assertNotNull(resource);
    assertEquals("myProcess.bpmn20.xml", resource.getValue());
  }

  @Test
  void shouldThrowExceptionWhenValueIsNull() {
    Exception exception = assertThrows(NullPointerException.class, () -> {
      ResourceName.create(null);
    });
    assertEquals("Resource name cannot be null", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenValueIsInvalid() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ResourceName.create("invalidFile.txt");
    });
    assertEquals("Invalid BPMN resource file name", exception.getMessage());
  }

  @Test
  void shouldBeEqualForSameValue() {
    ResourceName r1 = ResourceName.create("process.bpmn20.xml");
    ResourceName r2 = ResourceName.create("process.bpmn20.xml");
    assertEquals(r1, r2);
    assertEquals(r1.hashCode(), r2.hashCode());
  }

  @Test
  void shouldHaveToStringContainingValue() {
    ResourceName resource = ResourceName.create("example.bpmn20.xml");
    String toString = resource.toString();
    assertTrue(toString.contains("example.bpmn20.xml"));
  }

}

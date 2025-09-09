package cv.igrp.platform.process.management.processdefinition.domain.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BpmnXmlTest {

  @Test
  void shouldCreateBpmnXmlWithValidXml() {
    String xmlContent = """
          <definitions id="sample">...</definitions>
        """;

    BpmnXml bpmnXml = BpmnXml.create(xmlContent);

    assertNotNull(bpmnXml);
    assertEquals(xmlContent, bpmnXml.getXml());
  }

  @Test
  void shouldThrowExceptionWhenXmlIsNull() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> BpmnXml.create(null));

    assertEquals("The value of BpmnXml cannot be null or blank", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenXmlIsBlank() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> BpmnXml.create("   "));

    assertEquals("The value of BpmnXml cannot be null or blank", exception.getMessage());
  }

}

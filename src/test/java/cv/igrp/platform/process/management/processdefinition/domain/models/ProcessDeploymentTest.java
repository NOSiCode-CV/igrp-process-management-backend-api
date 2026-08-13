package cv.igrp.platform.process.management.processdefinition.domain.models;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import org.junit.jupiter.api.Test;


class ProcessDeploymentTest {

  @Test
  void constructor_shouldThrow_whenKeyIsNull() {
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        ProcessDeployment.builder()
            .applicationBase(Code.create("igrp-app"))
            .build()
    );
    assertEquals("Key cannot be null", ex.getMessage());
  }

  @Test
  void constructor_shouldThrow_whenApplicationBaseIsNull() {
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        ProcessDeployment.builder()
            .key(Code.create("PROCESS_KEY"))
            .build()
    );
    assertEquals("Application Code cannot be null", ex.getMessage());
  }

  @Test
  void deploy_shouldThrow_whenBpmnXmlIsNull() {
    ProcessDeployment deployment = ProcessDeployment.builder()
        .key(Code.create("PROCESS_KEY"))
        .applicationBase(Code.create("igrp-app"))
        .resourceName(ResourceName.create("invoicing.bpmn20.xml"))
        .bpmnXml(null)
        .build();

    IllegalStateException ex = assertThrows(IllegalStateException.class, deployment::deploy);
    assertEquals("BPMN xml cannot be null", ex.getMessage());
  }

  @Test
  void deploy_shouldThrow_whenResourceNameIsNull() {
    ProcessDeployment deployment = ProcessDeployment.builder()
        .key(Code.create("PROCESS_KEY"))
        .applicationBase(Code.create("igrp-app"))
        .bpmnXml(BpmnXml.create("<definitions />"))
        .resourceName(null)
        .build();

    IllegalStateException ex = assertThrows(IllegalStateException.class, deployment::deploy);
    assertEquals("ResourceName cannot be null", ex.getMessage());
  }

  @Test
  void deploy_shouldMarkAsDeployed_andSetDeployedAt_whenAllRequiredFieldsPresent() {
    ProcessDeployment deployment = ProcessDeployment.builder()
        .key(Code.create("PROCESS_KEY"))
        .applicationBase(Code.create("igrp-app"))
        .resourceName(ResourceName.create("invoicing.bpmn20.xml"))
        .bpmnXml(BpmnXml.create("<definitions />"))
        .build();

    LocalDateTime before = LocalDateTime.now();
    deployment.deploy();
    LocalDateTime after = LocalDateTime.now();

    assertTrue(deployment.isDeployed(), "deployed flag should be true after deploy()");
    assertNotNull(deployment.getDeployedAt(), "deployedAt must be set");

    assertFalse(deployment.getDeployedAt().isBefore(before),
        "deployedAt should not be before the deploy start time");
    assertFalse(deployment.getDeployedAt().isAfter(after),
        "deployedAt should not be after the test time");
  }

}


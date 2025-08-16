package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;


import cv.igrp.platform.process.management.IgrpPlatformProcessManagementApplication;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IgrpPlatformProcessManagementApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ProcessDeploymentRepositoryImplIntegrationTest {

  @Autowired
  private ProcessDeploymentRepository processDeploymentRepository;

  private ProcessDeployment deployment;

  @BeforeEach
  public void beforeEach() throws Exception {
    // Arrange
    String bpmnXml = Files.readString(Paths.get(getClass().getClassLoader()
        .getResource("bpmn/sample.bpmn20.xml").toURI()));

    deployment = ProcessDeployment.builder()
        .key(Code.create("deployment_process_key"))
        .name(Name.create("Invoicing sample"))
        .resourceName(ResourceName.create("sample.bpmn20.xml"))
        .bpmnXml(BpmnXml.create(bpmnXml))
        .applicationBase(Code.create("igrp-app"))
        .build();
  }

  @Test
  public void deploy() throws Exception {

    // Act
    ProcessDeployment deployedProcess = processDeploymentRepository.deploy(deployment);

    // Assert
    assertNotNull(deployedProcess);
    assertEquals("deployment_process_key", deployedProcess.getKey().getValue());
    assertEquals("Invoicing sample", deployedProcess.getName().getValue());
    assertEquals("sample.bpmn20.xml", deployedProcess.getResourceName().getValue());
    assertNotNull(deployedProcess.getDeploymentId());
    assertTrue(deployedProcess.isDeployed());

  }

  @Test
  public void findAll(){
    // Arrange
    ProcessDeploymentFilter filter = ProcessDeploymentFilter.builder()
        .build();

    // Act
    processDeploymentRepository.deploy(deployment);

    PageableLista<ProcessDeployment> result = processDeploymentRepository.findAll(filter);

    // Asserts
    assertNotNull(result);
    assertEquals(1, result.getContent().size());

  }

}

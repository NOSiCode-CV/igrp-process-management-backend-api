package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.exception.ProcessDeploymentException;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import cv.nosi.igrp.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessDeploymentRepositoryImplTest {

  @Mock
  private ProcessDefinitionAdapter processDefinitionAdapter;

  @Mock
  private ProcessManagerAdapter processManagerAdapter;

  private ProcessDeploymentRepositoryImpl repository;

  private ProcessDeployment deployment;

  @BeforeEach
  void setUp() {
    ProcessDeploymentMapper processDeploymentMapper = new ProcessDeploymentMapper();
    repository = new ProcessDeploymentRepositoryImpl(
        processDefinitionAdapter,
        processDeploymentMapper,
        processManagerAdapter
    );
    deployment = ProcessDeployment.builder()
        .key(Code.create("deployment_process_key"))
        .name(Name.create("Invoicing sample"))
        .resourceName(ResourceName.create("invoicing.bpmn20.xml"))
        .bpmnXml(BpmnXml.create("<definitions />"))
        .applicationBase(Code.create("igrp-app"))
        .build();
  }

  @Test
  void deploy_shouldReturnMappedModel_whenAdapterSucceeds() {
    // Arrange
    IgrpProcessDefinitionRepresentation representation =
        IgrpProcessDefinitionRepresentation.builder()
            .key("deployment_process_key")
            .name("Invoicing sample")
            .resourceName("invoicing.bpmn20.xml")
            .bpmnXml("<definitions />")
            .applicationBase("igrp-app")
            .deploymentId("12345678")
            .version("1.0")
            .deployed(true)
            .build();

    when(processDefinitionAdapter.deploy(any(IgrpProcessDefinitionRepresentation.class)))
        .thenReturn(representation);

    // Act
    ProcessDeployment result = repository.deploy(deployment);

    // Assert
    assertNotNull(result);
    assertEquals("deployment_process_key", result.getKey().getValue());
    assertEquals("Invoicing sample", result.getName().getValue());
    assertEquals("invoicing.bpmn20.xml", result.getResourceName().getValue());
    assertEquals("12345678", result.getDeploymentId());
    assertTrue(result.isDeployed());

    verify(processDefinitionAdapter).deploy(any(IgrpProcessDefinitionRepresentation.class));
  }

  @Test
  void deploy_shouldThrowProcessDeploymentException_whenAdapterThrows() {

    // When
    when(processDefinitionAdapter.deploy(any(IgrpProcessDefinitionRepresentation.class)))
        .thenThrow(new RuntimeException("Deployment failed"));

    // Act
    ProcessDeploymentException ex = assertThrows(
        ProcessDeploymentException.class,
        () -> repository.deploy(deployment)
    );

    // asserts
    assertTrue(ex.getMessage().contains("Failed to deploy process deployment: " + deployment.getKey().getValue()));

    verify(processDefinitionAdapter).deploy(any(IgrpProcessDefinitionRepresentation.class));
  }

  @Test
  void getAllDeployments_shouldReturnMappedList() {
    // Arrange
    /*ProcessDeploymentFilter filter = ProcessDeploymentFilter.builder()
        .build();


    PageableLista<ProcessDeployment> pageableList = PageableLista.builder()
        .
        .content(List.of(deployment))
        .build();

    when(repository.findAll(filter)).thenReturn(pageableList);

    // Act
    PageableLista<ProcessDeployment> result = service.getAllDeployments(filter);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());

    ProcessDeployment item = result.getContent().get(0);
    assertEquals("key1", item.getKey().getValue());
    assertEquals("name1", item.getName().getValue());
    assertEquals("desc", item.getDescription());
    assertEquals("APP", item.getApplicationBase().getValue());

    verify(repository).findAll(filter);
    */
  }


}

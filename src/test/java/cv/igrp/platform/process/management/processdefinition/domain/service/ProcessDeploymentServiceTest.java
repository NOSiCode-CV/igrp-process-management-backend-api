package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessDeploymentServiceTest {

  @Mock
  private ProcessDeploymentRepository processDeploymentRepository;

  @InjectMocks
  private ProcessDeploymentService service;

  @Test
  void deployProcess_shouldDeployAndCallRepository() {
    // Arrange
    ProcessDeployment processDeployment = ProcessDeployment.builder()
        .key(Code.create("invoice-process-key"))
        .name(Name.create("Invoice Process"))
        .description("Invoice Process sample")
        .resourceName(ResourceName.create("invoicing.bpmn20.xml"))
        .bpmnXml(BpmnXml.create("<definitions>...</definitions>"))
        .applicationBase(Code.create("igrp-app"))
        .build();

    when(processDeploymentRepository.deploy(any(ProcessDeployment.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    ProcessDeployment result = service.deployProcess(processDeployment);

    // Asserts
    assertTrue(result.isDeployed(), "Process should be marked as deployed");
    assertNotNull(result.getDeployedAt(), "DeployedAt should be set");

    verify(processDeploymentRepository).deploy(processDeployment);
  }

  @Test
  void getAllDeployments_shouldDelegateToRepository() {
    /*
    // Arrange
    ProcessDeploymentFilter filter = ProcessDeploymentFilter.builder()
        .build();

    PageableLista<ProcessDeployment> expectedPage = mock(PageableLista.class);

    when(processDeploymentRepository.findAll(filter)).thenReturn(expectedPage);

    // Act
    PageableLista<ProcessDeployment> result = service.getAllDeployments(filter);

    // Assert
    verify(processDeploymentRepository).findAll(filter);
    assertSame(expectedPage, result);
     */
  }

}

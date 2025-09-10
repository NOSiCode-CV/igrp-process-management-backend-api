package cv.igrp.platform.process.management.processdefinition.application.queries;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ListDeploymentsQueryHandlerTest {

  @Mock
  private ProcessDeploymentService processDeploymentService;

  private ListDeploymentsQueryHandler handler;

  @BeforeEach
  void setup() {
    ProcessDeploymentMapper mapper = new ProcessDeploymentMapper();
    handler = new ListDeploymentsQueryHandler(processDeploymentService, mapper);
  }

  @Test
  void handle_ShouldReturnMappedResponse() {
    // Arrange
    ListDeploymentsQuery queryDummy = new ListDeploymentsQuery();

    ProcessDeployment processDeployment = ProcessDeployment.builder()
        .name(Name.create("Process invoicing"))
        .description("Process invoicing description")
        .key(Code.create("invoicing_key"))
        .resourceName(ResourceName.create("invoicing.bpmn20.xml"))
        .bpmnXml(BpmnXml.create("<definitions...</<definitions>"))
        .applicationBase(Code.create("igrp-app"))
        .version("1.0")
        .deploymentId("deploy-001")
        .deployed(true)
        .deployedAt(LocalDateTime.now())
        .procReleaseId(Code.create("123456"))
        .procReleaseKey(Code.create("invoicing_key"))
        .build();

    PageableLista<ProcessDeployment> pageableDeployments = PageableLista.<ProcessDeployment>builder()
            .pageNumber(0)
            .pageSize(1)
            .totalElements(1L)
            .totalPages(1)
            .first(true)
            .last(true)
            .content(List.of(processDeployment))
            .build();

    when(processDeploymentService.getAllDeployments(any(ProcessDeploymentFilter.class))).thenReturn(pageableDeployments);

    // Act
    ResponseEntity<ProcessDeploymentListPageDTO> response = handler.handle(queryDummy);

    // Asserts
    assertEquals(200, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getContent().size());
    ProcessDeploymentListDTO result = response.getBody().getContent().getFirst();
    assertEquals("Process invoicing", result.getName());
    assertEquals("Process invoicing description", result.getDescription());
    assertEquals("invoicing_key", result.getProcessKey());
    assertEquals("123456", result.getId());

    assertEquals(0, response.getBody().getPageNumber());
    assertEquals(1, response.getBody().getPageSize());
    assertEquals(1, response.getBody().getTotalElements());
    assertEquals(1, response.getBody().getTotalPages());
    assertTrue(response.getBody().isFirst());
    assertTrue(response.getBody().isLast());

    // verify
    verify(processDeploymentService).getAllDeployments(any(ProcessDeploymentFilter.class));

  }

}

package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceListaPageDTO;

@Component
public class ListProcessInstancesQueryHandler implements QueryHandler<ListProcessInstancesQuery, ResponseEntity<ProcessInstanceListaPageDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProcessInstancesQueryHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper processInstanceMapper;

  public ListProcessInstancesQueryHandler(ProcessInstanceService processInstanceService,
                                          ProcessInstanceMapper processInstanceMapper) {
    this.processInstanceService = processInstanceService;
    this.processInstanceMapper = processInstanceMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<ProcessInstanceListaPageDTO> handle(ListProcessInstancesQuery query) {
    ProcessInstanceFilter filter = ProcessInstanceFilter.builder()
        .number(query.getNumber() != null ? Code.create(query.getNumber()) : null)
        .procReleaseId(query.getProcReleaseId() != null ? Code.create(query.getProcReleaseId()) : null)
        .procReleaseKey(query.getProcReleaseKey() != null ? Code.create(query.getProcReleaseKey()) : null)
        .status(query.getStatus() != null ? ProcessInstanceStatus.valueOf(query.getStatus()) : null)
        .searchTerms(query.getSearchTerms())
        .page(query.getPage())
        .size(query.getSize())
        .build();
    PageableLista<ProcessInstance> processInstances =  processInstanceService.getAllProcessInstances(filter);
    return ResponseEntity.ok(processInstanceMapper.toDTO(processInstances));
  }

}

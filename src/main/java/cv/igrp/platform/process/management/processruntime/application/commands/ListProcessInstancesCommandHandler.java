package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.VariablesFilterDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.VariablesExpression;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceListPageDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ListProcessInstancesCommandHandler implements CommandHandler<ListProcessInstancesCommand, ResponseEntity<ProcessInstanceListPageDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProcessInstancesCommandHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper processInstanceMapper;

  public ListProcessInstancesCommandHandler(ProcessInstanceService processInstanceService,
                                            ProcessInstanceMapper processInstanceMapper) {
    this.processInstanceService = processInstanceService;
    this.processInstanceMapper = processInstanceMapper;
  }

  @Transactional(readOnly = true)
  @IgrpCommandHandler
  public ResponseEntity<ProcessInstanceListPageDTO> handle(ListProcessInstancesCommand command) {
    ProcessInstanceFilter filter = ProcessInstanceFilter.builder()
        .number(command.getNumber() != null ? Code.create(command.getNumber()) : null)
        .procReleaseId(command.getProcReleaseId() != null ? Code.create(command.getProcReleaseId()) : null)
        .procReleaseKey(command.getProcReleaseKey() != null ? Code.create(command.getProcReleaseKey()) : null)
        .status(command.getStatus() != null ? ProcessInstanceStatus.valueOf(command.getStatus()) : null)
        .applicationBase(command.getApplicationBase() != null ? Code.create(command.getApplicationBase()) : null)
        .variablesExpressions(toVariablesExpressionList(command.getVariablesfilterdto()))
        .dateFrom(DateUtil.stringToLocalDate.apply(command.getDateFrom()))
        .dateTo(DateUtil.stringToLocalDate.apply(command.getDateTo()))
        .name(command.getName() != null && !command.getName().isBlank() ? Name.create(command.getName()) : null)
        .page(command.getPage())
        .size(command.getSize())
        .build();
    PageableLista<ProcessInstance> processInstances = processInstanceService.getAllProcessInstances(filter);
    return ResponseEntity.ok(processInstanceMapper.toDTO(processInstances));
  }

  public List<VariablesExpression> toVariablesExpressionList(VariablesFilterDTO dto){
    return dto.getVariables().stream()
        .map(variablesExpressionDTO -> VariablesExpression.builder()
            .name(variablesExpressionDTO.getName())
            .operator(variablesExpressionDTO.getOperator())
            .value(variablesExpressionDTO.getValue())
            .build()
        ).toList();
  }

}

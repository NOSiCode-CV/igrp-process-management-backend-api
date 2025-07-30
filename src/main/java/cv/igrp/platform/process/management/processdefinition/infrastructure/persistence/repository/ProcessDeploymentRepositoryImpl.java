package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.nosi.igrp.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.nosi.igrp.runtime.core.engine.process.ProcessManagerAdapter;
import cv.nosi.igrp.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessDefinition;
import cv.nosi.igrp.runtime.core.engine.process.model.ProcessFilter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcessDeploymentRepositoryImpl implements ProcessDeploymentRepository {

  private final ProcessDefinitionAdapter processDefinitionAdapter;

  private  final ProcessDeploymentMapper processDeploymentMapper;

  private final ProcessManagerAdapter processManagerAdapter;

  public ProcessDeploymentRepositoryImpl(ProcessDefinitionAdapter processDefinitionAdapter, ProcessDeploymentMapper processDeploymentMapper, ProcessManagerAdapter processManagerAdapter) {
    this.processDefinitionAdapter = processDefinitionAdapter;
    this.processDeploymentMapper = processDeploymentMapper;
    this.processManagerAdapter = processManagerAdapter;
  }

  @Override
  public ProcessDeployment deploy(ProcessDeployment processDeployment) {

    IgrpProcessDefinitionRepresentation processDefinitionRepresentation = IgrpProcessDefinitionRepresentation.builder()
        .key(processDeployment.getKey().getValue())
        .name(processDeployment.getName().getValue())
        .resourceName(processDeployment.getResourceName().getValue())
        .bpmnXml(processDeployment.getBpmnXml().toString())
        .applicationBase(processDeployment.getApplicationBase().getValue())  // todo, is application base same as category?
        .build();

    var deployedRepresentation  = (IgrpProcessDefinitionRepresentation) processDefinitionAdapter.deploy(processDefinitionRepresentation);

    return processDeploymentMapper.toModel(deployedRepresentation);
  }

  @Override
  public void undeploy(String deploymentId) {

  }

  @Override
  public PageableLista<ProcessDeployment> findAll(Code applicationCode) {
    ArrayList<ProcessDeployment> deployments = new ArrayList<>();
    // Go to activiti engine here
    // ...
    return new PageableLista<>(
        0,
        50,
        0L,
        0,
        false,
        false,
        deployments
    );
  }

  private ProcessFilter toProcessFilter(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = new ProcessFilter();
    processFilter.setName(filter.getProcessName());
    processFilter.setApplicationBase(filter.getApplicationBase() != null ? filter.getApplicationBase().getValue() : null);

    if (filter.getPageNumber() != null && filter.getPageSize() != null) {
      int startIndex = filter.getPageNumber() * filter.getPageSize();
      processFilter.setStartIndex(startIndex);
      processFilter.setMaxResults(filter.getPageSize());
    }
    return processFilter;
  }


  @Override
  public PageableLista<ProcessDeployment> findAll(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = toProcessFilter(filter);

    int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 20;
    int pageNumber = filter.getPageNumber() != null ? filter.getPageNumber() : 0;
    int startIndex = pageNumber * pageSize;

    processFilter.setStartIndex(startIndex);
    processFilter.setMaxResults(pageSize);

    List<ProcessDefinition> definitions = processManagerAdapter.getDeployedProcesses(processFilter);

    // Mapear para ProcessDeployment
    List<ProcessDeployment> content = definitions.stream()
        .map(def -> ProcessDeployment.builder()
            .id(Identifier.create(def.getId()))
            .key(Code.create(def.getKey()))
            .name(Name.create(def.getName()))
            .description(def.getDescription())
            .applicationBase(Code.create(def.getCategory()))
            .deploymentId(def.getDeploymentId())
            .version( String.valueOf(def.getVersion()))
            .build())
        .toList();

    boolean hasNextPage = definitions.size() == pageSize;
    long totalElements = hasNextPage ? startIndex + definitions.size() + 1 : startIndex + definitions.size();

    int totalPages = (int) Math.ceil((double) totalElements / pageSize);
    boolean first = pageNumber == 0;
    boolean last = !hasNextPage;

    return PageableLista.<ProcessDeployment>builder()
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .totalElements(totalElements)
        .totalPages(totalPages)
        .first(first)
        .last(last)
        .content(content)
        .build();
  }



}

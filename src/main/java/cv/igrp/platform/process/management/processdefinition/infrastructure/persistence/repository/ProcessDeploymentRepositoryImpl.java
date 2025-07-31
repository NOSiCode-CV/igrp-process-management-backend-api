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
        .bpmnXml(processDeployment.getBpmnXml().getXml())
        .applicationBase(processDeployment.getApplicationBase().getValue())
        .build();

    var deployedRepresentation  = (IgrpProcessDefinitionRepresentation) processDefinitionAdapter.deploy(processDefinitionRepresentation);

    //System.out.println("deployedRepresentation:: "+deployedRepresentation);

    return processDeploymentMapper.toModel(deployedRepresentation);
  }

  @Override
  public void undeploy(String deploymentId) {

  }

  private ProcessFilter toProcessFilter(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = new ProcessFilter();
    processFilter.setName(filter.getProcessName()!=null && !filter.getProcessName().isBlank() ? filter.getProcessName() : null );
    processFilter.setApplicationBase(filter.getApplicationBase() != null && !filter.getApplicationBase().getValue().isBlank() ? filter.getApplicationBase().getValue() : null);

    return processFilter;
  }


  @Override
  public PageableLista<ProcessDeployment> findAll(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = toProcessFilter(filter);

    List<ProcessDefinition> definitions = processManagerAdapter.getDeployedProcesses(processFilter);
    System.out.println("definitions: " + definitions);

    // Mapear para ProcessDeployment
    List<ProcessDeployment> content = definitions.stream()
        .map(def -> ProcessDeployment.builder()
            .id(def.getId())
            .key(Code.create(def.getKey()))
            .name(Name.create(def.getName()))
            .description(def.getDescription())
            .applicationBase(Code.create(def.getApplicationBase()))
            .deploymentId(def.getDeploymentId())
            .version( String.valueOf(def.getVersion()))
            .build())
        .toList();


    return PageableLista.<ProcessDeployment>builder()
        .pageNumber(processFilter.getPageNumber())
        .pageSize(processFilter.getPageSize())
        .content(content)
        .build();
  }



}

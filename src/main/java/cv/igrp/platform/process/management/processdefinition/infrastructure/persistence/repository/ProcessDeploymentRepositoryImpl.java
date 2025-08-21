package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.framework.runtime.core.engine.process.ProcessDefinitionAdapter;
import cv.igrp.framework.runtime.core.engine.process.ProcessManagerAdapter;
import cv.igrp.framework.runtime.core.engine.process.model.IgrpProcessDefinitionRepresentation;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessDefinition;
import cv.igrp.framework.runtime.core.engine.process.model.ProcessFilter;
import cv.igrp.framework.runtime.core.engine.task.TaskQueryService;
import cv.igrp.platform.process.management.processdefinition.domain.exception.ProcessDeploymentException;
import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link ProcessDeploymentRepository} that delegates to
 * process definition and manager adapters for deployment and querying.
 */
@Repository
public class ProcessDeploymentRepositoryImpl implements ProcessDeploymentRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDeploymentRepositoryImpl.class);

  private final ProcessDefinitionAdapter processDefinitionAdapter;
  private  final ProcessDeploymentMapper processDeploymentMapper;
  private final ProcessManagerAdapter processManagerAdapter;

  private final TaskQueryService taskQueryService;

  public ProcessDeploymentRepositoryImpl(ProcessDefinitionAdapter processDefinitionAdapter,
                                         ProcessDeploymentMapper processDeploymentMapper,
                                         ProcessManagerAdapter processManagerAdapter,
                                         TaskQueryService taskQueryService) {
    this.processDefinitionAdapter = processDefinitionAdapter;
    this.processDeploymentMapper = processDeploymentMapper;
    this.processManagerAdapter = processManagerAdapter;
    this.taskQueryService = taskQueryService;
  }

  @Override
  public ProcessDeployment deploy(ProcessDeployment processDeployment) {
    try {
      IgrpProcessDefinitionRepresentation processDefinitionRepresentation = IgrpProcessDefinitionRepresentation.builder()
          .key(processDeployment.getKey().getValue())
          .name(processDeployment.getName().getValue())
          .resourceName(processDeployment.getResourceName().getValue())
          .bpmnXml(processDeployment.getBpmnXml().getXml())
          .applicationBase(processDeployment.getApplicationBase().getValue())
          .build();
      var deployedRepresentation = (IgrpProcessDefinitionRepresentation) processDefinitionAdapter.deploy(processDefinitionRepresentation);
      return processDeploymentMapper.toModel(deployedRepresentation);
    }catch (Exception e) {
        LOGGER.error("Failed to deploy process deployment: {}", processDeployment.getKey().getValue(), e);
        throw new ProcessDeploymentException("Failed to deploy process deployment: " + processDeployment.getKey().getValue(), e);
      }
  }

  @Override
  public PageableLista<ProcessDeployment> findAll(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = toProcessFilter(filter);

    List<ProcessDefinition> definitions = processManagerAdapter.getDeployedProcesses(processFilter);

    List<ProcessDeployment> content = definitions.stream()
        .map(def -> ProcessDeployment.builder()
            .id(def.id())
            .procReleaseId(def.id() != null ? Code.create(def.id()) : null)
            .key(Code.create(def.key()))
            .name(Name.create(def.name()))
            .description(def.description())
            .applicationBase(Code.create(def.applicationBase()))
            .deploymentId(def.deploymentId())
            .version( String.valueOf(def.version()))
            .resourceName(ResourceName.create(def.resourceName()))
            .deployed(!def.suspended())
            .build())
        .toList();

    return PageableLista.<ProcessDeployment>builder()
        .pageNumber(processFilter.getPageNumber())
        .pageSize(processFilter.getPageSize())
        .content(content)
        .build();
  }

  private ProcessFilter toProcessFilter(ProcessDeploymentFilter filter) {
    ProcessFilter processFilter = new ProcessFilter();
    processFilter.setName(filter.getProcessName()!=null && !filter.getProcessName().isBlank() ? filter.getProcessName() : null );
    processFilter.setApplicationBase(filter.getApplicationBase() != null ? filter.getApplicationBase().getValue() : null);
    return processFilter;
  }

  @Override
  public List<ProcessArtifact> findAllArtifacts(String processDefinitionId) {
    List<cv.igrp.framework.runtime.core.engine.task.model.ProcessArtifact> artifacts = taskQueryService.getProcessArtifacts(processDefinitionId);
    return artifacts.stream().map(artifact -> ProcessArtifact.builder()
        .formKey(Code.create(artifact.formKey() != null ? artifact.formKey() : "NOT_SET"))
        .name(Name.create(artifact.taskName() != null ? artifact.taskName() : "NOT_SET"))
        .key(Code.create(artifact.taskKey()))
        .processDefinitionId(Code.create(processDefinitionId))
        .build()).toList();
  }

}

package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessPackage;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.domain.models.ResourceName;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class ProcessDeploymentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDeploymentService.class);


  private final ProcessDeploymentRepository processDeploymentRepository;
  private final UserContext userContext;

  private final ProcessArtifactService processArtifactService;
  private final ProcessSequenceService processSequenceService;

  public ProcessDeploymentService(ProcessDeploymentRepository processDeploymentRepository,
                                  UserContext userContext,
                                  ProcessArtifactService processArtifactService,
                                  ProcessSequenceService processSequenceService
  ) {
    this.processDeploymentRepository = processDeploymentRepository;
    this.userContext = userContext;
    this.processArtifactService = processArtifactService;
    this.processSequenceService = processSequenceService;
  }

  public ProcessDeployment deployProcess(ProcessDeployment processDeployment){
    processDeployment.deploy();
    return processDeploymentRepository.deploy(processDeployment);
  }

  public PageableLista<ProcessDeployment> getAllDeployments(ProcessDeploymentFilter processDeploymentFilter){
    if(processDeploymentFilter.isFilterByCurrentUser()){
      userContext.getCurrentGroups()
          .forEach(processDeploymentFilter::addContextGroup);
    }
    return processDeploymentRepository.findAll(processDeploymentFilter);
  }

  public List<ProcessArtifact> getDeployedArtifactsByProcessDefinitionId(String processDefinitionId) {
    return processDeploymentRepository.findAllArtifacts(processDefinitionId);
  }

  public String findLastProcessDefinitionIdByKey(String processDefinitionKey) {
    return processDeploymentRepository.findLastProcessDefinitionIdByKey(processDefinitionKey);
  }

  public void assignProcessDefinition(String processDefinitionId, String groups) {
    if (processDefinitionId == null || processDefinitionId.isBlank()) {
      throw IgrpResponseStatusException.badRequest("Process definition ID cannot be null or blank");
    }
    if (groups == null || groups.isBlank()) {
      throw IgrpResponseStatusException.badRequest("Groups cannot be null or blank");
    }
    Arrays.stream(groups.split(","))
        .map(String::trim)
        .filter(g -> !g.isEmpty())
        .distinct()
        .forEach(group ->
            processDeploymentRepository
                .addCandidateStarterGroup(processDefinitionId, group)
        );
  }

  public ProcessPackage exportProcessDefinition(String processDefinitionId){
    ProcessDeployment processDeployment = processDeploymentRepository.findById(processDefinitionId)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No process definition found with ID: " + processDefinitionId));
    ProcessPackage processPackage = ProcessPackage.builder()
        .processName(processDeployment.getName())
        .processId(Code.create(processDefinitionId))
        .processDescription(processDeployment.getDescription())
        .processKey(Code.create(processDeployment.getKey().getValue()))
        .applicationBase(processDeployment.getApplicationBase())
        .bpmnXml(processDeployment.getBpmnXml())
        .build();

    // Add the sequence
    processPackage.addSequence(
        processSequenceService.getSequenceByProcessDefinitionKey(processPackage.getProcessKey())
    );

    // Add artifacts
    processArtifactService.getArtifactsByProcessDefinitionId(processPackage.getProcessId())
        .forEach(processPackage::addArtifact);

    // Add candidate groups

    return processPackage;
  }

  public void importProcessDefinition(ProcessPackage processPackage){
    LOGGER.info("Importing process definition: {}", processPackage.getProcessKey());

    // Deploy Process
    deployProcess(
        ProcessDeployment.builder()
            .bpmnXml(processPackage.getBpmnXml())
            .name(processPackage.getProcessName())
            .description(processPackage.getProcessDescription())
            .resourceName(ResourceName.create(processPackage.getProcessVersion() + ".bpmn20.xml"))
            .key(processPackage.getProcessKey())
            .applicationBase(processPackage.getApplicationBase())
            .build()
    );

    // Save Process Sequence
    Optional<ProcessSequence> sequenceOptional = processSequenceService
        .getSequenceByProcessDefinitionKeyOpt(processPackage.getProcessKey());
    if(sequenceOptional.isEmpty()){
      processSequenceService.save(processPackage.getSequence());
    }

    // Save Process Artifacts
    processPackage.getArtifacts().forEach(processArtifactService::configureArtifact);

    // Candidate Groups

    LOGGER.info("Process definition imported successfully: {}", processPackage.getProcessKey());
  }

}

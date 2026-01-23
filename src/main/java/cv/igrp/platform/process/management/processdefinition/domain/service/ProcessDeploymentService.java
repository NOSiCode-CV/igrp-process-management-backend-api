package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.filter.ProcessDeploymentFilter;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDeploymentRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProcessDeploymentService {

  private final ProcessDeploymentRepository processDeploymentRepository;
  private final UserContext userContext;

  public ProcessDeploymentService(ProcessDeploymentRepository processDeploymentRepository,
                                  UserContext userContext
  ) {
    this.processDeploymentRepository = processDeploymentRepository;
    this.userContext = userContext;
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


}

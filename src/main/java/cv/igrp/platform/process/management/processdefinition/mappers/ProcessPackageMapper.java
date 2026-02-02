package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessPackageDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessPackage;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class ProcessPackageMapper {

  private final ProcessArtifactMapper artifactMapper;
  private final ProcessSequenceMapper sequenceMapper;

  public ProcessPackageMapper(ProcessArtifactMapper artifactMapper,
                              ProcessSequenceMapper sequenceMapper) {
    this.artifactMapper = artifactMapper;
    this.sequenceMapper = sequenceMapper;
  }

  public ProcessPackageDTO toDTO(ProcessPackage processPackage){
    ProcessPackageDTO dto = new ProcessPackageDTO();
    dto.setProcessName(processPackage.getProcessName().getValue());
    dto.setProcessDescription(processPackage.getProcessDescription());
    dto.setProcessKey(processPackage.getProcessKey().getValue());
    dto.setBpmnXml(processPackage.getBpmnXml() != null ? processPackage.getBpmnXml().getXml() : null);
    dto.setApplicationBase(processPackage.getApplicationBase() != null ? processPackage.getApplicationBase().getValue() : null);
    dto.setProcessVersion(processPackage.getProcessVersion());
    dto.setArtifacts(processPackage.getArtifacts().stream().map(artifactMapper::toDTO).toList());
    dto.setSequence(sequenceMapper.toDTO(processPackage.getSequence()));
    dto.setCandidateGroups(
        !processPackage.getCandidateGroups().isEmpty() ? String.join(",", processPackage.getCandidateGroups()) : null
    );
    return dto;
  }

  public ProcessPackage toModel(String processDefinitionID, ProcessPackageDTO dto){
    ProcessPackage processPackage = ProcessPackage.builder()
        .processId(Code.create(processDefinitionID))
        .processName(Name.create(dto.getProcessName()))
        .processDescription(dto.getProcessDescription())
        .processKey(Code.create(dto.getProcessKey()))
        .applicationBase(Code.create(dto.getApplicationBase()))
        .bpmnXml(BpmnXml.create(dto.getBpmnXml()))
        .processVersion(dto.getProcessVersion())
        .sequence(sequenceMapper.toModel(dto.getSequence()))
        .artifacts(dto.getArtifacts().stream().map(artifactMapper::toModel).toList())
        .build();
    if(dto.getCandidateGroups() != null && !dto.getCandidateGroups().isBlank()){
      Arrays.stream(
          dto.getCandidateGroups().split(",")
      ).forEach(group -> processPackage.getCandidateGroups().add(group));
    }
    return processPackage;
  }

}

package cv.igrp.platform.process.management.processdefinition.mappers;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessPackageDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.BpmnXml;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessPackage;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


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
    dto.setProcessVersion(processPackage.getProcessVersion());
    return dto;
  }

  public ProcessPackage toModel(ProcessPackageDTO dto){
    ProcessPackage processPackage = ProcessPackage.builder()
        .processName(Name.create(dto.getProcessName()))
        .processDescription(dto.getProcessDescription())
        .processKey(Code.create(dto.getProcessKey()))
        .applicationBase(Code.create(dto.getApplicationBase()))
        .bpmnXml(BpmnXml.create(dto.getBpmnXml()))
        .processVersion(dto.getProcessVersion())
        .sequence(sequenceMapper.toModel(dto.getSequence()))
        .artifacts(dto.getArtifacts().stream().map(artifactMapper::toModel).toList())
        .candidateGroups(
            dto.getCandidateGroups() != null
                ? new HashSet<>(List.of(dto.getCandidateGroups().split(",")))
                : new HashSet<>()
        )
        .build();
    return processPackage;
  }

}

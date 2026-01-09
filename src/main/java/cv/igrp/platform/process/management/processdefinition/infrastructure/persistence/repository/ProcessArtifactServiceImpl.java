package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDefinitionRepository;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessArtifactMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.ProcessArtifactEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessArtifactEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProcessArtifactServiceImpl implements ProcessDefinitionRepository {

  private final ProcessArtifactEntityRepository processArtifactEntityRepository;
  private final ProcessArtifactMapper mapper;

  public ProcessArtifactServiceImpl(ProcessArtifactEntityRepository processArtifactEntityRepository,
                                    ProcessArtifactMapper mapper) {
    this.processArtifactEntityRepository = processArtifactEntityRepository;
    this.mapper = mapper;
  }

  @Override
  public ProcessArtifact saveArtifact(ProcessArtifact processArtifact) {
    ProcessArtifactEntity entity = processArtifactEntityRepository.save(mapper.toEntity(processArtifact));
    return mapper.toModel(entity);
  }

  @Override
  public List<ProcessArtifact> findAllArtifacts(Code processDefinitionId) {
    List<ProcessArtifactEntity> processArtifactEntities =  processArtifactEntityRepository
        .findAllByProcessDefinitionId(processDefinitionId.getValue());
    return mapper.toModel(processArtifactEntities);
  }

  @Override
  public Optional<ProcessArtifact> findArtifactById(Identifier id) {
    return processArtifactEntityRepository.findById(id.getValue()).map(mapper::toModel);
  }

  @Override
  public void deleteArtifact(ProcessArtifact processArtifact) {
    processArtifactEntityRepository.deleteById(processArtifact.getId().getValue());
  }

  @Override
  public Optional<ProcessArtifact> findArtifactByProcessDefinitionIdAndKey(Code processDefinitionId, Code key) {
    return processArtifactEntityRepository.findByProcessDefinitionIdAndKey(
        processDefinitionId.getValue(),
        key.getValue()
    ).map(mapper::toModel);
  }

}

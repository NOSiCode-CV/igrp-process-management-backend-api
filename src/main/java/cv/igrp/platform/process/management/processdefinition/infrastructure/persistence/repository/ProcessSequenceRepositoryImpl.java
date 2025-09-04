package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessSequenceRepository;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.ProcessInstanceSequenceEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProcessSequenceRepositoryImpl implements ProcessSequenceRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessSequenceRepositoryImpl.class);

  private final ProcessInstanceSequenceEntityRepository sequenceEntityRepository;
  private final ProcessSequenceMapper sequenceMapper;

  public ProcessSequenceRepositoryImpl(ProcessInstanceSequenceEntityRepository sequenceEntityRepository,
                                       ProcessSequenceMapper sequenceMapper) {
    this.sequenceEntityRepository = sequenceEntityRepository;
    this.sequenceMapper = sequenceMapper;
  }


  @Override
  public Optional<ProcessSequence> findByProcessDefinitionKey(String processDefinitionKey) {
    LOGGER.debug("Fetching ProcessSequence with processDefinitionKey={}", processDefinitionKey);
    return sequenceEntityRepository.findByProcessDefinitionKey(processDefinitionKey).map(sequenceMapper::toModel);
  }


  @Override
  public ProcessSequence save(ProcessSequence model) {
    LOGGER.debug("Saving ProcessSequence with id={} and processDefinitionKey={}",
        model.getId(), model.getProcessDefinitionKey().getValue());
    return sequenceMapper.toModel(sequenceEntityRepository.save(sequenceMapper.toEntity(model)));
  }

  @Override
  public Optional<ProcessSequence> findByProcessDefinitionKeyForUpdate(String processDefinitionKey) {
    LOGGER.debug("Fetching (FOR UPDATE) ProcessSequence with processDefinitionKey={}", processDefinitionKey);
    return sequenceEntityRepository.findByProcessDefinitionKeyForUpdate(processDefinitionKey).map(sequenceMapper::toModel);
  }

}

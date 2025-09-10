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
  public Optional<ProcessSequence> findByProcessAndApplication(String processDefinitionKey, String applicationCode) {
    LOGGER.debug("Fetching ProcessSequence with processDefinitionKey={} and applicationCode={}", processDefinitionKey, applicationCode);
    return sequenceEntityRepository.findByProcessDefinitionKeyAndApplicationCode(processDefinitionKey, applicationCode).map(sequenceMapper::toModel);
  }


  @Override
  public ProcessSequence save(ProcessSequence model) {
    LOGGER.debug("Saving ProcessSequence with id={} and processDefinitionKey={} and applicationCode={}",
        model.getId(), model.getProcessDefinitionKey().getValue(), model.getApplicationCode().getValue());
    return sequenceMapper.toModel(sequenceEntityRepository.save(sequenceMapper.toEntity(model)));
  }


  @Override
  public Optional<ProcessSequence> findForUpdate(String processDefinitionKey, String applicationCode) {
    LOGGER.debug("Fetching (FOR UPDATE) ProcessSequence with processDefinitionKey={} and applicationCode={}", processDefinitionKey,applicationCode);
    return sequenceEntityRepository.findForUpdate(processDefinitionKey,applicationCode).map(sequenceMapper::toModel);
  }

}

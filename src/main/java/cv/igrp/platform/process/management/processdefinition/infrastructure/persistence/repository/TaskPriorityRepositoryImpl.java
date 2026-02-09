package cv.igrp.platform.process.management.processdefinition.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processdefinition.domain.models.TaskPriority;
import cv.igrp.platform.process.management.processdefinition.domain.repository.TaskPriorityRepository;
import cv.igrp.platform.process.management.processdefinition.mappers.TaskPriorityMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskPriorityEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskPriorityEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskPriorityRepositoryImpl implements TaskPriorityRepository {

  private final TaskPriorityEntityRepository repository;
  private final TaskPriorityMapper mapper;

  public TaskPriorityRepositoryImpl(TaskPriorityEntityRepository repository,
                                    TaskPriorityMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public TaskPriority savePriority(TaskPriority taskPriority) {
    TaskPriorityEntity entity = repository.save(mapper.toEntity(taskPriority));
    return mapper.toModel(entity);
  }

  @Override
  public List<TaskPriority> findAllPriority(Code processDefinitionKey) {
    List<TaskPriorityEntity> taskPriorityEntities = repository.findAllByProcessDefinitionKey(
        processDefinitionKey.getValue()
    );
    return taskPriorityEntities.stream().map(mapper::toModel).toList();
  }

  @Override
  public Optional<TaskPriority> findPriorityById(Identifier id) {
    return repository.findById(id.getValue()).map(mapper::toModel);
  }

  @Override
  public void deletePriority(TaskPriority area) {
    repository.deleteById(area.getId().getValue());
  }

}

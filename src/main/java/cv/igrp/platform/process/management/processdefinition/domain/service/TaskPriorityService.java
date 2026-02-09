package cv.igrp.platform.process.management.processdefinition.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.models.TaskPriority;
import cv.igrp.platform.process.management.processdefinition.domain.repository.TaskPriorityRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskPriorityService {

  private final TaskPriorityRepository taskPriorityRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskPriorityService.class);

  public TaskPriorityService(TaskPriorityRepository taskPriorityRepository) {
    this.taskPriorityRepository = taskPriorityRepository;
  }

  public TaskPriority configurePriority(TaskPriority newTaskPriority){
    Optional<TaskPriority> optionalTaskPriority = taskPriorityRepository.findPriorityById(newTaskPriority.getId());
    if(optionalTaskPriority.isPresent()){
      LOGGER.info("Updating priority [{}]", newTaskPriority.getId());
      TaskPriority taskPriority = optionalTaskPriority.get();
      taskPriority.update(newTaskPriority);
      return taskPriorityRepository.savePriority(taskPriority);
    }
    return taskPriorityRepository.savePriority(newTaskPriority);
  }

  public void deletePriority(Identifier id){
    TaskPriority taskPriority = getPriorityById(id);
    taskPriorityRepository.deletePriority(taskPriority);
  }

  public TaskPriority getPriorityById(Identifier id){
    return taskPriorityRepository.findPriorityById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Priority not found. ID: " + id.getValue()));
  }

  public List<TaskPriority> getPrioritiesByProcessDefinitionKey(Code processDefinitionKey) {
    return taskPriorityRepository.findAllPriority(processDefinitionKey);
  }

}

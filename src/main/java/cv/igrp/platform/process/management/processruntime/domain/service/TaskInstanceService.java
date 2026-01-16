package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDefinitionRepository;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.ProcessInstanceRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceEventRepository;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.application.constants.VariableTag;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TaskInstanceService {

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceRepository processInstanceRepository;
  private final ProcessDefinitionRepository processDefinitionRepository;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository
  ) {

    this.taskInstanceRepository = taskInstanceRepository;
    this.taskInstanceEventRepository = taskInstanceEventRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceRepository = processInstanceRepository;
    this.processDefinitionRepository = processDefinitionRepository;
  }


  public void createTaskInstancesByProcess(ProcessInstance processInstance) {
    this.createNextTaskInstances(processInstance, Code.create(processInstance.getStartedBy()));
  }

  public TaskInstance getByIdWihEvents(Identifier id) {
    return taskInstanceRepository.findByIdWithEvents(id.getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound("No Task Instance found with id: " + id));
  }

  public void claimTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.claim(data);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.claimTask(
        taskInstance.getExternalId().getValue(),
        taskInstance.getAssignedBy().getValue()
    );
  }


  public void assignTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    if (data.getTargetUser() != null) {

      taskInstance.assignUser(data);

      runtimeProcessEngineRepository.assignTask(
          taskInstance.getExternalId().getValue(),
          taskInstance.getAssignedBy().getValue(),
          data.getNote()
      );
    } else {

      taskInstance.addCandidateGroup(data);

      data.getCandidateGroups().forEach(group -> {
        runtimeProcessEngineRepository.addCandidateGroup(
            taskInstance.getExternalId().getValue(),
            group
        );
      });

    }

    if (data.getPriority() != null && !data.getPriority().equals(taskInstance.getPriority())) {
      runtimeProcessEngineRepository.setTaskPriority(
          taskInstance.getExternalId().getValue(),
          data.getPriority()
      );
    }

    this.save(taskInstance);

  }


  public void unClaimTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.unClaim(data);
    this.save(taskInstance);
    // Call the process engine to claim a task
    runtimeProcessEngineRepository.unClaimTask(
        taskInstance.getExternalId().getValue()
    );
  }

  public TaskInstance saveTask(TaskOperationData data) {
    var taskInstance = getByIdWihEvents(data.getId());
    // Validate
    data.validateVariablesAndForms();
    // Save
    taskInstance.addVariablesAndForms(data);
    this.save(taskInstance);
    // Process Engine
    runtimeProcessEngineRepository.saveTask(
        taskInstance.getExternalId().getValue(),
        null,
        data.getVariables()
    );
    return taskInstance;
  }

  public TaskInstance completeTask(TaskOperationData data) {
    data.validateVariablesAndForms();

    var taskInstance = getByIdWihEvents(data.getId());
    taskInstance.complete(data);
    // Save
    taskInstance.addVariablesAndForms(data);
    var completedTask = save(taskInstance);

    // Call the process engine to complete a task
    runtimeProcessEngineRepository.completeTask(
        taskInstance.getExternalId().getValue(),
        null,
        data.getVariables()
    );

    var processInstance = processInstanceRepository
        .findById(taskInstance.getProcessInstanceId().getValue()).orElseThrow(
            () -> IgrpResponseStatusException.notFound("No Process Instance found with id: " + taskInstance.getProcessInstanceId().getValue()));

    var activityProcess = runtimeProcessEngineRepository
        .getProcessInstanceById(processInstance.getEngineProcessNumber().getValue());

    this.createNextTaskInstances(processInstance, data.getCurrentUser());

    if (activityProcess.getStatus() == ProcessInstanceStatus.COMPLETED) {
      processInstance.complete(
          activityProcess.getEndedAt(),
          activityProcess.getEndedBy() != null ? activityProcess.getEndedBy() : data.getCurrentUser().getValue()
      );
      processInstanceRepository.save(processInstance);
    }

    return completedTask;
  }

  public TaskInstance getTaskById(Identifier id) {
    TaskInstance taskInstance = getByIdWihEvents(id);
    // Enrich with process variables
    Map<String, Object> variables = runtimeProcessEngineRepository.getProcessVariables(taskInstance.getEngineProcessNumber());
    taskInstance.addProcessVariables(variables);
    return taskInstance;
  }

  public PageableLista<TaskInstance> getAllTaskInstances(TaskInstanceFilter filter) {

    PageableLista<TaskInstance> taskInstances = taskInstanceRepository.findAll(filter);

    // Enrich with process variables
    Map<String, Map<String, Object>> variablesMap = new HashMap<>();
    List<String> engineProcessNumbers = taskInstances.getContent().stream()
        .map(TaskInstance::getEngineProcessNumber)
        .toList();
    for (String engineProcessNumber : engineProcessNumbers) {
      if (variablesMap.containsKey(engineProcessNumber))
        continue;
      Map<String, Object> variables = runtimeProcessEngineRepository.getProcessVariables(engineProcessNumber);
      variablesMap.put(engineProcessNumber, variables);
    }
    for (TaskInstance task : taskInstances.getContent()) {
      Map<String, Object> vars = variablesMap.get(task.getEngineProcessNumber());
      if (vars != null) {
        task.addProcessVariables(vars);
      }
    }

    return taskInstances;
  }

  public Map<String, Object> getTaskVariables(Identifier id) {
    var taskInstance = getTaskById(id);
    Map<String, Object> variables = taskInstance.getVariables();
    Map<String, Object> forms = taskInstance.getForms();
    return Map.of(
        VariableTag.FORMS.getCode(), forms,
        VariableTag.VARIABLES.getCode(), variables
    );
  }


  public TaskStatistics getGlobalTaskStatistics() {
    return taskInstanceRepository.getGlobalTaskStatistics();
  }


  public TaskStatistics getTaskStatisticsByUser(Code user) {
    return taskInstanceRepository.getTaskStatisticsByUser(user);
  }

  void createNextTaskInstances(ProcessInstance processInstance, Code user) {

    var activeTasks = runtimeProcessEngineRepository.getActiveTaskInstances(
        processInstance.getEngineProcessNumber().getValue());

    if (activeTasks.isEmpty()) {
      return;
    }

    // 1. Update priorities for all active tasks
    activeTasks.forEach(task ->
        runtimeProcessEngineRepository.setTaskPriority(
            task.getExternalId().getValue(),
            processInstance.getPriority()
        )
    );

    // 2. Preload artifacts and map {taskKey → formKey}
    var artifacts = processDefinitionRepository.findAllArtifacts(processInstance.getProcReleaseId());

    var artifactFormMap = artifacts.stream()
        .collect(Collectors.toMap(
            ProcessArtifact::getKey,
            a -> Code.create(a.getFormKey().getValue())
        ));

    // 3. Pre-map {taskKey → artifact} for faster lookup
    var artifactByKey = artifacts.stream()
        .collect(Collectors.toMap(ProcessArtifact::getKey, a -> a));

    // 4. Process each task
    for (var task : activeTasks) {

      // Instantiate next task using precomputed form key
      var newTask = task.withProperties(
          processInstance,
          artifactFormMap.get(task.getTaskKey()),
          user
      );

      var artifact = artifactByKey.get(task.getTaskKey());
      if (artifact != null) {
        for (var groupId : artifact.getCandidateGroups()) {

          // Add group to new task instance
          newTask.addCandidateGroup(groupId, user);

          // Add group to Activiti runtime task
          runtimeProcessEngineRepository.addCandidateGroup(
              task.getExternalId().getValue(),
              groupId
          );
        }
      }

      createTask(newTask);
    }

  }


  void createTask(TaskInstance taskInstance) {
    taskInstance.create();
    taskInstanceRepository.create(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getFirst());
  }


  TaskInstance save(TaskInstance taskInstance) {
    taskInstanceRepository.update(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getLast());
    return taskInstance;
  }

  private void saveCurrentEvent(TaskInstanceEvent taskInstanceEvent) {
    taskInstanceEvent.create();
    taskInstanceEventRepository.save(taskInstanceEvent);
  }

}

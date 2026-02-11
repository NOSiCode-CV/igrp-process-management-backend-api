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
import cv.igrp.platform.process.management.shared.domain.models.ArtifactContext;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TaskInstanceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstanceService.class);

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceRepository processInstanceRepository;
  private final ProcessDefinitionRepository processDefinitionRepository;

  private final UserContext userContext;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository,
                             UserContext userContext
  ) {

    this.taskInstanceRepository = taskInstanceRepository;
    this.taskInstanceEventRepository = taskInstanceEventRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceRepository = processInstanceRepository;
    this.processDefinitionRepository = processDefinitionRepository;
    this.userContext = userContext;
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

    if (filter.isFilterByCurrentUser()) {
      final var currentUser = userContext.getCurrentUser();
      final var isSuperAdmin = userContext.isSuperAdmin();
      filter.bindCurrentUser(currentUser, isSuperAdmin);
      userContext.getCurrentGroups()
          .forEach(filter::addContextUserGroup);
    }

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


  public TaskStatistics getTaskStatisticsByUser(Code user, List<String> groups) {
    return taskInstanceRepository.getTaskStatisticsByUser(
        user,
        groups,
        userContext.isSuperAdmin()
    );
  }

  void createNextTaskInstances(ProcessInstance processInstance, Code user) {

    var activeTasks = getActiveRuntimeTasks(processInstance);
    if (activeTasks.isEmpty()) {
      return;
    }

    updateRuntimePriorities(activeTasks, processInstance);

    var context = ArtifactContext.from(
        processDefinitionRepository.findAllArtifacts(processInstance.getProcReleaseId())
    );

    for (var runtimeTask : activeTasks) {

      var newTask = runtimeTask.withProperties(
          processInstance,
          context.findFormKey(runtimeTask.getTaskKey().getValue()).orElse(null),
          user
      );

      context.findArtifact(runtimeTask.getTaskKey().getValue())
          .ifPresent(artifact -> {
            assignGroups(runtimeTask, newTask, artifact, user);
            configureDueDate(runtimeTask, newTask, artifact);
          });

      createTask(newTask);
    }

  }

  private List<TaskInstance> getActiveRuntimeTasks(ProcessInstance processInstance) {
    return runtimeProcessEngineRepository.getActiveTaskInstances(
        processInstance.getEngineProcessNumber().getValue()
    );
  }

  private void updateRuntimePriorities(List<TaskInstance> tasks, ProcessInstance processInstance) {
    tasks.forEach(task ->
        runtimeProcessEngineRepository.setTaskPriority(
            task.getExternalId().getValue(),
            processInstance.getPriority()
        )
    );
  }

  private void assignGroups(TaskInstance runtimeTask, TaskInstance task, ProcessArtifact artifact, Code user) {
    for (var groupId : artifact.getCandidateGroups()) {
      task.addCandidateGroup(groupId, user);
      runtimeProcessEngineRepository.addCandidateGroup(
          runtimeTask.getExternalId().getValue(),
          groupId
      );
    }
  }

  private void configureDueDate(TaskInstance runtimeTask, TaskInstance task, ProcessArtifact artifact) {
    LOGGER.info("DueDate: {} from ProcessArtifact: {}", artifact.getDueDate(), artifact.getKey());
    if (artifact.getDueDate() == null) {
      return;
    }
    LocalDateTime dueDate = LocalDateTime.now().plus(Duration.parse(artifact.getDueDate()));
    task.updateDueDate(dueDate);
    runtimeProcessEngineRepository.setTaskDueDate(
        runtimeTask.getExternalId().getValue(),
        dueDate
    );
  }


  public void createTask(TaskInstance taskInstance) {
    taskInstance.create();
    taskInstanceRepository.create(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getFirst());
  }


  public TaskInstance save(TaskInstance taskInstance) {
    taskInstanceRepository.update(taskInstance);
    this.saveCurrentEvent(taskInstance.getTaskInstanceEvents().getLast());
    return taskInstance;
  }

  private void saveCurrentEvent(TaskInstanceEvent taskInstanceEvent) {
    taskInstanceEvent.create();
    taskInstanceEventRepository.save(taskInstanceEvent);
  }

}

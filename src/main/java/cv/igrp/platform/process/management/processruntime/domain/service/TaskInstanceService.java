package cv.igrp.platform.process.management.processruntime.domain.service;

import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.repository.ProcessDefinitionRepository;
import cv.igrp.platform.process.management.processruntime.domain.models.*;
import cv.igrp.platform.process.management.processruntime.domain.repository.*;
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
import java.util.*;


@Service
public class TaskInstanceService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstanceService.class);

  private final TaskInstanceRepository taskInstanceRepository;
  private final TaskInstanceEventRepository taskInstanceEventRepository;
  private final TaskAssignmentRuleRepository taskAssignmentRuleRepository;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceRepository processInstanceRepository;
  private final ProcessDefinitionRepository processDefinitionRepository;
  private final UserProfileRepository userProfileRepository;

  private final UserContext userContext;

  public TaskInstanceService(TaskInstanceRepository taskInstanceRepository,
                             TaskInstanceEventRepository taskInstanceEventRepository,
                             TaskAssignmentRuleRepository taskAssignmentRuleRepository,
                             RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository,
                             UserProfileRepository userProfileRepository,
                             UserContext userContext
  ) {

    this.taskInstanceRepository = taskInstanceRepository;
    this.taskInstanceEventRepository = taskInstanceEventRepository;
    this.taskAssignmentRuleRepository = taskAssignmentRuleRepository;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceRepository = processInstanceRepository;
    this.processDefinitionRepository = processDefinitionRepository;
    this.userProfileRepository = userProfileRepository;
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

      saveAssigneeRule(taskInstance, data);
    } else {

      taskInstance.addCandidates(data);

      data.getCandidateGroups().forEach(group -> {
        runtimeProcessEngineRepository.addCandidateGroup(
            taskInstance.getExternalId().getValue(),
            group
        );
      });

      var candidateUsers = data.getCandidateUsers().stream()
          .map(this::normalizeUserId)
          .flatMap(Optional::stream)
          .distinct()
          .toList();

      candidateUsers.forEach(user -> {
        runtimeProcessEngineRepository.addCandidateUser(
            taskInstance.getExternalId().getValue(),
            user
        );
      });

      saveCandidateUserRule(taskInstance, data, candidateUsers);

    }

    if (data.getPriority() != null && !data.getPriority().equals(taskInstance.getPriority())) {
      runtimeProcessEngineRepository.setTaskPriority(
          taskInstance.getExternalId().getValue(),
          data.getPriority()
      );
    }

    this.save(taskInstance);

  }

  private Optional<String> normalizeUserId(String userId) {
    if (userId == null || userId.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(userId.trim());
  }

  private void saveAssigneeRule(TaskInstance taskInstance, TaskOperationData data) {
    saveAssignmentRule(taskInstance, data, data.getTargetUser(), List.of());
  }

  private void saveCandidateUserRule(TaskInstance taskInstance, TaskOperationData data, List<String> candidateUsers) {
    saveAssignmentRule(taskInstance, data, null, candidateUsers);
  }

  private void saveAssignmentRule(
      TaskInstance taskInstance,
      TaskOperationData data,
      Code assignee,
      List<String> candidateUsers
  ) {
    if (assignee == null && (candidateUsers == null || candidateUsers.isEmpty())) {
      return;
    }
    taskAssignmentRuleRepository.save(TaskAssignmentRule.builder()
        .processDefinitionKey(taskInstance.getProcessKey())
        .processInstanceId(taskInstance.getProcessInstanceId())
        .taskDefinitionKey(taskInstance.getTaskKey())
        .assignee(assignee)
        .candidateUsers(candidateUsers)
        .assignmentMode(data.getAssignmentMode())
        .priority(data.getPriority() != null ? data.getPriority() : taskInstance.getPriority())
        .consumed(false)
        .active(true)
        .createdByTask(taskInstance.getId())
        .build()
    );
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
    // Resolve user profiles
    resolveUserProfiles(taskInstance);
    taskInstance.getTaskInstanceEvents().forEach(this::resolveUserProfiles);
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

    // Resolve user profiles
    taskInstances.getContent().forEach(taskInstance -> {
      resolveUserProfiles(taskInstance);
      taskInstance.getTaskInstanceEvents().forEach(this::resolveUserProfiles);
    });

    return taskInstances;
  }

  private void resolveUserProfiles(TaskInstance taskInstance) {
    Set<String> ids = new HashSet<>();

    addIfNotNull(ids, taskInstance.getStartedBy());
    addIfNotNull(ids, taskInstance.getEndedBy());
    addIfNotNull(ids, taskInstance.getAssignedBy());

    userProfileRepository.findBySubjectOrEmails(ids, ids).forEach(userProfile -> {

      if (matches(userProfile, taskInstance.getStartedBy())) {
        taskInstance.resolveUserProfileStartedBy(userProfile);
      }
      if (matches(userProfile, taskInstance.getEndedBy())) {
        taskInstance.resolveUserProfileEndedBy(userProfile);
      }
      if (matches(userProfile, taskInstance.getAssignedBy())) {
        taskInstance.resolveUserProfileAssignedBy(userProfile);
      }
    });

  }

  private void resolveUserProfiles(TaskInstanceEvent taskInstanceEvent) {
    String performedBy = taskInstanceEvent.getPerformedBy().getValue();
    userProfileRepository.findBySubjectOrEmail(performedBy, performedBy)
        .ifPresent(taskInstanceEvent::resolveUserProfilePerformedBy);
  }

  private boolean matches(UserProfile userProfile, Code value) {
    if (value == null) {
      return false;
    }
    String identifier = value.getValue();
    return Objects.equals(identifier, userProfile.getSub())
        || Objects.equals(identifier, userProfile.getEmail());
  }

  private void addIfNotNull(Set<String> ids, Code value) {
    if (value != null) {
      ids.add(value.getValue());
    }
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
      createNextTaskInstance(runtimeTask, processInstance, context, user);
    }

  }

  private void createNextTaskInstance(
      TaskInstance runtimeTask,
      ProcessInstance processInstance,
      ArtifactContext context,
      Code user
  ) {
    var artifact = context.findArtifact(runtimeTask.getTaskKey().getValue());
    var task = runtimeTask.withProperties(
        processInstance,
        context.findFormKey(runtimeTask.getTaskKey().getValue()).orElse(null),
        user
    );

    artifact.ifPresent(processArtifact -> configureDueDate(runtimeTask, task, processArtifact));

    createTask(task);

    applyTaskAssignments(
        task,
        artifact.map(ProcessArtifact::getCandidateGroups).orElse(Set.of()),
        matchingAssignmentRules(processInstance, task),
        user
    );
  }

  private List<TaskAssignmentRuleRequest> matchingAssignmentRules(
      ProcessInstance processInstance,
      TaskInstance taskInstance
  ) {
    return Optional.ofNullable(processInstance.getAssignmentRules())
        .orElse(List.of())
        .stream()
        .filter(rule -> rule.matches(taskInstance.getTaskKey()))
        .toList();
  }

  private void applyTaskAssignments(
      TaskInstance taskInstance,
      Set<String> definitionCandidateGroups,
      List<TaskAssignmentRuleRequest> assignmentRules,
      Code user
  ) {
    var assigneeRule = assignmentRules.stream()
        .filter(TaskAssignmentRuleRequest::hasAssignee)
        .findFirst();

    if (assigneeRule.isPresent()) {
      applyAssigneeRule(taskInstance, assigneeRule.get(), user);
      return;
    }

    applyDefinitionCandidateGroups(taskInstance, definitionCandidateGroups, user);
    applyCandidateUserRules(taskInstance, assignmentRules, user);
  }

  private void applyAssigneeRule(TaskInstance taskInstance, TaskAssignmentRuleRequest rule, Code user) {
    assignTask(TaskOperationData.builder()
        .id(taskInstance.getId().getValue().toString())
        .currentUser(user)
        .targetUser(rule.getAssignee().getValue())
        .priority(rule.getPriority())
        .assignmentMode(rule.getAssignmentMode())
        .build());
  }

  private void applyDefinitionCandidateGroups(TaskInstance taskInstance, Set<String> candidateGroups, Code user) {
    var groups = normalizeCandidateGroups(candidateGroups);
    if (groups.isEmpty()) {
      return;
    }

    assignTask(TaskOperationData.builder()
        .id(taskInstance.getId().getValue().toString())
        .currentUser(user)
        .candidateGroups(groups)
        .build());
  }

  private void applyCandidateUserRules(
      TaskInstance taskInstance,
      List<TaskAssignmentRuleRequest> assignmentRules,
      Code user
  ) {
    assignmentRules.stream()
        .filter(TaskAssignmentRuleRequest::hasCandidateUsers)
        .forEach(rule -> applyCandidateUserRule(taskInstance, rule, user));
  }

  private void applyCandidateUserRule(TaskInstance taskInstance, TaskAssignmentRuleRequest rule, Code user) {
    assignTask(TaskOperationData.builder()
        .id(taskInstance.getId().getValue().toString())
        .currentUser(user)
        .candidateUsers(rule.getCandidateUsers())
        .priority(rule.getPriority())
        .assignmentMode(rule.getAssignmentMode())
        .build());
  }

  private List<String> normalizeCandidateGroups(Set<String> candidateGroups) {
    if (candidateGroups == null || candidateGroups.isEmpty()) {
      return List.of();
    }
    return candidateGroups.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(group -> !group.isBlank())
        .distinct()
        .toList();
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

  private void configureDueDate(TaskInstance runtimeTask, TaskInstance task, ProcessArtifact artifact) {
    LOGGER.info("DueDate: {} from ProcessArtifact: {}", artifact.getDueDate(), artifact.getKey());
    if (artifact.getDueDate() == null || artifact.getDueDate().isBlank()) {
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

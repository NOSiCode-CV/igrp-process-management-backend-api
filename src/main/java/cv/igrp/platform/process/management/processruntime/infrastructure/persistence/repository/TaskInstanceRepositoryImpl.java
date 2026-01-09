package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskStatistics;
import cv.igrp.platform.process.management.processruntime.domain.models.VariablesExpression;
import cv.igrp.platform.process.management.processruntime.domain.repository.TaskInstanceRepository;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.TaskInstanceEntityRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static cv.igrp.platform.process.management.shared.application.constants.VaribalesOperator.*;


@Repository
public class TaskInstanceRepositoryImpl implements TaskInstanceRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstanceRepositoryImpl.class);

  private final TaskInstanceEntityRepository taskInstanceEntityRepository;
  private final TaskInstanceMapper taskMapper;

  public TaskInstanceRepositoryImpl(TaskInstanceEntityRepository taskInstanceEntityRepository,
                                    TaskInstanceMapper taskMapper) {

    this.taskInstanceEntityRepository = taskInstanceEntityRepository;
    this.taskMapper = taskMapper;
  }


  @Override
  public Optional<TaskInstance> findById(UUID id) {
    return taskInstanceEntityRepository.findById(id).map(taskMapper::toModel);
  }


  @Override
  public Optional<TaskInstance> findByIdWithEvents(UUID id) {
    return taskInstanceEntityRepository.findById(id).map(t -> taskMapper.toModel(t, true));
  }


  @Override
  public void create(TaskInstance taskInstance) {
    taskInstanceEntityRepository.save(taskMapper.toNewTaskEntity(taskInstance));
  }


  @Override
  public void update(TaskInstance taskInstance) {
    var taskInstanceEntity = taskInstanceEntityRepository
        .findById(taskInstance.getId().getValue())
        .orElseThrow(() -> IgrpResponseStatusException.notFound(
            "No Task Instance found with id: " + taskInstance.getId().getValue()));
    taskMapper.toTaskEntity(taskInstance, taskInstanceEntity);
    taskInstanceEntityRepository.save(taskInstanceEntity);
  }


  @Override
  public PageableLista<TaskInstance> findAll(TaskInstanceFilter filter) {

    Specification<TaskInstanceEntity> spec = buildSpecification(filter);

    PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize(),
        Sort.by(Sort.Direction.DESC, "startedAt"));

    final var pageableTask = taskInstanceEntityRepository.findAll(spec, pageRequest);

    List<TaskInstanceEntity> filtered = pageableTask.toList();

    // Apply filter on user and groups
    if (filter.getUser() != null || !filter.getCandidateGroups().isEmpty()) {
      filtered = pageableTask.stream()
          .filter(taskInstanceEntity -> {

            LOGGER.info("filter.getUser(): {}", filter.getUser());

            boolean matchUser = Objects.equals(taskInstanceEntity.getAssignedBy(), filter.getUser().getValue());

            boolean matchGroups = matchesGroup(taskInstanceEntity, filter.getCandidateGroups());

            return matchUser || matchGroups;
          })
          .toList();
    }

    List<TaskInstance> content = filtered
        .stream()
        .map(taskMapper::toModel)
        .toList();

    return new PageableLista<>(
        pageableTask.getNumber(),
        pageableTask.getSize(),
        (long) filtered.size(),
        (int) Math.ceil((double) filtered.size() / filter.getSize()),
        pageableTask.getNumber() == (int) Math.ceil((double) filtered.size() / filter.getSize()) - 1,
        pageableTask.getNumber() == 0,
        content
    );

  }

  private Specification<TaskInstanceEntity> buildSpecification(TaskInstanceFilter filter) {

    LOGGER.debug("Filter: {}", filter);

    Specification<TaskInstanceEntity> spec = (root, query, builder) -> null;

    if (filter.getProcessInstanceId() != null) {
      spec = spec.and((root, query, cb) ->
          cb.equal(root.get("processInstanceId").get("id"), filter.getProcessInstanceId().getValue()));
    }

    if (filter.getProcessNumber() != null) {
      spec = spec.and((root, query, cb) ->
          cb.or(cb.equal(root.get("processInstanceId").get("number"), filter.getProcessNumber().getValue()),
              cb.equal(root.get("processInstanceId").get("businessKey"), filter.getProcessNumber().getValue())));
    }

    if (filter.getApplicationBase() != null) {
      spec = spec.and((root, query, cb) ->
          cb.equal(root.get("processInstanceId").get("applicationBase"), filter.getApplicationBase().getValue()));
    }

    if (filter.getName() != null) {
      spec = spec.and((root, query, cb) ->
          cb.like(root.get("name"), "%" + filter.getName().getValue() + "%"));
    }

    if (filter.getProcessName() != null) {
      spec = spec.and((root, query, cb) ->
          cb.like(root.get("processInstanceId").get("name"), "%" + filter.getProcessName().getValue() + "%"));
    }

    if (filter.getProcessRealeaseKey() != null) {
      spec = spec.and((root, query, cb) ->
          cb.equal(root.get("processInstanceId").get("procReleaseKey"), filter.getProcessRealeaseKey().getValue()));
    }

    if (filter.getStatus() != null) {
      spec = spec.and((root, query, cb) ->
          cb.equal(root.get("status"), filter.getStatus().getCode()));
    }

    if (filter.getDateFrom() != null) {
      spec = spec.and((root, query, cb) ->
          cb.greaterThanOrEqualTo(root.get("startedAt"), filter.getDateFrom().atStartOfDay()));
    }

    if (filter.getDateTo() != null) {
      spec = spec.and((root, query, cb) ->
          cb.lessThanOrEqualTo(root.get("startedAt"), filter.getDateTo().atTime(LocalTime.MAX)));
    }

    if (!filter.getVariablesExpressions().isEmpty()) {
      spec = spec.and((root, query, cb) -> {
        List<Predicate> predicates = filter.getVariablesExpressions()
            .stream()
            .map(expr -> buildVariablePredicate(expr, root, cb))
            .toList();
        return cb.and(predicates.toArray(new Predicate[0]));
      });
    }

    return spec;
  }

  boolean matchesGroup(TaskInstanceEntity t, List<String> filterGroups) {

    LOGGER.info("Task Groups: {}", t.getCandidateGroups());
    LOGGER.info("Filter Groups: {}", filterGroups);

    if (filterGroups == null || filterGroups.isEmpty()) return false;

    List<String> taskGroups =
        Arrays.stream(t.getCandidateGroups().split(","))
            .map(String::trim)
            .toList();

    return taskGroups.stream().anyMatch(filterGroups::contains);
  }


  private Predicate buildVariablePredicate(
      VariablesExpression expr,
      Root<TaskInstanceEntity> root,
      CriteriaBuilder cb
  ) {
    Object value = expr.getValue();

    // Build JSON expression, prepending "variables" as top-level key
    Expression<String> jsonExpr = buildJsonPathExpression(root, cb, expr.getName());

    // Create operator predicate depending on type
    return buildOperatorPredicate(jsonExpr, expr, value, cb);
  }


  private Expression<String> buildJsonPathExpression(
      Root<TaskInstanceEntity> root,
      CriteriaBuilder cb,
      String variableName
  ) {
    String[] pathSegments = variableName.split("\\.");

    Expression<?> expr = root.get("variables");

    for (String key : pathSegments) {
      expr = cb.function(
          "jsonb_extract_path_text",
          String.class,
          expr,
          cb.literal(key)
      );
    }

    return (Expression<String>) expr;
  }


  private Predicate buildOperatorPredicate(
      Expression<String> jsonExpr,
      VariablesExpression expr,
      Object value,
      CriteriaBuilder cb
  ) {
    // ----------------------------
    // NUMBER
    // ----------------------------
    if (value instanceof Number number) {
      Expression<BigDecimal> numExpr = cb.function(
          "to_number",
          BigDecimal.class,
          jsonExpr,
          cb.literal("999999999.999999")
      );
      BigDecimal val = new BigDecimal(number.toString());

      return switch (expr.getOperator()) {
        case EQUALS -> cb.equal(numExpr, val);
        case NOT_EQUALS -> cb.notEqual(numExpr, val);
        case GREATER_THAN -> cb.greaterThan(numExpr, val);
        case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(numExpr, val);
        case LESS_THAN -> cb.lessThan(numExpr, val);
        case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(numExpr, val);
        default -> throw new IllegalArgumentException(
            "Operator " + expr.getOperator() + " not supported for NUMBER"
        );
      };
    }

    // ----------------------------
    // DATE (LocalDate)
    // ----------------------------
    if (value instanceof LocalDate date) {
      Expression<LocalDate> dateExpr = cb.function(
          "to_date",
          LocalDate.class,
          jsonExpr,
          cb.literal("YYYY-MM-DD")
      );

      return switch (expr.getOperator()) {
        case EQUALS -> cb.equal(dateExpr, date);
        case NOT_EQUALS -> cb.notEqual(dateExpr, date);
        case GREATER_THAN -> cb.greaterThan(dateExpr, date);
        case GREATER_THAN_OR_EQUAL -> cb.greaterThanOrEqualTo(dateExpr, date);
        case LESS_THAN -> cb.lessThan(dateExpr, date);
        case LESS_THAN_OR_EQUAL -> cb.lessThanOrEqualTo(dateExpr, date);
        default -> throw new IllegalArgumentException(
            "Operator " + expr.getOperator() + " not supported for DATE"
        );
      };
    }

    // ----------------------------
    // BOOLEAN
    // ----------------------------
    if (value instanceof Boolean bool) {
      return switch (expr.getOperator()) {
        case EQUALS -> cb.equal(jsonExpr, bool.toString());
        case NOT_EQUALS -> cb.notEqual(jsonExpr, bool.toString());
        default -> throw new IllegalArgumentException(
            "Operator " + expr.getOperator() + " not supported for BOOLEAN"
        );
      };
    }

    // ----------------------------
    // STRING (default)
    // ----------------------------
    String strVal = value.toString();
    return switch (expr.getOperator()) {
      case EQUALS -> cb.equal(jsonExpr, strVal);
      case EQUALS_IGNORE_CASE -> cb.equal(cb.lower(jsonExpr), strVal.toLowerCase());
      case NOT_EQUALS -> cb.notEqual(jsonExpr, strVal);
      case NOT_EQUALS_IGNORE_CASE -> cb.notEqual(cb.lower(jsonExpr), strVal.toLowerCase());
      case LIKE -> cb.like(jsonExpr, "%" + strVal + "%");
      case LIKE_IGNORE_CASE -> cb.like(cb.lower(jsonExpr), "%" + strVal.toLowerCase() + "%");
      default -> throw new IllegalArgumentException(
          "Operator " + expr.getOperator() + " not supported for STRING"
      );
    };
  }


  @Override
  public TaskStatistics getGlobalTaskStatistics() {

    long total = taskInstanceEntityRepository.count();

    long available = countByStatus(TaskInstanceStatus.CREATED);
    long assigned = countByStatus(TaskInstanceStatus.ASSIGNED);
    long suspended = countByStatus(TaskInstanceStatus.SUSPENDED);
    long completed = countByStatus(TaskInstanceStatus.COMPLETED);
    long canceled = countByStatus(TaskInstanceStatus.CANCELED);

    return TaskStatistics.builder()
        .totalTaskInstances(total)
        .totalAvailableTasks(available)
        .totalAssignedTasks(assigned)
        .totalSuspendedTasks(suspended)
        .totalCompletedTasks(completed)
        .totalCanceledTasks(canceled)
        .build();
  }


  private long countByStatus(TaskInstanceStatus status) {
    return taskInstanceEntityRepository.count((root, query, cb) -> cb.equal(root.get("status"), status));
  }


  @Override
  public TaskStatistics getTaskStatisticsByUser(Code user) {

    LOGGER.debug("User: {}", user.getValue());

    // base: tasks related to user
    Specification<TaskInstanceEntity> userSpec = (root, q, cb) ->
        cb.or(
            cb.equal(root.get("assignedBy"), user.getValue()),
            cb.equal(root.get("endedBy"), user.getValue())
        );

    // total: all tasks, general context, not only user
    long total = taskInstanceEntityRepository.count();

    long available = countByStatus(userSpec, TaskInstanceStatus.CREATED);
    long assigned = countByStatusAndField(userSpec, TaskInstanceStatus.ASSIGNED, "assignedBy", user.getValue());
    long suspended = countByStatusAndField(userSpec, TaskInstanceStatus.SUSPENDED, "assignedBy", user.getValue());
    long completed = countByStatusAndField(userSpec, TaskInstanceStatus.COMPLETED, "endedBy", user.getValue());
    long canceled = countByStatusAndField(userSpec, TaskInstanceStatus.CANCELED, "endedBy", user.getValue());

    return TaskStatistics.builder()
        .totalTaskInstances(total)
        .totalAvailableTasks(available)
        .totalAssignedTasks(assigned)
        .totalSuspendedTasks(suspended)
        .totalCompletedTasks(completed)
        .totalCanceledTasks(canceled)
        .build();
  }

  private long countByStatus(Specification<TaskInstanceEntity> base, TaskInstanceStatus status) {
    return taskInstanceEntityRepository.count(base.and((root, q, cb) -> cb.equal(root.get("status"), status)));
  }

  private long countByStatusAndField(Specification<TaskInstanceEntity> base,
                                     TaskInstanceStatus status, String field, String value) {
    return taskInstanceEntityRepository.count(
        base.and((root, q, cb) -> cb.equal(root.get("status"), status))
            .and((root, q, cb) -> cb.equal(root.get(field), value))
    );
  }

  @Override
  public Optional<TaskInstance> findByExternalId(String id) {
    return taskInstanceEntityRepository.findByExternalId(id).map(taskMapper::toModel);
  }

}

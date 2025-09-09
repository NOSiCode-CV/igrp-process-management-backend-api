/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.interfaces.rest;

import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.framework.core.domain.QueryBus;
import cv.igrp.framework.stereotype.IgrpController;
import cv.igrp.platform.process.management.processruntime.application.commands.AssignTaskCommand;
import cv.igrp.platform.process.management.processruntime.application.commands.ClaimTaskCommand;
import cv.igrp.platform.process.management.processruntime.application.commands.CompleteTaskCommand;
import cv.igrp.platform.process.management.processruntime.application.commands.UnClaimTaskCommand;
import cv.igrp.platform.process.management.processruntime.application.dto.*;
import cv.igrp.platform.process.management.processruntime.application.queries.*;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@IgrpController
@RestController
@RequestMapping(path = "tasks-instances")
@Tag(name = "TaskInstances", description = "Task Instances Management")
public class TaskInstancesController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstancesController.class);


  private final CommandBus commandBus;
  private final QueryBus queryBus;


  public TaskInstancesController(
    CommandBus commandBus, QueryBus queryBus
  ) {
    this.commandBus = commandBus;
    this.queryBus = queryBus;
  }

  @GetMapping(
  )
  @Operation(
    summary = "GET method to handle operations for listTaskInstances",
    description = "GET method to handle operations for listTaskInstances",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of task instances",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceListPageDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceListPageDTO> listTaskInstances(
    @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
    @RequestParam(value = "processNumber", required = false) String processNumber,
    @RequestParam(value = "processName", required = false) String processName,
    @RequestParam(value = "user", required = false) String user,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "dateFrom", required = false) String dateFrom,
    @RequestParam(value = "dateTo", required = false) String dateTo,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      LOGGER.debug("Operation started");

      final var query = new ListTaskInstancesQuery(processInstanceId, processNumber, processName, user, status, dateFrom, dateTo, page, size);

      ResponseEntity<TaskInstanceListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}"
  )
  @Operation(
    summary = "GET method to handle operations for getTaskInstanceById",
    description = "GET method to handle operations for getTaskInstanceById",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Task Instance Info",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceDTO> getTaskInstanceById(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new GetTaskInstanceByIdQuery(id);

      ResponseEntity<TaskInstanceDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/claim"
  )
  @Operation(
    summary = "POST method to handle operations for claimTask",
    description = "POST method to handle operations for claimTask",
    responses = {
      @ApiResponse(
          responseCode = "204",
          description = "No Content",
          content = @Content(
              mediaType = "",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )

  public ResponseEntity<?> claimTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new ClaimTaskCommand(id);

       ResponseEntity<?> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/unclaim"
  )
  @Operation(
    summary = "POST method to handle operations for unClaimTask",
    description = "POST method to handle operations for unClaimTask",
    responses = {
      @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = @Content(
              mediaType = "",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )

  public ResponseEntity<?> unClaimTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new UnClaimTaskCommand(id);

       ResponseEntity<?> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/assign"
  )
  @Operation(
    summary = "POST method to handle operations for assignTask",
    description = "POST method to handle operations for assignTask",
    responses = {
      @ApiResponse(
          responseCode = "204",
          description = "No content",
          content = @Content(
              mediaType = "",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )

  public ResponseEntity<?> assignTask(@Valid @RequestBody AssignTaskDTO assignTaskRequest
    , @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new AssignTaskCommand(assignTaskRequest, id);

       ResponseEntity<?> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/complete"
  )
  @Operation(
    summary = "POST method to handle operations for completeTask",
    description = "POST method to handle operations for completeTask",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceDTO> completeTask(@Valid @RequestBody TaskDataDTO completeTaskRequest
    , @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new CompleteTaskCommand(completeTaskRequest, id);

       ResponseEntity<TaskInstanceDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "me"
  )
  @Operation(
    summary = "GET method to handle operations for getAllMyTasks",
    description = "GET method to handle operations for getAllMyTasks",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List All My Tasks",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceListPageDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceListPageDTO> getAllMyTasks(
    @RequestParam(value = "processInstanceId", required = false) String processInstanceId,
    @RequestParam(value = "processNumber", required = false) String processNumber,
    @RequestParam(value = "processName", required = false) String processName,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "dateFrom", required = false) String dateFrom,
    @RequestParam(value = "dateTo", required = false) String dateTo,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      LOGGER.debug("Operation started");

      final var query = new GetAllMyTasksQuery(processInstanceId, processNumber, processName, status, dateFrom, dateTo, page, size);

      ResponseEntity<TaskInstanceListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "status"
  )
  @Operation(
    summary = "GET method to handle operations for listTaskInstanceStatus",
    description = "GET method to handle operations for listTaskInstanceStatus",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ConfigParameterDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<List<ConfigParameterDTO>> listTaskInstanceStatus(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new ListTaskInstanceStatusQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "event_type"
  )
  @Operation(
    summary = "GET method to handle operations for listTaskInstanceEventType",
    description = "GET method to handle operations for listTaskInstanceEventType",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ConfigParameterDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<List<ConfigParameterDTO>> listTaskInstanceEventType(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new ListTaskInstanceEventTypeQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}/variables"
  )
  @Operation(
    summary = "GET method to handle operations for getTaskVariablesById",
    description = "GET method to handle operations for getTaskVariablesById",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskVariableDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<List<TaskVariableDTO>> getTaskVariablesById(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new GetTaskVariablesByIdQuery(id);

      ResponseEntity<List<TaskVariableDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "stats"
  )
  @Operation(
    summary = "GET method to handle operations for getTaskInstanceStatistics",
    description = "GET method to handle operations for getTaskInstanceStatistics",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceStatsDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceStatsDTO> getTaskInstanceStatistics(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new GetTaskInstanceStatisticsQuery();

      ResponseEntity<TaskInstanceStatsDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "stats/me"
  )
  @Operation(
    summary = "GET method to handle operations for getMyTaskInstanceStatistics",
    description = "GET method to handle operations for getMyTaskInstanceStatistics",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = TaskInstanceStatsDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<TaskInstanceStatsDTO> getMyTaskInstanceStatistics(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new GetMyTaskInstanceStatisticsQuery();

      ResponseEntity<TaskInstanceStatsDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}

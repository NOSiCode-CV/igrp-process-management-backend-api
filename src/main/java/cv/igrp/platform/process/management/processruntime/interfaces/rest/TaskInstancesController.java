/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.interfaces.rest;

import cv.igrp.framework.stereotype.IgrpController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import cv.igrp.framework.core.domain.QueryBus;
import cv.igrp.platform.process.management.processruntime.application.queries.*;
import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.platform.process.management.processruntime.application.commands.*;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.AssignTaskDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskDataDTO;
import java.util.List;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceStatsDTO;

@IgrpController
@RestController
@RequestMapping(path = "tasks-instances")
@Tag(name = "TaskInstances", description = "Task Instances Management")
public class TaskInstancesController {


  private final QueryBus queryBus;
  private final CommandBus commandBus;

  public TaskInstancesController(QueryBus queryBus, CommandBus commandBus) {
          this.queryBus = queryBus;
          this.commandBus = commandBus;
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
    @RequestParam(value = "applicationBase", required = false) String applicationBase,
    @RequestParam(value = "candidateGroups", required = false) String candidateGroups,
    @RequestParam(value = "user", required = false) String user,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "dateFrom", required = false) String dateFrom,
    @RequestParam(value = "dateTo", required = false) String dateTo,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      final var query = new ListTaskInstancesQuery(processInstanceId, processNumber, processName, applicationBase, candidateGroups, user, status, dateFrom, dateTo, page, size);

      ResponseEntity<TaskInstanceListPageDTO> response = queryBus.handle(query);

      return response;
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

      final var query = new GetTaskInstanceByIdQuery(id);

      ResponseEntity<TaskInstanceDTO> response = queryBus.handle(query);

      return response;
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

      final var command = new ClaimTaskCommand(id);

       ResponseEntity<?> response = commandBus.send(command);

       return response;
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

      final var command = new UnClaimTaskCommand(id);

       ResponseEntity<?> response = commandBus.send(command);

       return response;
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

      final var command = new AssignTaskCommand(assignTaskRequest, id);

       ResponseEntity<?> response = commandBus.send(command);

       return response;
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

      final var command = new CompleteTaskCommand(completeTaskRequest, id);

       ResponseEntity<TaskInstanceDTO> response = commandBus.send(command);

       return response;
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
    @RequestParam(value = "applicationBase", required = false) String applicationBase,
    @RequestParam(value = "processName", required = false) String processName,
    @RequestParam(value = "candidateGroups", required = false) String candidateGroups,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "dateFrom", required = false) String dateFrom,
    @RequestParam(value = "dateTo", required = false) String dateTo,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      final var query = new GetAllMyTasksQuery(processInstanceId, processNumber, applicationBase, processName, candidateGroups, status, dateFrom, dateTo, page, size);

      ResponseEntity<TaskInstanceListPageDTO> response = queryBus.handle(query);

      return response;
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

      final var query = new ListTaskInstanceStatusQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      return response;
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

      final var query = new ListTaskInstanceEventTypeQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      return response;
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

      final var query = new GetTaskVariablesByIdQuery(id);

      ResponseEntity<List<TaskVariableDTO>> response = queryBus.handle(query);

      return response;
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

      final var query = new GetTaskInstanceStatisticsQuery();

      ResponseEntity<TaskInstanceStatsDTO> response = queryBus.handle(query);

      return response;
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

      final var query = new GetMyTaskInstanceStatisticsQuery();

      ResponseEntity<TaskInstanceStatsDTO> response = queryBus.handle(query);

      return response;
  }

   @PostMapping(
   value = "{id}/save"
  )
  @Operation(
    summary = "POST method to handle operations for saveTask",
    description = "POST method to handle operations for saveTask",
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

  public ResponseEntity<TaskInstanceDTO> saveTask(@Valid @RequestBody TaskDataDTO saveTaskRequest
    , @PathVariable(value = "id") String id)
  {

      final var command = new SaveTaskCommand(saveTaskRequest, id);

       ResponseEntity<TaskInstanceDTO> response = commandBus.send(command);

       return response;
  }

}

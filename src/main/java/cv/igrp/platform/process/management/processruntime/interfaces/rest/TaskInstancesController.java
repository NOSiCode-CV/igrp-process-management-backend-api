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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.framework.core.domain.QueryBus;
import cv.igrp.platform.process.management.processruntime.application.commands.*;
import cv.igrp.platform.process.management.processruntime.application.queries.*;


import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;

@IgrpController
@RestController
@RequestMapping(path = "tasks")
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
                  implementation = TaskInstanceListaPageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<TaskInstanceListaPageDTO> listTaskInstances(
    @RequestParam(value = "processId", required = false) String processId,
    @RequestParam(value = "user", required = false) String user)
  {

      LOGGER.debug("Operation started");

      final var query = new ListTaskInstancesQuery(processId, user);

      ResponseEntity<TaskInstanceListaPageDTO> response = queryBus.handle(query);

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
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )
  
  public ResponseEntity<String> claimTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new ClaimTaskCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

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
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )
  
  public ResponseEntity<String> unClaimTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new UnClaimTaskCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

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
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )
  
  public ResponseEntity<String> assignTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new AssignTaskCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/unassign"
  )
  @Operation(
    summary = "POST method to handle operations for unAssignTask",
    description = "POST method to handle operations for unAssignTask",
    responses = {
      @ApiResponse(
          responseCode = "204",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )
  
  public ResponseEntity<String> unAssignTask(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new UnAssignTaskCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}
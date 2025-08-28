/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.interfaces.rest;

import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.framework.core.domain.QueryBus;
import cv.igrp.framework.stereotype.IgrpController;
import cv.igrp.platform.process.management.processruntime.application.commands.StartProcessInstanceCommand;
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
@RequestMapping(path = "process-instances")
@Tag(name = "ProcessInstance", description = "Process Instance Management")
public class ProcessInstanceController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessInstanceController.class);


  private final CommandBus commandBus;
  private final QueryBus queryBus;


  public ProcessInstanceController(
    CommandBus commandBus, QueryBus queryBus
  ) {
    this.commandBus = commandBus;
    this.queryBus = queryBus;
  }

  @GetMapping(
  )
  @Operation(
    summary = "GET method to handle operations for listProcessInstances",
    description = "GET method to handle operations for listProcessInstances",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of process instances",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceListPageDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<ProcessInstanceListPageDTO> listProcessInstances(
    @RequestParam(value = "number", required = false) String number,
    @RequestParam(value = "procReleaseKey", required = false) String procReleaseKey,
    @RequestParam(value = "procReleaseId", required = false) String procReleaseId,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "searchTerms", required = false) String searchTerms,
    @RequestParam(value = "applicationBase", required = false) String applicationBase,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      LOGGER.debug("Operation started");

      final var query = new ListProcessInstancesQuery(number, procReleaseKey, procReleaseId, status, searchTerms, applicationBase, page, size);

      ResponseEntity<ProcessInstanceListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
  )
  @Operation(
    summary = "POST method to handle operations for startProcessInstance",
    description = "POST method to handle operations for startProcessInstance",
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Started process instance",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<ProcessInstanceDTO> startProcessInstance(@Valid @RequestBody StartProcessRequestDTO startProcessInstanceRequest
    )
  {

      LOGGER.debug("Operation started");

      final var command = new StartProcessInstanceCommand(startProcessInstanceRequest);

       ResponseEntity<ProcessInstanceDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}"
  )
  @Operation(
    summary = "GET method to handle operations for getProcessInstanceById",
    description = "GET method to handle operations for getProcessInstanceById",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Process Instance Info",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<ProcessInstanceDTO> getProcessInstanceById(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new GetProcessInstanceByIdQuery(id);

      ResponseEntity<ProcessInstanceDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "status"
  )
  @Operation(
    summary = "GET method to handle operations for listProcessInstanceStatus",
    description = "GET method to handle operations for listProcessInstanceStatus",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of process instance status",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ConfigParameterDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<List<ConfigParameterDTO>> listProcessInstanceStatus(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new ListProcessInstanceStatusQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}/task-status"
  )
  @Operation(
    summary = "GET method to handle operations for getTaskStatus",
    description = "GET method to handle operations for getTaskStatus",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of task status of the process instance",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceTaskStatusDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<List<ProcessInstanceTaskStatusDTO>> getTaskStatus(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new GetTaskStatusQuery(id);

      ResponseEntity<List<ProcessInstanceTaskStatusDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "stats"
  )
  @Operation(
    summary = "GET method to handle operations for getProcessInstanceStatistics",
    description = "GET method to handle operations for getProcessInstanceStatistics",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceStatsDTO.class,
                  type = "object")
          )
      )
    }
  )

  public ResponseEntity<ProcessInstanceStatsDTO> getProcessInstanceStatistics(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new GetProcessInstanceStatisticsQuery();

      ResponseEntity<ProcessInstanceStatsDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}

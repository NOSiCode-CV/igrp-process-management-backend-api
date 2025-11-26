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
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.StartProcessRequestDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import java.util.List;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceStatsDTO;
import cv.igrp.platform.process.management.shared.application.dto.ProcessEventDTO;
import cv.igrp.platform.process.management.shared.application.dto.StartProcessDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.CreateProcessRequestDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessVariablesRequestDTO;

@IgrpController
@RestController
@RequestMapping(path = "process-instances")
@Tag(name = "ProcessInstance", description = "Process Instance Management")
public class ProcessInstanceController {

  
  private final QueryBus queryBus;
  private final CommandBus commandBus;

  public ProcessInstanceController(QueryBus queryBus, CommandBus commandBus) {
          this.queryBus = queryBus;
          this.commandBus = commandBus;
  }
   @GetMapping(
  )
  @Operation(
    summary = "List process instances",
    description = "List process instances",
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

      final var query = new ListProcessInstancesQuery(number, procReleaseKey, procReleaseId, status, searchTerms, applicationBase, page, size);

      ResponseEntity<ProcessInstanceListPageDTO> response = queryBus.handle(query);

      return response;
  }

   @PostMapping(
   value = "start-deprecated"
  )
  @Operation(
    summary = "Start process instance",
    description = "Start process instance",
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

      final var command = new StartProcessInstanceCommand(startProcessInstanceRequest);

       ResponseEntity<ProcessInstanceDTO> response = commandBus.send(command);

       return response;
  }

   @GetMapping(
   value = "{id}"
  )
  @Operation(
    summary = "Get process instance by id",
    description = "Get process instance by id",
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

      final var query = new GetProcessInstanceByIdQuery(id);

      ResponseEntity<ProcessInstanceDTO> response = queryBus.handle(query);

      return response;
  }

   @GetMapping(
   value = "status"
  )
  @Operation(
    summary = "List process instance status",
    description = "List process instance status",
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

      final var query = new ListProcessInstanceStatusQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      return response;
  }

   @GetMapping(
   value = "{id}/task-status"
  )
  @Operation(
    summary = "Get task status",
    description = "Get task status",
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

      final var query = new GetTaskStatusQuery(id);

      ResponseEntity<List<ProcessInstanceTaskStatusDTO>> response = queryBus.handle(query);

      return response;
  }

   @GetMapping(
   value = "stats"
  )
  @Operation(
    summary = "Get process instance statistics",
    description = "Get process instance statistics",
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

      final var query = new GetProcessInstanceStatisticsQuery();

      ResponseEntity<ProcessInstanceStatsDTO> response = queryBus.handle(query);

      return response;
  }

   @PostMapping(
   value = "event"
  )
  @Operation(
    summary = "Trigger process event",
    description = "Trigger process event",
    responses = {
      @ApiResponse(
          responseCode = "200",
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
  
  public ResponseEntity<String> triggerProcessEvent(@Valid @RequestBody ProcessEventDTO triggerProcessEventRequest
    )
  {

      final var command = new TriggerProcessEventCommand(triggerProcessEventRequest);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

   @PostMapping(
   value = "start-event"
  )
  @Operation(
    summary = "Start process event",
    description = "Start process event",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessInstanceDTO> startProcessEvent(@Valid @RequestBody StartProcessDTO startProcessEventRequest
    )
  {

      final var command = new StartProcessEventCommand(startProcessEventRequest);

       ResponseEntity<ProcessInstanceDTO> response = commandBus.send(command);

       return response;
  }

   @PostMapping(
  )
  @Operation(
    summary = "Create process instance",
    description = "Create process instance",
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessInstanceDTO> createProcessInstance(@Valid @RequestBody CreateProcessRequestDTO createProcessInstanceRequest
    )
  {

      final var command = new CreateProcessInstanceCommand(createProcessInstanceRequest);

       ResponseEntity<ProcessInstanceDTO> response = commandBus.send(command);

       return response;
  }

   @PostMapping(
   value = "{id}/start"
  )
  @Operation(
    summary = "Start process instance by id",
    description = "Start process instance by id",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessInstanceDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessInstanceDTO> startProcessInstanceById(@Valid @RequestBody ProcessVariablesRequestDTO startProcessInstanceByIdRequest
    , @PathVariable(value = "id") String id)
  {

      final var command = new StartProcessInstanceByIdCommand(startProcessInstanceByIdRequest, id);

       ResponseEntity<ProcessInstanceDTO> response = commandBus.send(command);

       return response;
  }

}
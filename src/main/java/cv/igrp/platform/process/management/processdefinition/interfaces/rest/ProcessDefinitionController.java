/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.interfaces.rest;

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
import cv.igrp.platform.process.management.processdefinition.application.queries.*;
import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.platform.process.management.processdefinition.application.commands.*;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentRequestDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import java.util.List;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.SequenceRequestDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.AssignProcessDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessPackageDTO;

@IgrpController
@RestController
@RequestMapping(path = "process-definitions")
@Tag(name = "ProcessDefinition", description = "Process Definition Management")
public class ProcessDefinitionController {

  
  private final QueryBus queryBus;
  private final CommandBus commandBus;

  public ProcessDefinitionController(QueryBus queryBus, CommandBus commandBus) {
          this.queryBus = queryBus;
          this.commandBus = commandBus;
  }
      @PostMapping(
   value = "deploy"
  )
  @Operation(
    summary = "POST method to handle operations for Deploy process",
    description = "POST method to handle operations for Deploy process",
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Process deploymeny",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessDeploymentDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessDeploymentDTO> deployProcess(@Valid @RequestBody ProcessDeploymentRequestDTO deployProcessRequest
    )
  {

      final var command = new DeployProcessCommand(deployProcessRequest);

       ResponseEntity<ProcessDeploymentDTO> response = commandBus.send(command);

       return response;
  }

      @GetMapping(
  )
  @Operation(
    summary = "GET method to handle operations for List deployments",
    description = "GET method to handle operations for List deployments",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of deployments",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessDeploymentListPageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessDeploymentListPageDTO> listDeployments(
    @RequestParam(value = "applicationBase", required = false) String applicationBase,
    @RequestParam(value = "processName", required = false) String processName,
    @RequestParam(value = "page", defaultValue = "0") Integer page,
    @RequestParam(value = "size", defaultValue = "20") Integer size,
    @RequestParam(value = "filterByCurrentUser", required = false) boolean filterByCurrentUser,
    @RequestParam(value = "candidateGroups", required = false) String candidateGroups)
  {

      final var query = new ListDeploymentsQuery(applicationBase, processName, page, size, filterByCurrentUser, candidateGroups);

      ResponseEntity<ProcessDeploymentListPageDTO> response = queryBus.handle(query);

      return response;
  }

      @PutMapping(
   value = "{id}/artifacts/{taskKey}"
  )
  @Operation(
    summary = "PUT method to handle operations for Configure artifact",
    description = "PUT method to handle operations for Configure artifact",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Persisted Project Artifact",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessArtifactDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessArtifactDTO> configureArtifact(@Valid @RequestBody ProcessArtifactRequestDTO configureArtifactRequest
    , @PathVariable(value = "id") String id,@PathVariable(value = "taskKey") String taskKey)
  {

      final var command = new ConfigureArtifactCommand(configureArtifactRequest, id, taskKey);

       ResponseEntity<ProcessArtifactDTO> response = commandBus.send(command);

       return response;
  }

      @DeleteMapping(
   value = "artifacts/{id}"
  )
  @Operation(
    summary = "DELETE method to handle operations for Delete artifact",
    description = "DELETE method to handle operations for Delete artifact",
    responses = {
      @ApiResponse(
          responseCode = "204",
          description = "No Content",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = String.class,
                  type = "String")
          )
      )
    }
  )
  
  public ResponseEntity<String> deleteArtifact(
    @PathVariable(value = "id") String id)
  {

      final var command = new DeleteArtifactCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

      @GetMapping(
   value = "{id}/artifacts"
  )
  @Operation(
    summary = "GET method to handle operations for Get artifacts by process definition id",
    description = "GET method to handle operations for Get artifacts by process definition id",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of Process Artifacts",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessArtifactDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ProcessArtifactDTO>> getArtifactsByProcessDefinitionId(
    @PathVariable(value = "id") String id)
  {

      final var query = new GetArtifactsByProcessDefinitionIdQuery(id);

      ResponseEntity<List<ProcessArtifactDTO>> response = queryBus.handle(query);

      return response;
  }

      @GetMapping(
   value = "{id}/deployed-artifacts"
  )
  @Operation(
    summary = "GET method to handle operations for Get deployed artifacts by process definition id",
    description = "GET method to handle operations for Get deployed artifacts by process definition id",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of Deployed Process Artifact",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessArtifactDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ProcessArtifactDTO>> getDeployedArtifactsByProcessDefinitionId(
    @PathVariable(value = "id") String id)
  {

      final var query = new GetDeployedArtifactsByProcessDefinitionIdQuery(id);

      ResponseEntity<List<ProcessArtifactDTO>> response = queryBus.handle(query);

      return response;
  }

      @GetMapping(
   value = "{processDefinitionKey}/sequence"
  )
  @Operation(
    summary = "GET method to handle operations for Get process sequence",
    description = "GET method to handle operations for Get process sequence",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessSequenceDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessSequenceDTO> getProcessSequence(
    @PathVariable(value = "processDefinitionKey") String processDefinitionKey)
  {

      final var query = new GetProcessSequenceQuery(processDefinitionKey);

      ResponseEntity<ProcessSequenceDTO> response = queryBus.handle(query);

      return response;
  }

      @PostMapping(
   value = "{processDefinitionKey}/sequence"
  )
  @Operation(
    summary = "POST method to handle operations for Create process sequence",
    description = "POST method to handle operations for Create process sequence",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessSequenceDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessSequenceDTO> createProcessSequence(@Valid @RequestBody SequenceRequestDTO createProcessSequenceRequest
    , @PathVariable(value = "processDefinitionKey") String processDefinitionKey)
  {

      final var command = new CreateProcessSequenceCommand(createProcessSequenceRequest, processDefinitionKey);

       ResponseEntity<ProcessSequenceDTO> response = commandBus.send(command);

       return response;
  }

      @PostMapping(
   value = "{id}/assign"
  )
  @Operation(
    summary = "POST method to handle operations for Assign process definition",
    description = "POST method to handle operations for Assign process definition",
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
  
  public ResponseEntity<String> assignProcessDefinition(@Valid @RequestBody AssignProcessDTO assignProcessDefinitionRequest
    , @PathVariable(value = "id") String id)
  {

      final var command = new AssignProcessDefinitionCommand(assignProcessDefinitionRequest, id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

      @GetMapping(
   value = "{id}/export"
  )
  @Operation(
    summary = "GET method to handle operations for Export process definition",
    description = "GET method to handle operations for Export process definition",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessPackageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessPackageDTO> exportProcessDefinition(
    @PathVariable(value = "id") String id)
  {

      final var query = new ExportProcessDefinitionQuery(id);

      ResponseEntity<ProcessPackageDTO> response = queryBus.handle(query);

      return response;
  }

      @PostMapping(
   value = "{id}/import"
  )
  @Operation(
    summary = "POST method to handle operations for Import process definition",
    description = "POST method to handle operations for Import process definition",
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
  
  public ResponseEntity<String> importProcessDefinition(@Valid @RequestBody ProcessPackageDTO importProcessDefinitionRequest
    , @PathVariable(value = "id") String id)
  {

      final var command = new ImportProcessDefinitionCommand(importProcessDefinitionRequest, id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

      @DeleteMapping(
   value = "{id}/archive"
  )
  @Operation(
    summary = "DELETE method to handle operations for Archive process definition",
    description = "DELETE method to handle operations for Archive process definition",
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
  
  public ResponseEntity<String> archiveProcessDefinition(
    @PathVariable(value = "id") String id)
  {

      final var command = new ArchiveProcessDefinitionCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

      @PostMapping(
   value = "{id}/unarchive"
  )
  @Operation(
    summary = "POST method to handle operations for Un archive process definition",
    description = "POST method to handle operations for Un archive process definition",
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
  
  public ResponseEntity<String> unArchiveProcessDefinition(
    @PathVariable(value = "id") String id)
  {

      final var command = new UnArchiveProcessDefinitionCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

      @PostMapping(
   value = "{id}/unassign"
  )
  @Operation(
    summary = "POST method to handle operations for Un assign process definition",
    description = "POST method to handle operations for Un assign process definition",
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
  
  public ResponseEntity<String> unAssignProcessDefinition(@Valid @RequestBody AssignProcessDTO unAssignProcessDefinitionRequest
    , @PathVariable(value = "id") String id)
  {

      final var command = new UnAssignProcessDefinitionCommand(unAssignProcessDefinitionRequest, id);

       ResponseEntity<String> response = commandBus.send(command);

       return response;
  }

}
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.CommandBus;
import cv.igrp.framework.core.domain.QueryBus;
import cv.igrp.platform.process.management.processdefinition.application.commands.*;
import cv.igrp.platform.process.management.processdefinition.application.queries.*;


import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentRequestDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import java.util.List;

@IgrpController
@RestController
@RequestMapping(path = "process-definitions")
@Tag(name = "ProcessDefinition", description = "Process Definition Management")
public class ProcessDefinitionController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionController.class);

  
  private final CommandBus commandBus;
  private final QueryBus queryBus;

  
  public ProcessDefinitionController(
    CommandBus commandBus, QueryBus queryBus
  ) {
    this.commandBus = commandBus;
    this.queryBus = queryBus;
  }

  @PostMapping(
    value = "deploy"
  )
  @Operation(
    summary = "POST method to handle operations for deployProcess",
    description = "POST method to handle operations for deployProcess",
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

      LOGGER.debug("Operation started");

      final var command = new DeployProcessCommand(deployProcessRequest);

       ResponseEntity<ProcessDeploymentDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
  )
  @Operation(
    summary = "GET method to handle operations for listDeployments",
    description = "GET method to handle operations for listDeployments",
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
    @RequestParam(value = "size", defaultValue = "20") Integer size)
  {

      LOGGER.debug("Operation started");

      final var query = new ListDeploymentsQuery(applicationBase, processName, page, size);

      ResponseEntity<ProcessDeploymentListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{id}/artifacts"
  )
  @Operation(
    summary = "POST method to handle operations for createArtifact",
    description = "POST method to handle operations for createArtifact",
    responses = {
      @ApiResponse(
          responseCode = "201",
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
  
  public ResponseEntity<ProcessArtifactDTO> createArtifact(@Valid @RequestBody ProcessArtifactRequestDTO createArtifactRequest
    , @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new CreateArtifactCommand(createArtifactRequest, id);

       ResponseEntity<ProcessArtifactDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @DeleteMapping(
    value = "artifacts/{id}"
  )
  @Operation(
    summary = "DELETE method to handle operations for deleteArtifact",
    description = "DELETE method to handle operations for deleteArtifact",
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

      LOGGER.debug("Operation started");

      final var command = new DeleteArtifactCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}/artifacts"
  )
  @Operation(
    summary = "GET method to handle operations for getArtifactsByProcessDefinitionId",
    description = "GET method to handle operations for getArtifactsByProcessDefinitionId",
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

      LOGGER.debug("Operation started");

      final var query = new GetArtifactsByProcessDefinitionIdQuery(id);

      ResponseEntity<List<ProcessArtifactDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}/deployed-artifacts"
  )
  @Operation(
    summary = "GET method to handle operations for getDeployedArtifactsByProcessDefinitionId",
    description = "GET method to handle operations for getDeployedArtifactsByProcessDefinitionId",
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

      LOGGER.debug("Operation started");

      final var query = new GetDeployedArtifactsByProcessDefinitionIdQuery(id);

      ResponseEntity<List<ProcessArtifactDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}
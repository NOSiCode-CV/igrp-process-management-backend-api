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

  @DeleteMapping(
    value = "{deploymentId}"
  )
  @Operation(
    summary = "DELETE method to handle operations for undeployProcess",
    description = "DELETE method to handle operations for undeployProcess",
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
  
  public ResponseEntity<String> undeployProcess(
    @PathVariable(value = "deploymentId") String deploymentId)
  {

      LOGGER.debug("Operation started");

      final var command = new UndeployProcessCommand(deploymentId);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}
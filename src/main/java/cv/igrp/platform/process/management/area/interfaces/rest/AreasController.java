/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.interfaces.rest;

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
import cv.igrp.platform.process.management.area.application.commands.*;
import cv.igrp.platform.process.management.area.application.queries.*;


import cv.igrp.platform.process.management.area.application.dto.AreaListPageDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionListPageDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionRequestDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;
import java.util.List;
import cv.igrp.platform.process.management.shared.application.dto.ConfigParameterDTO;

@IgrpController
@RestController
@RequestMapping(path = "areas")
@Tag(name = "Areas", description = "Area and Process Definition Management")
public class AreasController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AreasController.class);

  
  private final CommandBus commandBus;
  private final QueryBus queryBus;

  
  public AreasController(
    CommandBus commandBus, QueryBus queryBus
  ) {
    this.commandBus = commandBus;
    this.queryBus = queryBus;
  }

  @GetMapping(
  )
  @Operation(
    summary = "GET method to handle operations for listAreas",
    description = "GET method to handle operations for listAreas",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of areas",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = AreaListPageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<AreaListPageDTO> listAreas(
    @RequestParam(value = "code", required = false) String code,
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "applicationBase", required = false) String applicationBase,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "parentId", required = false) String parentId,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size)
  {

      LOGGER.debug("Operation started");

      final var query = new ListAreasQuery(code, name, applicationBase, status, parentId, page, size);

      ResponseEntity<AreaListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
  )
  @Operation(
    summary = "POST method to handle operations for createArea",
    description = "POST method to handle operations for createArea",
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Persisted Area",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = AreaDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<AreaDTO> createArea(@Valid @RequestBody AreaRequestDTO createAreaRequest
    )
  {

      LOGGER.debug("Operation started");

      final var command = new CreateAreaCommand(createAreaRequest);

       ResponseEntity<AreaDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}"
  )
  @Operation(
    summary = "GET method to handle operations for getAreaById",
    description = "GET method to handle operations for getAreaById",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Area Info",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = AreaDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<AreaDTO> getAreaById(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new GetAreaByIdQuery(id);

      ResponseEntity<AreaDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PutMapping(
    value = "{id}"
  )
  @Operation(
    summary = "PUT method to handle operations for updateArea",
    description = "PUT method to handle operations for updateArea",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Updated area",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = AreaDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<AreaDTO> updateArea(@Valid @RequestBody AreaRequestDTO updateAreaRequest
    , @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new UpdateAreaCommand(updateAreaRequest, id);

       ResponseEntity<AreaDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @DeleteMapping(
    value = "{id}"
  )
  @Operation(
    summary = "DELETE method to handle operations for deleteArea",
    description = "DELETE method to handle operations for deleteArea",
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
  
  public ResponseEntity<String> deleteArea(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var command = new DeleteAreaCommand(id);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{areaId}/process-definitions"
  )
  @Operation(
    summary = "GET method to handle operations for listProcessDefinitions",
    description = "GET method to handle operations for listProcessDefinitions",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of process definition of an area",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessDefinitionListPageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessDefinitionListPageDTO> listProcessDefinitions(
    @RequestParam(value = "processKey", required = false) String processKey,
    @RequestParam(value = "status", required = false) String status,
    @RequestParam(value = "releaseId", required = false) String releaseId,
    @RequestParam(value = "page", required = false) Integer page,
    @RequestParam(value = "size", required = false) Integer size, @PathVariable(value = "areaId") String areaId)
  {

      LOGGER.debug("Operation started");

      final var query = new ListProcessDefinitionsQuery(processKey, status, releaseId, page, size, areaId);

      ResponseEntity<ProcessDefinitionListPageDTO> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{areaId}/process-definitions"
  )
  @Operation(
    summary = "POST method to handle operations for createProcessDefinition",
    description = "POST method to handle operations for createProcessDefinition",
    responses = {
      @ApiResponse(
          responseCode = "201",
          description = "Persisted process definition",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProcessDefinitionDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProcessDefinitionDTO> createProcessDefinition(@Valid @RequestBody ProcessDefinitionRequestDTO createProcessDefinitionRequest
    , @PathVariable(value = "areaId") String areaId)
  {

      LOGGER.debug("Operation started");

      final var command = new CreateProcessDefinitionCommand(createProcessDefinitionRequest, areaId);

       ResponseEntity<ProcessDefinitionDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @DeleteMapping(
    value = "{areaId}/process-definitions/{processDefinitionId}"
  )
  @Operation(
    summary = "DELETE method to handle operations for removeProcessDefinition",
    description = "DELETE method to handle operations for removeProcessDefinition",
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
  
  public ResponseEntity<String> removeProcessDefinition(
    @PathVariable(value = "areaId") String areaId,@PathVariable(value = "processDefinitionId") String processDefinitionId)
  {

      LOGGER.debug("Operation started");

      final var command = new RemoveProcessDefinitionCommand(areaId, processDefinitionId);

       ResponseEntity<String> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "status"
  )
  @Operation(
    summary = "GET method to handle operations for listAreaStatus",
    description = "GET method to handle operations for listAreaStatus",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of area status",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ConfigParameterDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ConfigParameterDTO>> listAreaStatus(
    )
  {

      LOGGER.debug("Operation started");

      final var query = new ListAreaStatusQuery();

      ResponseEntity<List<ConfigParameterDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}
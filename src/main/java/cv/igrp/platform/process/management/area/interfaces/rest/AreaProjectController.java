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


import java.util.List;
import cv.igrp.platform.process.management.area.application.dto.AreaListaPageDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.ProjectDTO;
import cv.igrp.platform.process.management.area.application.dto.ProjectRequestDTO;

@IgrpController
@RestController
@RequestMapping(path = "areas")
@Tag(name = "AreaProject", description = "Area and Project Management")
public class AreaProjectController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AreaProjectController.class);

  
  private final CommandBus commandBus;
  private final QueryBus queryBus;

  
  public AreaProjectController(
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
                  implementation = AreaListaPageDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<AreaListaPageDTO>> listAreas(
    @RequestParam(value = "code", required = false) String code,
    @RequestParam(value = "name", required = false) String name,
    @RequestParam(value = "applicationCode", required = false) String applicationCode)
  {

      LOGGER.debug("Operation started");

      final var query = new ListAreasQuery(code, name, applicationCode);

      ResponseEntity<List<AreaListaPageDTO>> response = queryBus.handle(query);

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
    value = "{areaId}/projects"
  )
  @Operation(
    summary = "GET method to handle operations for listProjects",
    description = "GET method to handle operations for listProjects",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of projects of an area",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProjectDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ProjectDTO>> listProjects(
    @PathVariable(value = "areaId") String areaId)
  {

      LOGGER.debug("Operation started");

      final var query = new ListProjectsQuery(areaId);

      ResponseEntity<List<ProjectDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @PostMapping(
    value = "{areaId}/projects"
  )
  @Operation(
    summary = "POST method to handle operations for createProject",
    description = "POST method to handle operations for createProject",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "Persisted project",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ProjectDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectRequestDTO createProjectRequest
    , @PathVariable(value = "areaId") String areaId)
  {

      LOGGER.debug("Operation started");

      final var command = new CreateProjectCommand(createProjectRequest, areaId);

       ResponseEntity<ProjectDTO> response = commandBus.send(command);

       LOGGER.debug("Operation finished");

        return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

  @GetMapping(
    value = "{id}/children"
  )
  @Operation(
    summary = "GET method to handle operations for listChildren",
    description = "GET method to handle operations for listChildren",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "List of subareas",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = AreaDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<AreaDTO>> listChildren(
    @PathVariable(value = "id") String id)
  {

      LOGGER.debug("Operation started");

      final var query = new ListChildrenQuery(id);

      ResponseEntity<List<AreaDTO>> response = queryBus.handle(query);

      LOGGER.debug("Operation finished");

      return ResponseEntity.status(response.getStatusCode())
              .headers(response.getHeaders())
              .body(response.getBody());
  }

}
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

import cv.igrp.platform.process.management.processruntime.application.dto.ActivityDTO;
import java.util.List;
import cv.igrp.platform.process.management.processruntime.application.dto.ActivityProgressDTO;

@IgrpController
@RestController
@RequestMapping(path = "activities")
@Tag(name = "Activities", description = "Activities Management")
public class ActivitiesController {

  
  private final QueryBus queryBus;

  public ActivitiesController(QueryBus queryBus) {
          this.queryBus = queryBus;
          
  }
      @GetMapping(
   value = "{id}"
  )
  @Operation(
    summary = "GET method to handle operations for Get activity by id",
    description = "GET method to handle operations for Get activity by id",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ActivityDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<ActivityDTO> getActivityById(
    @PathVariable(value = "id") String id)
  {

      final var query = new GetActivityByIdQuery(id);

      ResponseEntity<ActivityDTO> response = queryBus.handle(query);

      return response;
  }

      @GetMapping(
   value = "instances"
  )
  @Operation(
    summary = "GET method to handle operations for Get activity instances",
    description = "GET method to handle operations for Get activity instances",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ActivityDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ActivityDTO>> getActivityInstances(
    @RequestParam(value = "processIdentifier") String processIdentifier,
    @RequestParam(value = "type", required = false) String type)
  {

      final var query = new GetActivityInstancesQuery(processIdentifier, type);

      ResponseEntity<List<ActivityDTO>> response = queryBus.handle(query);

      return response;
  }

      @GetMapping(
   value = "progress"
  )
  @Operation(
    summary = "GET method to handle operations for Get activity progress",
    description = "GET method to handle operations for Get activity progress",
    responses = {
      @ApiResponse(
          responseCode = "200",
          description = "",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  implementation = ActivityProgressDTO.class,
                  type = "object")
          )
      )
    }
  )
  
  public ResponseEntity<List<ActivityProgressDTO>> getActivityProgress(
    @RequestParam(value = "processIdentifier") String processIdentifier,
    @RequestParam(value = "type", required = false) String type)
  {

      final var query = new GetActivityProgressQuery(processIdentifier, type);

      ResponseEntity<List<ActivityProgressDTO>> response = queryBus.handle(query);

      return response;
  }

}
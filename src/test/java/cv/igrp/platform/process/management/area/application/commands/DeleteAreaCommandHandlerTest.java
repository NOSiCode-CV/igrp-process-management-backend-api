package cv.igrp.platform.process.management.area.application.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import cv.igrp.platform.process.management.area.domain.service.AreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class DeleteAreaCommandHandlerTest {

  @Mock
  private AreaService areaService;

  @InjectMocks
  private DeleteAreaCommandHandler handler;

  @BeforeEach
  void setUp() {

  }

  @Test
  void testHandle_callsDeleteAndReturnsNoContent() {
    String id = UUID.randomUUID().toString();
    DeleteAreaCommand command = new DeleteAreaCommand(id);

    ResponseEntity<String> response = handler.handle(command);

    verify(areaService, times(1)).deleteArea(UUID.fromString(id));
    assertEquals(204, response.getStatusCode().value());
    assertNull(response.getBody());
  }

}

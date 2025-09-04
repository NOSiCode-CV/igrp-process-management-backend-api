package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.SequenceRequestDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessSequenceService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProcessSequenceCommandHandlerTest {

  @Mock
  private ProcessSequenceService processSequenceService;

  @Mock
  private ProcessSequenceMapper processSequenceMapper;

  @Mock
  private UserContext userContext;

    @InjectMocks
    private CreateProcessSequenceCommandHandler createProcessSequenceCommandHandler;

    @BeforeEach
    void setUp() {}


    @Test
    void testHandle() {
      // Given
      var userName = "demo@nosi.cv";
      var user = Code.create(userName);
      when(userContext.getCurrentUser()).thenReturn(user);

      var requestDto = new SequenceRequestDTO();
      requestDto.setName("MySequence");
      requestDto.setPrefix("PRX");
      requestDto.setDateFormat("yyyyMMdd");
      requestDto.setPadding((short)5);
      requestDto.setCheckDigitSize((short)2);
      requestDto.setNumberIncrement((short)1);

      var command = new CreateProcessSequenceCommand();
      command.setKey("process-123");
      command.setSequencerequestdto(requestDto);

      var savedSequence = mock(ProcessSequence.class);
      when(processSequenceService.save(any(ProcessSequence.class))).thenReturn(savedSequence);

      var dto = new ProcessSequenceDTO();
      when(processSequenceMapper.toDTO(savedSequence)).thenReturn(dto);

      // When
      ResponseEntity<ProcessSequenceDTO> response = createProcessSequenceCommandHandler.handle(command);

      // Then
      assertNotNull(response);
      assertEquals(dto, response.getBody());

      verify(userContext).getCurrentUser();
      verify(processSequenceService).save(any(ProcessSequence.class));
      verify(processSequenceMapper).toDTO(savedSequence);
    }

}

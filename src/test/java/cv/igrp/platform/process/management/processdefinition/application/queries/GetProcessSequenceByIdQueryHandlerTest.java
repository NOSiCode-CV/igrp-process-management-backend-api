package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessSequence;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessSequenceService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessSequenceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetProcessSequenceByIdQueryHandlerTest {

  @Mock
  private ProcessSequenceService processSequenceService;

  @Mock
  private ProcessSequenceMapper processSequenceMapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private GetProcessSequenceByIdQueryHandler handler;

  private GetProcessSequenceByIdQuery query;
  private ProcessSequence processSequenceMock;
  private ProcessSequenceDTO processSequenceDTOMock;
  private String sequenceId;

  @BeforeEach
  void setUp() {

    sequenceId = UUID.randomUUID().toString();

    query = new GetProcessSequenceByIdQuery(sequenceId);

    processSequenceMock = mock(ProcessSequence.class);
    processSequenceDTOMock = mock(ProcessSequenceDTO.class);

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));

    when(processSequenceService.getSequenceById(Identifier.create(sequenceId)))
        .thenReturn(processSequenceMock);

    when(processSequenceMapper.toDTO(processSequenceMock))
        .thenReturn(processSequenceDTOMock);
  }

  @Test
  void testHandleGetProcessSequenceByIdQuery() {

    ResponseEntity<ProcessSequenceDTO> response = handler.handle(query);

    assertNotNull(response);
    assertEquals(processSequenceDTOMock, response.getBody());

    verify(userContext).getCurrentUser();
    verify(processSequenceService).getSequenceById(Identifier.create(sequenceId));
    verify(processSequenceMapper).toDTO(processSequenceMock);

  }

}

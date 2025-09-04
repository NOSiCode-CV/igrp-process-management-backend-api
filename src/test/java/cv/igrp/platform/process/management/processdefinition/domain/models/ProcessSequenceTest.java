package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProcessSequenceTest {

  private ProcessSequence.ProcessSequenceBuilder validBuilder() {
    return ProcessSequence.builder()
        .id(Identifier.generate())
        .name(Name.create("MySequence"))
        .prefix(Code.create("PRX"))
        .checkDigitSize((short) 2)
        .padding((short) 5)
        .dateFormat("yyyyMMdd")
        .nextNumber(1L)
        .numberIncrement((short) 1)
        .processDefinitionKey(Code.create("process-123"));
  }

  @Test
  void validate_shouldThrow_whenCheckDigitSize_gt10() {
    var seq = validBuilder().checkDigitSize((short) 11).build();
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }

  @Test
  void validate_shouldThrow_whenCheckDigitSize_negative() {
    var seq = validBuilder().checkDigitSize((short) -1).build();
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }

  @Test
  void validate_shouldThrow_whenPadding_gt20() {
    var seq = validBuilder().padding((short) 21).build();
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }

  @Test
  void validate_shouldThrow_whenPadding_negative() {
    var seq = validBuilder().padding((short) -1).build();
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }

  @Test
  void validate_shouldThrow_whenNumberIncrement_zeroOrNegative() {
    var zero = validBuilder().numberIncrement((short) 0).build();
    assertThrows(IgrpResponseStatusException.class, zero::validate);

    var negative = validBuilder().numberIncrement((short) -5).build();
    assertThrows(IgrpResponseStatusException.class, negative::validate);
  }

  @Test
  void validate_shouldThrow_whenDateFormat_invalidPattern() {
    // padrão inválido para DateTimeFormatter.ofPattern(...)
    var seq = validBuilder().dateFormat("INVALID_FORMAT").build();
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }

  @Test
  void validate_shouldThrowIllegalArg_whenNextNumber_lt1() {
    var seq = validBuilder().nextNumber(0L).build();
    assertThrows(IllegalArgumentException.class, seq::validate);
  }

  // opcional: se Code.create não validar caracteres, este também exercita o regex do prefix
  @Test
  void validate_shouldThrow_whenPrefix_notAlphanumeric() {
    var seq = validBuilder().prefix(Code.create("???")).build(); // só funciona se Code.create aceitar
    assertThrows(IgrpResponseStatusException.class, seq::validate);
  }
}

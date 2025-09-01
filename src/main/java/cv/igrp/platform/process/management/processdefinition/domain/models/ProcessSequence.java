package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.processruntime.domain.models.ProcessNumber;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
public class ProcessSequence {
  private final Identifier id;
  private final Name name;
  private final Code prefix;
  private final Short checkDigitSize;
  private final Short padding;
  private final String dateFormat;
  private Long nextNumber;
  private final Short numberIncrement;
  private final Code processDefinitionId;

  @Builder
  public ProcessSequence(Identifier id,
                         Name name,
                         Code prefix,
                         Short checkDigitSize,
                         Short padding,
                         String dateFormat,
                         Long nextNumber,
                         Short numberIncrement,
                         Code processDefinitionId)
  {
    this.id = id;
    this.name = Objects.requireNonNull(name, "The Name of the sequence cannot be null!");
    this.prefix = Objects.requireNonNull(prefix, "The Prefix of the sequence cannot be null!");
    this.checkDigitSize = Objects.requireNonNull(checkDigitSize, "CheckDigitSize of the Sequence cannot be null!");
    this.padding = padding;
    this.dateFormat = Objects.requireNonNull(dateFormat, "The DateFormat of the Sequence cannot be null!");
    this.nextNumber = nextNumber;
    this.numberIncrement = numberIncrement;
    this.processDefinitionId = Objects.requireNonNull(processDefinitionId, "The Process Definition Id of the Sequence cannot be null!");
  }

  public ProcessSequence newInstance() {
    return with(null,1L);
  }

  public ProcessSequence with(Identifier id, Long nextNumber) {
    return ProcessSequence.builder()
        .id(id==null ? Identifier.generate() : id)
        .nextNumber(nextNumber)
        .name(this.name)
        .prefix(this.prefix)
        .checkDigitSize(this.checkDigitSize)
        .padding(this.padding)
        .dateFormat(this.dateFormat)
        .numberIncrement(this.numberIncrement)
        .processDefinitionId(this.processDefinitionId).build();
  }


  public ProcessNumber generateNextProcessNumberAndIncrement() {
    String processNumber = generateNumber(this.nextNumber);
    this.nextNumber += this.numberIncrement;
    return ProcessNumber.create(processNumber);
  }


  private String generateNumber(Long currentNumber) {
    StringBuilder sb = new StringBuilder();

    // prefixo
    sb.append(prefix.getValue());

    // data formatada
    if (dateFormat != null && !dateFormat.isEmpty()) {
      String formattedDate = DateUtil.biLocalDateToString.apply(LocalDate.now(),DateTimeFormatter.ofPattern(dateFormat));
      sb.append(formattedDate);
    }

    // número sequencial com padding
    if (padding != null && padding > 0) {
      sb.append(String.format("%0" + padding + "d", currentNumber));
    } else {
      sb.append(currentNumber);
    }

    // dígito de controlo (exemplo simples: mod 10)
    if (checkDigitSize != null && checkDigitSize > 0) {
      long checkDigit = currentNumber % (long) Math.pow(10, checkDigitSize);
      sb.append(checkDigit);
    }

    return sb.toString();
  }

}

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
    return buildWith(Identifier.generate());
  }


  public ProcessSequence copyWithId(Identifier id) {
    return buildWith(id);
  }


  private ProcessSequence buildWith(Identifier id) {
    return ProcessSequence.builder()
        .id(id)
        .name(this.name)
        .prefix(this.prefix)
        .checkDigitSize(this.checkDigitSize)
        .padding(this.padding)
        .dateFormat(this.dateFormat)
        .nextNumber(this.nextNumber == null ? 1L : this.nextNumber)
        .numberIncrement(this.numberIncrement)
        .processDefinitionId(this.processDefinitionId)
        .build();
  }


  public ProcessNumber generateNextProcessNumberAndIncrement() {
    String processNumber = generateNumber(this.nextNumber);
    this.nextNumber += this.numberIncrement;
    return ProcessNumber.create(processNumber);
  }


  private String generateNumber(Long currentNumber) {
    StringBuilder sb = new StringBuilder();

    sb.append(prefix.getValue());

    if (dateFormat != null && !dateFormat.isEmpty()) {
      sb.append(DateUtil.biLocalDateToString.apply(LocalDate.now(),DateTimeFormatter.ofPattern(dateFormat)));
    }

    if (padding != null && padding > 0) {
      sb.append(String.format("%0" + padding + "d", currentNumber));
    } else {
      sb.append(currentNumber);
    }

    if (checkDigitSize != null && checkDigitSize > 0) {
      long checkDigit = currentNumber % (long) Math.pow(10, checkDigitSize);
      sb.append(checkDigit);
    }

    return sb.toString();
  }

}

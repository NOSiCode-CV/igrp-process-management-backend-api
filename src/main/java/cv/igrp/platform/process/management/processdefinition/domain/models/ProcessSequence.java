package cv.igrp.platform.process.management.processdefinition.domain.models;

import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class ProcessSequence {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessSequence.class);

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
    this.name = name;
    this.prefix = prefix;
    this.checkDigitSize = checkDigitSize;
    this.padding = padding;
    this.dateFormat = dateFormat;
    this.nextNumber = nextNumber;
    this.numberIncrement = numberIncrement;
    this.processDefinitionId = processDefinitionId;
  }


  public void validate() {
    if (name == null || name.getValue().trim().isEmpty()) {
      throw IgrpResponseStatusException.badRequest("Sequence name must not be blank.");
    }

    if (prefix == null || !prefix.getValue().matches("[A-Za-z0-9/_-]+")) {
      throw IgrpResponseStatusException.badRequest("Prefix must be alphanumeric and not null.");
    }

    if (checkDigitSize != null && (checkDigitSize < 0 || checkDigitSize > 10)) {
      throw IgrpResponseStatusException.badRequest("Check digit size must be between 0 and 10.");
    }

    if (padding != null && (padding < 0 || padding > 20)) {
      throw IgrpResponseStatusException.badRequest("Padding must be between 0 and 20.");
    }

    if (dateFormat != null && !dateFormat.isEmpty()) {
      try {
        DateTimeFormatter.ofPattern(dateFormat);
      } catch (IllegalArgumentException e) {
        throw IgrpResponseStatusException.badRequest("Invalid date format: " + dateFormat);
      }
    }

    if (nextNumber != null && nextNumber < 1) {
      throw new IllegalArgumentException("Next number must be >= 1.");
    }

    if (numberIncrement == null || numberIncrement <= 0) {
      throw IgrpResponseStatusException.badRequest("Number increment must be > 0.");
    }

    if (processDefinitionId == null) {
      throw IgrpResponseStatusException.badRequest("Process Definition Id must not be null.");
    }
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
    String processNumber = generateNumber(this.nextNumber == null ? 1L : this.nextNumber);
    this.nextNumber = (this.nextNumber == null ? 1L : this.nextNumber) + this.numberIncrement;
    return ProcessNumber.create(processNumber);
  }


  private String generateNumber(Long currentNumber) {

    LOGGER.info("Generation of process sequence has been started...");

    StringBuilder sb = new StringBuilder();

    LOGGER.info("PREFIX is [{}]",prefix.getValue());
    sb.append(prefix.getValue());

    if (dateFormat != null && !dateFormat.isEmpty()) {
      var now = LocalDate.now();
      var dateStr = DateUtil.biLocalDateToString.apply(now, DateTimeFormatter.ofPattern(dateFormat));
      LOGGER.info("CURRENT_DATE [{}] with FORMAT [{}] is [{}]", now, dateFormat, dateStr);
      sb.append(dateStr);
    }

    if (padding != null && padding > 0) {
      var number = String.format("%0" + padding + "d", currentNumber); //do not edit
      LOGGER.info("CURRENT_NUMBER [{}] with PADDING [{}] is [{}]",currentNumber, padding, number);
      sb.append(number);
    } else {
      LOGGER.info("CURRENT_NUMBER [{}] with NO PADDING [{}] is NUMBER [{}]",currentNumber, padding, currentNumber);
      sb.append(currentNumber);
    }

    if (checkDigitSize != null && checkDigitSize > 0) {
      long checkDigit = currentNumber % (long) Math.pow(10, checkDigitSize);
      LOGGER.info("CURRENT_NUMBER [{}] with CHECK_DIGIT_SIZE [{}] is [{}]", currentNumber, checkDigitSize, checkDigit);
      sb.append(checkDigit);
    } else {
      LOGGER.info("CHECK_DIGIT_SIZE is NULL.");
    }

    var sequence = sb.toString();

    LOGGER.info("Generation of process sequence has been successfully finished. RESULT [{}]", sequence);

    return sequence;
  }

}

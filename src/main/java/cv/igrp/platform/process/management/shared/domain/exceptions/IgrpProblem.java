package cv.igrp.platform.process.management.shared.domain.exceptions;

import org.springframework.http.HttpStatus;

public record IgrpProblem<T>(
      HttpStatus status,
      String title,
      T details) {
}

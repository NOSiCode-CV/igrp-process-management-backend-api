package cv.igrp.platform.process.management.processdefinition.domain.exception;

/**
 * Exception thrown when process deployment operations fail.
 */
public class ProcessDeploymentException extends RuntimeException {

  public ProcessDeploymentException(String message) {
    super(message);
  }

  public ProcessDeploymentException(String message, Throwable cause) {
    super(message, cause);
  }

}

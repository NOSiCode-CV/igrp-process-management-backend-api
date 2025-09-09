package cv.igrp.platform.process.management.processruntime.domain.exception;

/**
 * Exception thrown to indicate any error occurring during runtime process engine operations.
 * <p>
 * This exception serves as a generic wrapper for all process and task-related errors,
 * such as missing process definitions, invalid process instance IDs, failed task completion,
 * or assignment errors.
 * </p>
 *
 * <p>
 * Since this is a runtime exception, callers may choose to handle it explicitly or
 * allow it to propagate. Implementations should provide meaningful messages to help
 * diagnose the root cause.
 * </p>
 */
public class RuntimeProcessEngineException extends RuntimeException {

  /**
   * Constructs a new {@code RuntimeProcessEngineException} with the specified detail message.
   *
   * @param message the detail message explaining the reason for the error
   */
  public RuntimeProcessEngineException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code RuntimeProcessEngineException} with the specified detail message
   * and cause.
   *
   * @param message the detail message explaining the reason for the error
   * @param cause   the underlying cause of this exception
   */
  public RuntimeProcessEngineException(String message, Throwable cause) {
    super(message, cause);
  }
}

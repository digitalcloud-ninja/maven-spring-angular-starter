package ninja.digitalcloud.application.backend.exceptions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
  private HttpStatus statusCode;
  private LocalDateTime timestamp;
  private String message;
  private List<String> errors;

  /**
   * 
   */
  public ErrorMessage() {
    super();
  }

  /**
   * @param statusCode
   * @param message
   * @param errors
   */
  public ErrorMessage(final HttpStatus statusCode, final String message, final List<String> errors) {
    super();
    this.timestamp = LocalDateTime.now();
    this.statusCode = statusCode;
    this.message = message;
    this.errors = errors;
  }

  /**
   * @param statusCode
   * @param message
   * @param errors
   */
  public ErrorMessage(final HttpStatus statusCode, final String message, final String errors) {
    super();
    this.timestamp = LocalDateTime.now();
    this.statusCode = statusCode;
    this.message = message;
    this.errors = Arrays.asList(errors);
  }

  /**
   * @return the statusCode
   */
  public HttpStatus getStatusCode() {
    return statusCode;
  }

  /**
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * @return the timestamp
   */
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the errors
   */
  public List<String> getErrors() {
    return errors;
  }

  /**
   * @param errors the errors to set
   */
  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */

  @Override
  public String toString() {
    return "ErrorMessage [statusCode=" + statusCode + "errors=" + errors + ", message=" + message + ", timestamp="
        + timestamp + "]";
  }
}

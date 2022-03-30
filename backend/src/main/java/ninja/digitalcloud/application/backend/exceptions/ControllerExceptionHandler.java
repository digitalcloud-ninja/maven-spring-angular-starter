package ninja.digitalcloud.application.backend.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    static String logFormat = "%s: {\"request\": \"%s\", \"errors\": [\"%s\"]}";

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String errors = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String errors = ex.getParameterName() + " parameter is missing";
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String errors = ex.getRequestPartName() + " part is missing";
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String errors = ex.getVariableName() + " path variable is missing";
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    // Customer 400 Exception Handlers

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        final String errors = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final javax.validation.ConstraintViolationException ex, final WebRequest request) {
        final List<String> errors = new ArrayList<>();
        // add violations to errors.
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final String errors = "No request handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    // Custom 404 Resource Not Found Exception
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleAll(final ResourceNotFoundException ex, final WebRequest request) {
        final String errors = "No resource found for " + request.getContextPath();
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    //405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // Build string of supported methods
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        log.info(String.format(logFormat, request, builder));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // Build string of supported media types
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));
        log.info(String.format(logFormat, request, builder));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    // 500
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        log.error(String.format(logFormat, request, ex));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Exception Occurred.");
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatusCode());
    }

    private ResponseEntity<Object> getObjectResponseEntity(BindException ex, HttpHeaders headers, WebRequest request) {
        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        log.info(String.format(logFormat, request, errors));
        final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, errorMessage, headers, errorMessage.getStatusCode(), request);
    }
}
  
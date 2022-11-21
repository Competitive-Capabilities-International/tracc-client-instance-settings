package com.traccsolution.tracc_client_instance_settings.exception;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.traccsolution.tracc_client_instance_settings.dto.ApiErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Stream;

/**
 * <p>Global Rest API exception handler to include trace and span id references in error messages for various exception types<p/>
 * All error messages must have a trace and span id returned as reference for traceability.
 * Internal server errors will be logged to server logs but details of the exception will not be sent to api consumer.
 * Only a reference will be sent back containing a trace id and span id
 *
 * @author KeeshanReddy
 * @see <a href="https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">Custom Error Message Handling for REST API</a>
 */
@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String SUPPORT_MESSAGE = "Sorry, there has been an error - Please contact support";
    public static final String FIELD_SEPARATOR = ": ";
    private Tracer tracer;


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createBindExceptionResponse(ex, headers, request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createBindExceptionResponse(ex, headers, request, ex.getBindingResult());
    }

    private ResponseEntity<Object> createBindExceptionResponse(BindException ex, HttpHeaders headers, WebRequest request, BindingResult bindingResult) {
        Stream<String> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + FIELD_SEPARATOR + error.getDefaultMessage());
        Stream<String> globalErrors = bindingResult.getGlobalErrors()
                .stream()
                .map(error -> error.getObjectName() + FIELD_SEPARATOR + error.getDefaultMessage());
        List<String> errors = Stream.concat(fieldErrors, globalErrors).toList();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return createErrorResponse(ex, headers, request, errors, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        return createErrorResponse(ex, headers, request, Arrays.asList(error), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = populateRootCause(ex);
        if (rootCause instanceof JsonMappingException jsonMappingException) {
            String errorMessage = populateJsonMappingExceptionErrorMsg(jsonMappingException);
            return createErrorResponse(ex, headers, request, Arrays.asList(errorMessage), HttpStatus.BAD_REQUEST);
        }
        return handleUnexpectedErrors(new Exception(rootCause), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex, status);
        ApiErrorDTO apiErrorDTO = populateAPIErrorDTO(body, status);
        return super.handleExceptionInternal(ex, apiErrorDTO, headers, apiErrorDTO.getStatus(), request);
    }

    private void logException(Exception ex, HttpStatus status) {
        if (status.is5xxServerError()) {
            log.error(ex.getMessage(),ex);
        } else {
            log.debug(ex.getMessage(),ex);
        }
    }

    private ApiErrorDTO populateAPIErrorDTO(Object body, HttpStatus status) {
        if (body != null && body instanceof ApiErrorDTO apiError) {
            return apiError;
        }
        return createDefaultAPIErrorDTO(status);
    }

    private ApiErrorDTO createDefaultAPIErrorDTO(HttpStatus status) {
        String message = populateErrorMessage(tracer);
        return ApiErrorDTO.builder()
                .status(status)
                .message(message)
                // error must not contain sensitive info. server logs can be used with trace and span id to find root cause
                .error("Internal server error occurred")
                .build();
    }

    @ExceptionHandler({RestResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFound(RestResourceNotFoundException ex, WebRequest request) {
        String error = "Rest Resources not found.";
        String resourceName = ex.getResourceName()!=null ? ex.getResourceName() : "(unknown)";
        error+=" resource name = " + resourceName + ". ";
        // add ids if supplied
        Map<String, String> filters = ex.getFilters();
        if (filters != null){
            error +="Search criteria = " + filters;
        }else{
            error +="Search criteria = (no filter supplied)";
        }
        return createErrorResponse(ex, new HttpHeaders(), request, Arrays.asList(error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleUnexpectedErrors(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    private Throwable populateRootCause(HttpMessageNotReadableException ex) {
        return ex.getRootCause() != null ? ex.getRootCause() : ex;
    }

    private String populateJsonMappingExceptionErrorMsg(JsonMappingException ex) {
        String fieldName = "unknown field name (could not locate last reference)";
        if (!ex.getPath().isEmpty()) {
            int lastElementIndex = ex.getPath().size() - 1;
            JsonMappingException.Reference lastReference = ex.getPath().get(lastElementIndex);
            fieldName = lastReference.getFieldName();
        }
        return fieldName + FIELD_SEPARATOR + ex.getMessage();
    }

    private ResponseEntity<Object> createErrorResponse(Exception ex, HttpHeaders headers, WebRequest request, List<String> errors, HttpStatus httpStatus) {
        String errorMessage = populateErrorMessage(tracer);
        ApiErrorDTO apiError = ApiErrorDTO.builder()
                .status(httpStatus)
                .message(errorMessage)
                .errors(errors)
                .build();
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getRequestPartName() + " part is missing";
        return createErrorResponse(ex, headers, request, Arrays.asList(error), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return createErrorResponse(ex, headers, request, Arrays.asList(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String requireTypeMessage = populateRequiredType(ex);
        String error = ex.getName() + FIELD_SEPARATOR + ex.getValue() + " is not of type " + requireTypeMessage;
        return createErrorResponse(ex, new HttpHeaders(), request, Arrays.asList(error), HttpStatus.BAD_REQUEST);
    }

    private String populateRequiredType(MethodArgumentTypeMismatchException ex) {
        String requireTypeMessage = "unknown (requiredType was null)";
        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null) {
            requireTypeMessage = requiredType.getSimpleName();
        }
        return requireTypeMessage;
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + FIELD_SEPARATOR + violation.getMessage());
        }
        return createErrorResponse(ex, new HttpHeaders(), request, errors, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        Set<HttpMethod> supportedHttpMethods = ex.getSupportedHttpMethods();
        if (supportedHttpMethods != null) {
            supportedHttpMethods.forEach(t -> builder.append(t + " "));
        }
        return createErrorResponse(ex, new HttpHeaders(), request, Arrays.asList(builder.toString()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        String error = builder.substring(0, builder.length() - 2);
        return createErrorResponse(ex, headers, request, Arrays.asList(error), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    private String populateErrorMessage(Tracer tracer) {
        String reference = SUPPORT_MESSAGE;
        Span span = tracer.currentSpan();
        if (span != null) {
            TraceContext traceContext = span.context();
            reference += " with reference [trace-id = " + traceContext.traceId() + " span-id = " + traceContext.spanId() + "]";
        }
        return reference;
    }
}

package com.food_delivery.exception;

import static com.food_delivery.constant.CommonParams.EMPTY_STRING;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String INVALID_ARGUMENT = "Invalid argument";
    private static final String MAX_FILE_SIZE_EXCEED = "Max file size upload exceed";
    private static final String INVALID_NUMBER_TYPE = "Invalid number type";
    private static final String INVALID_JSON_FORMAT = "Invalid request payload format";

    @Autowired
    ErrorResponseHandler errorResponseHandler;

    @ExceptionHandler(CommonException.class)
    public void handleCommonException(CommonException cx, HttpServletResponse response) {
        errorResponseHandler.handle(cx, response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<ObjectError> errors = ex.getAllErrors();
        List<ErrorPath> eps = resolveErrors(errors);
        ErrorResponse errorMessage = ErrorResponse.of(INVALID_ARGUMENT, ErrorCode.INVALID_ARGUMENTS, eps, HttpStatus.BAD_REQUEST);

        return handleExceptionResponse(errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionResponse(ErrorResponse.of(INVALID_JSON_FORMAT, ErrorCode.BAD_REQUEST_PARAMS, HttpStatus.BAD_REQUEST));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> errors = ex.getAllErrors();
        List<ErrorPath> eps = resolveErrors(errors);
        return handleExceptionResponse(ErrorResponse.of(INVALID_ARGUMENT, ErrorCode.INVALID_ARGUMENTS, eps, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return handleExceptionResponse(ErrorResponse.of(MAX_FILE_SIZE_EXCEED, ErrorCode.BAD_REQUEST_PARAMS, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        return handleExceptionResponse(ErrorResponse.of(INVALID_NUMBER_TYPE, ErrorCode.INVALID_ARGUMENTS, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<Object> handleExecutionException(ExecutionException ex) {
        return handleExceptionResponse(ErrorResponse.of(ex.getMessage(), ErrorCode.GENERAL, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private List<ErrorPath> resolveErrors(List<ObjectError> errors) {
        List<ErrorPath> eps = new ArrayList<>();
        for (ObjectError e: errors) {
            if (e instanceof FieldError) {
                eps.add(ErrorPath.of(((FieldError) e).getField(), e.getDefaultMessage()));
            } else {
                eps.add(ErrorPath.of(EMPTY_STRING, e.getDefaultMessage()));
            }
        }

        return eps;
    }

    private ResponseEntity<Object> handleExceptionResponse(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

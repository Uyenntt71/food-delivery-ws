package com.food_delivery.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ErrorResponseHandler {
    protected final ObjectMapper mapper = new ObjectMapper();

    public void handle(Exception ex, HttpServletResponse response) {
        if (!response.isCommitted()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            if (ex instanceof CommonException) {
                try {
                    handleCommonException((CommonException) ex, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleCommonException(CommonException ex, HttpServletResponse response) throws IOException {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus httpStatus;

        switch (errorCode) {
            case AUTHENTICATION:
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;
            case PERMISSION_DENIED:
                httpStatus = HttpStatus.FORBIDDEN;
                break;
            case INVALID_ARGUMENTS:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case ITEM_NOT_FOUND:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case BAD_REQUEST_PARAMS:
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            case EXISTS:
                httpStatus = HttpStatus.CONFLICT;
                break;
            case GENERAL:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        response.setStatus(httpStatus.value());
        mapper.writeValue(response.getWriter(), ErrorResponse.of(ex.getMessage(), ex.getCode(), errorCode, httpStatus));
    }
}

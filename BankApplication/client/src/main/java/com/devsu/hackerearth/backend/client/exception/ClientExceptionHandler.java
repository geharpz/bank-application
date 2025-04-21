package com.devsu.hackerearth.backend.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsu.hackerearth.backend.client.dto.ApiErrorResponse;
import com.devsu.hackerearth.backend.client.log.LoggingUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j

public class ClientExceptionHandler extends ErrorResponse {
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleClientNotFound(ClientNotFoundException ex,
            HttpServletRequest request) {
        LoggingUtil.logWarn(log, request, "Client not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(ClientInactiveException.class)
    public ResponseEntity<ApiErrorResponse> handleClientInactive(ClientInactiveException ex,
            HttpServletRequest request) {
        LoggingUtil.logWarn(log, request, "Inactive client access: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DataConflictException ex, HttpServletRequest request) {
        LoggingUtil.logWarn(log, request, "Data conflict: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleDomain(BusinessException ex, HttpServletRequest request) {
        LoggingUtil.logWarn(log, request, "Business rule violation: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        LoggingUtil.logError(log, request, ex, "Unexpected internal error");
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }
}

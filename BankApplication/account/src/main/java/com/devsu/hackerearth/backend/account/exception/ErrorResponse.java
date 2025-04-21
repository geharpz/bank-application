package com.devsu.hackerearth.backend.account.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.account.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

public class ErrorResponse {

        protected ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, Exception ex,
                        HttpServletRequest request) {
                String code = (ex instanceof BusinessException) ? code = ((BusinessException) ex).getCode()
                                : "GENERIC_ERROR";
                String message = (ex instanceof BusinessException)
                                ? ex.getMessage()
                                : "An unexpected error occurred. Please contact support.";
                ApiErrorResponse error = new ApiErrorResponse(
                                code,
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                request.getRequestURI());
                return new ResponseEntity<>(error, status);
        }
}
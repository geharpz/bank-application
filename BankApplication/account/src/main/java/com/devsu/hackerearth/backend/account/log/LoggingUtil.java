package com.devsu.hackerearth.backend.account.log;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggingUtil {

    public void logWarn(Logger log, HttpServletRequest request, String message, Object... args) {
        log.warn("[WARN] [{}] [{}] {} | User: {} | Args: {}",
                LocalDateTime.now(),
                request.getRequestURI(),
                message,
                getUsername(),
                args);
    }

    public void logError(Logger log, HttpServletRequest request, Exception ex, String message) {
        log.error("[ERROR] [{}] [{}] {} | User: {}",
                LocalDateTime.now(),
                request.getRequestURI(),
                message,
                getUsername(),
                ex);
    }

    private String getUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
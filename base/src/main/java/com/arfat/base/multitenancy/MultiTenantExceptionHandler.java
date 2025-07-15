package com.arfat.base.multitenancy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MultiTenantExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MultiTenantExceptionHandler.class);

    /**
     * Handles TenantNotFoundException and returns a custom error message.
     *
     * @param ex the TenantNotFoundException
     * @return a custom error message
     */
    @ExceptionHandler(TenantNotFoundException.class)
    public ResponseEntity<?> handleTenantNotFoundException(TenantNotFoundException ex) {
        log.debug("Handling TenantNotFoundException: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }
}

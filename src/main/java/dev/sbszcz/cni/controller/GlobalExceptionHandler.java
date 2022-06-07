package dev.sbszcz.cni.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    record ErrorVO(String reason){}

    @ExceptionHandler(value = {Exception.class})
    ResponseEntity<ErrorVO> handleGenericException(Exception e) {

        log.error("error during request processing occurred", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorVO(e.getMessage() == null ? "generic error" : e.getMessage()));
    }

    @ExceptionHandler(value = {LanguageNotAvailableException.class})
    ResponseEntity<ErrorVO> handleLanguageNotAvailable(LanguageNotAvailableException e) {

        log.error("Language not available", e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorVO(e.getMessage()));
    }

}

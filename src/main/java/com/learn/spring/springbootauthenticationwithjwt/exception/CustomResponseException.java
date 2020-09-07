package com.learn.spring.springbootauthenticationwithjwt.exception;

import com.learn.spring.springbootauthenticationwithjwt.dto.response.ErrorResponse;
import com.learn.spring.springbootauthenticationwithjwt.dto.response.FieldErrorValidation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class CustomResponseException extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("errorMessage", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("error: handleMethodArgumentNotValid");
        ErrorResponse errorResponse = new ErrorResponse();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.getErrors().add(new FieldErrorValidation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

}

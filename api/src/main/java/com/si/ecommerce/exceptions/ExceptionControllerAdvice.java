package com.si.ecommerce.exceptions;

import com.si.ecommerce.models.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(exception.getMessage());
        errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorInfo.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ NotFoundException.class, UsernameNotFoundException.class })
    public ResponseEntity<ErrorInfo> notFoundExceptionHandler(Exception exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(exception.getMessage());
        errorInfo.setTimestamp(LocalDateTime.now());
        errorInfo.setErrorCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> exceptionHandler(MethodArgumentNotValidException exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        String errorMsg = exception.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(", "));
        errorInfo.setErrorMessage(errorMsg);
        errorInfo.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizeRequestException.class)
    public ResponseEntity<ErrorInfo> unauthorizeRequestException(Exception exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(exception.getMessage());
        errorInfo.setTimestamp(LocalDateTime.now());
        errorInfo.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorInfo> badRequestException(BadRequestException exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorMessage(exception.getMessage());
        errorInfo.setTimestamp(LocalDateTime.now());
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}


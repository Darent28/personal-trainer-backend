package com.pt.personal_trainer.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pt.personal_trainer.domain.dto.ExceptionViolationDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@RestControllerAdvice
public class StatusExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        
        ExceptionViolationDto exceptionViolationDto = ex.getFieldErrors().stream()
            .map(fieldError -> new ExceptionViolationDto(fieldError.getDefaultMessage()))
            .findFirst()
            .orElse(new ExceptionViolationDto("Validation error"));

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exceptionViolationDto.getExceptionMessage());
    }

    @ExceptionHandler(CustomExceptions.class)
    public ProblemDetail handleCustomResponseException(CustomExceptions ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String exceptionMessage = ex.getMessage();

        if(ex.getClass() == CustomExceptions.NotFoundException.class) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if(ex.getClass() == CustomExceptions.ServerErrorException.class) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ProblemDetail.forStatusAndDetail(httpStatus, exceptionMessage);
    }
}

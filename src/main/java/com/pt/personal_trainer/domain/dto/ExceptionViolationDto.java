package com.pt.personal_trainer.domain.dto;

public class ExceptionViolationDto {

    private String exceptionMessage;

    public ExceptionViolationDto() {
    }

    public ExceptionViolationDto(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}

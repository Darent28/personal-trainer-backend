package com.pt.personal_trainer.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionViolationDto {
    private String exceptionMessage;
}

package com.si.ecommerce.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorInfo {
    private String errorMessage;

    private Integer errorCode;

    private LocalDateTime timestamp;
}
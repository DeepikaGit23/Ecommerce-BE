package com.si.ecommerce.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}

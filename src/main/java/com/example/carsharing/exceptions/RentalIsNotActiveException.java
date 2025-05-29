package com.example.carsharing.exceptions;

public class RentalIsNotActiveException extends RuntimeException {
    public RentalIsNotActiveException(String message) {
        super(message);
    }
}

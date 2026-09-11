package com.training.enrollmentservice.exception;

public class EnrollmentAlreadyExistsException extends RuntimeException {

    public EnrollmentAlreadyExistsException(String message) {
        super(message);
    }
}
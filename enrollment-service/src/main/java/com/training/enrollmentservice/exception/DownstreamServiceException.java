package com.training.enrollmentservice.exception;
import lombok.Getter;

@Getter
public class DownstreamServiceException extends RuntimeException {

    private final String serviceName;
    private final int status;
    private final String resourceId;

    public DownstreamServiceException(
            String serviceName,
            int status,
            String resourceId,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.serviceName = serviceName;
        this.status = status;
        this.resourceId = resourceId;
    }
}

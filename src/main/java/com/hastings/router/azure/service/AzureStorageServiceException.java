package com.hastings.router.azure.service;

public class AzureStorageServiceException extends RuntimeException {

    public AzureStorageServiceException() {
    }

    public AzureStorageServiceException(String message) {
        super(message);
    }

    public AzureStorageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

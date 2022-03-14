package com.hastings.router.user.repository;

import com.hastings.router.azure.service.AzureStorageServiceException;

public class UserException extends RuntimeException {

    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}

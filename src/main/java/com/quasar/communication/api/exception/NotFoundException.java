package com.quasar.communication.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

	private final HttpStatus statusCode;
	private final String errorMessage;

	public NotFoundException(HttpStatus statusCode, String errorMessage) {
		super(errorMessage);
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}

package com.quasar.communication.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

	private HttpStatus statusCode;
	private String errorMessage;

	public NotFoundException(HttpStatus statusCode, String errorMessage) {
		super(errorMessage);
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}

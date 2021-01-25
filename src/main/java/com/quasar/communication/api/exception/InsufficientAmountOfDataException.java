package com.quasar.communication.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InsufficientAmountOfDataException extends Exception {
	
	public InsufficientAmountOfDataException(String message) {
		super(message);
	}
	
}

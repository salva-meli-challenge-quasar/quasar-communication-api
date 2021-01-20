package com.quasar.communication.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MissingDataException extends Exception {
	
	public MissingDataException(String message) {
		super(message);
	}
	
}

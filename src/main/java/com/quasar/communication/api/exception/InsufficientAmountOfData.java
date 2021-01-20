package com.quasar.communication.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InsufficientAmountOfData extends Exception {
	
	public InsufficientAmountOfData(String message) {
		super(message);
	}
	
}

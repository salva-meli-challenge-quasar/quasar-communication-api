package com.quasar.communication.api.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.quasar.communication.api.exception.NotFoundException;
import com.quasar.communication.api.model.ErrorResponse;

@ControllerAdvice
public class QuasarExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = NotFoundException.class)
	ResponseEntity<ErrorResponse> handle(NotFoundException ex, HttpServletRequest request) {
		return new ResponseEntity<>(new ErrorResponse(ex, request.getRequestURI()), ex.getStatusCode());
	}
}

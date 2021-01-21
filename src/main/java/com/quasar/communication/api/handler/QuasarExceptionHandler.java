package com.quasar.communication.api.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.quasar.communication.api.exception.NotFoundException;
import com.quasar.communication.api.model.ErrorResponse;

@ControllerAdvice
public class QuasarExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(QuasarExceptionHandler.class);

	@ExceptionHandler(value = NotFoundException.class)
	ResponseEntity<ErrorResponse> handle(NotFoundException ex, HttpServletRequest request) {
		logger.error("-- Error while processing request - Http Status --", ex.getStatusCode(), ex);
		return new ResponseEntity<>(new ErrorResponse(ex, request.getRequestURI()), ex.getStatusCode());
	}
}

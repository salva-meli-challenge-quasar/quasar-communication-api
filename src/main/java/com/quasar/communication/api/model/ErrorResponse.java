package com.quasar.communication.api.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.quasar.communication.api.exception.NotFoundException;

public class ErrorResponse {

	private String timestamp;
	private int status;
	private String error;
	private String message;
	private String path;

	public ErrorResponse(NotFoundException ex, String url) {
		this.timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		this.status = ex.getStatusCode().value();
		this.error = ex.getStatusCode().getReasonPhrase();
		this.message = ex.getErrorMessage();
		this.path = url;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

package com.quasar.communication.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quasar.api.core.model.Point2D;

public class TopsecretResponse {

	@JsonProperty("position")
	private Point2D position;
	@JsonProperty("message")
	private String message;

	public TopsecretResponse(Point2D position, String message) {
		this.position = position;
		this.message = message;
	}
	
	public TopsecretResponse() {
		
	}
	
	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

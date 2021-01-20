package com.quasar.communication.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quasar.api.core.model.Point2D;

public class TopSecretResponse {

	@JsonProperty("position")
	private Point2D position;
	@JsonProperty("message")
	private String message;

	public TopSecretResponse(Point2D position, String message) {
		this.position = position;
		this.message = message;
	}
	
	public Point2D getPosition() {
		return position;
	}

	public String getMessage() {
		return message;
	}

}

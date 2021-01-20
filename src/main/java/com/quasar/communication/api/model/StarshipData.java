package com.quasar.communication.api.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StarshipData {

	@JsonProperty("name")
	private String satelliteName;
	@NotNull(message = "distance field can not be missing")
	@JsonProperty("distance")
	private Double distance;
	@NotNull(message = "message field can not be missing")
	@JsonProperty("message")
	private String[] message;
	
	public String getSatelliteName() {
		return satelliteName;
	}

	public void setSatelliteName(String satelliteName) {
		this.satelliteName = satelliteName;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}

}

package com.quasar.communication.api.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Valid
public class StarshipData {

	@NotNull(message = "name field can not be missing")
	@JsonProperty("name")
	String satelliteName;
	@NotNull(message = "distance field can not be missing")
	@JsonProperty("distance")
	Double distance;
	@NotNull(message = "message field can not be missing")
	@JsonProperty("message")
	String[] message;

	public String getSatelliteName() {
		return satelliteName;
	}

	public void setSatelliteName(String satelliteName) {
		this.satelliteName = satelliteName;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}

}

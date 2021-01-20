package com.quasar.communication.api.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopsecretRequest {

	@NotNull(message = "satellites field can not be missing")
	@NotEmpty(message = "satellites can not be empty")
	@JsonProperty("satellites")
	private List<StarshipData> starshipData;

	public List<StarshipData> getStarshipData() {
		return starshipData;
	}

	public void setStarshipData(List<StarshipData> starshipData) {
		this.starshipData = starshipData;
	}

}

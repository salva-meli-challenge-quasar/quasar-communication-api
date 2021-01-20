package com.quasar.communication.api.builder;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.entity.StarshipDataReceived;

@Service
public class StarshipDataReceivedBuilder {

	private Satellite satellite;
	private Double distance;
	private String message;
	private LocalDateTime dateCreated;

	public StarshipDataReceived build() {
		if(!validateFields()) {
			return null;
		}
		this.dateCreated = LocalDateTime.now();
		return new StarshipDataReceived(this);
	}
	
	public StarshipDataReceivedBuilder withSatellite(Satellite satellite) {
		this.satellite = satellite;
		return this;
	}
	
	public StarshipDataReceivedBuilder withDistance(Double distance) {
		this.distance = distance;
		return this;
	}
	
	public StarshipDataReceivedBuilder withMessage(String message) {
		this.message = message;
		return this;
	}
	
	private boolean validateFields() {
		return this.satellite != null && this.distance != null && this.message != null;
	}
	
	public Satellite getSatellite() {
		return satellite;
	}

	public Double getDistance() {
		return distance;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

}

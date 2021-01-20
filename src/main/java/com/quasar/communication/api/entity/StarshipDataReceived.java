package com.quasar.communication.api.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quasar.communication.api.builder.StarshipDataReceivedBuilder;

@Entity
@Table(name = "starship_data_received")
public class StarshipDataReceived {

	@Id
	@GeneratedValue
	@Column(name = "s_id")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "satellite_id", nullable = false)
	@JsonIgnore
	private Satellite satellite;
	@Column(name = "distance")
	private double distance;
	@Column(name = "message")
	private String message;
	@Column(name = "date_created")
	private LocalDateTime dateCreated;

	public StarshipDataReceived() {

	}

	public StarshipDataReceived(StarshipDataReceivedBuilder starshipDataReceivedBuilder) {
		this.satellite = starshipDataReceivedBuilder.getSatellite();
		this.distance = starshipDataReceivedBuilder.getDistance();
		this.message = starshipDataReceivedBuilder.getMessage();
		this.dateCreated = starshipDataReceivedBuilder.getDateCreated();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Satellite getSatellite() {
		return satellite;
	}

	public void setSatellite(Satellite satellite) {
		this.satellite = satellite;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

}

package com.quasar.communication.api.manager;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.communication.api.builder.StarshipDataReceivedBuilder;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.entity.StarshipDataReceived;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.repository.SatelliteRepository;
import com.quasar.communication.api.repository.StarshipDataReceivedRepository;

@Service
public class StarshipDataManager {

	@Autowired
	StarshipDataReceivedBuilder starshipDataReceivedBuilder;
	@Autowired
	StarshipDataReceivedRepository starshipDataReceivedRepository;
	@Autowired
	SatelliteRepository satelliteRepository;
	@Autowired
	ObjectMapper objectMapper;

	public StarshipDataReceived save(StarshipData starshipData)
			throws NoSuchSatelliteException, JsonProcessingException {
		Satellite satellite = satelliteRepository.findByName(starshipData.getSatelliteName());
		if (satellite == null) {
			throw new NoSuchSatelliteException(
					String.format("Satellite %s does not exist", starshipData.getSatelliteName()));
		}
		return starshipDataReceivedRepository
				.save(starshipDataReceivedBuilder.withSatellite(satellite).withDistance(starshipData.getDistance())
						.withMessage(objectMapper.writeValueAsString(starshipData.getMessage())).build());
	}

	public void save(Map<Satellite, StarshipData> satelliteStarshipData)
			throws JsonProcessingException {
		for (Entry<Satellite, StarshipData> entry : satelliteStarshipData.entrySet()) {
			starshipDataReceivedRepository.save(starshipDataReceivedBuilder.withSatellite(entry.getKey())
					.withDistance(entry.getValue().getDistance())
					.withMessage(objectMapper.writeValueAsString(entry.getValue().getMessage()))
					.build());
		}
	}
}

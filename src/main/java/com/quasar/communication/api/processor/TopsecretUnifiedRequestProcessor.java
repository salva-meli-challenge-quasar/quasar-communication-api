package com.quasar.communication.api.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.api.core.model.Point2D;
import com.quasar.api.core.response.LocationResponse;
import com.quasar.api.core.response.MessageResponse;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.manager.StarshipDataManager;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.model.TopsecretRequest;
import com.quasar.communication.api.model.TopsecretResponse;
import com.quasar.communication.api.repository.SatelliteRepository;

@Service
public class TopsecretUnifiedRequestProcessor extends TopsecretRequestProcessor {

	@Autowired
	private SatelliteRepository satelliteRepository;
	@Autowired
	private StarshipDataManager starshipDataManager;

	public TopsecretResponse process(TopsecretRequest topsecretRequest)
			throws NoSuchSatelliteException, JsonProcessingException, MissingDataException {
		List<Point2D> points = new ArrayList<>();
		double[] distances = new double[topsecretRequest.getStarshipData().size()];
		String[][] messages = new String[topsecretRequest.getStarshipData().size()][];
		Map<Satellite, StarshipData> satelliteStarshipData = new HashMap<>();
		for (int index = 0; index < topsecretRequest.getStarshipData().size(); index++) {
			satelliteStarshipData.put(processStarshipData(topsecretRequest.getStarshipData().get(index), points,
					distances, messages, index), topsecretRequest.getStarshipData().get(index));
		}
		LocationResponse locationResponse = sendLocationRequest(points, distances);
		MessageResponse messageResponse = sendMessageRequest(messages);
		starshipDataManager.save(satelliteStarshipData);
		return new TopsecretResponse(locationResponse.getLocation(), messageResponse.getMessage());
	}

	private Satellite processStarshipData(StarshipData starshipData, List<Point2D> points, double[] distances,
			String[][] messages, int index)
			throws MissingDataException, NoSuchSatelliteException {
		validateStarshipData(starshipData);
		Satellite satellite = satelliteRepository.findByName(starshipData.getSatelliteName().toLowerCase());
		if (satellite == null) {
			throw new NoSuchSatelliteException(
					String.format("Satellite with name: %s does not exist", starshipData.getSatelliteName()));
		}
		points.add(satellite.getPosition());
		distances[index] = starshipData.getDistance();
		messages[index] = starshipData.getMessage();
		return satellite;
	}

	public void validateStarshipData(StarshipData starshipData) throws MissingDataException {
		if (starshipData.getDistance() == null || starshipData.getMessage() == null
				|| starshipData.getSatelliteName() == null || starshipData.getSatelliteName().trim().isEmpty()) {
			throw new MissingDataException("There is missing data - check your request");
		}
	}
}

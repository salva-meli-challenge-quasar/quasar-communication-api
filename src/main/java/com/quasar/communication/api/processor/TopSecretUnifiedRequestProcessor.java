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
import com.quasar.communication.api.model.TopSecretRequest;
import com.quasar.communication.api.model.TopSecretResponse;

@Service
public class TopSecretUnifiedRequestProcessor extends TopSecretRequestProcessor {
	
	@Autowired
	private StarshipDataManager starshipDataManager;

	public TopSecretResponse process(TopSecretRequest topSecretRequest)
			throws NoSuchSatelliteException, JsonProcessingException, MissingDataException {
		List<Point2D> points = new ArrayList<>();
		double[] distances = new double[topSecretRequest.getStarshipData().size()];
		String[][] messages = new String[topSecretRequest.getStarshipData().size()][];
		Map<Satellite, StarshipData> satelliteStarshipData = new HashMap<>();
		for (int index = 0; index < topSecretRequest.getStarshipData().size(); index++) {
			satelliteStarshipData.put(processStarshipData(topSecretRequest.getStarshipData().get(index), points,
					distances, messages, index), topSecretRequest.getStarshipData().get(index));
		}
		LocationResponse locationResponse = sendLocationRequest(points, distances);
		MessageResponse messageResponse = sendMessageRequest(messages);
		starshipDataManager.save(satelliteStarshipData);
		return new TopSecretResponse(locationResponse.getLocation(), messageResponse.getMessage());
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

	private void validateStarshipData(StarshipData starshipData) throws MissingDataException {
		if (starshipData.getDistance() == null || starshipData.getMessage() == null
				|| starshipData.getSatelliteName() == null || starshipData.getSatelliteName().trim().isEmpty()) {
			throw new MissingDataException("There is missing data - check your request");
		}
	}
}

package com.quasar.communication.api.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.api.core.model.Point2D;
import com.quasar.api.core.response.LocationResponse;
import com.quasar.api.core.response.MessageResponse;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.entity.StarshipDataReceived;
import com.quasar.communication.api.exception.InsufficientAmountOfData;
import com.quasar.communication.api.model.TopSecretRequest;
import com.quasar.communication.api.model.TopSecretResponse;

@Service
public class TopSecretSplitRequestProcessor extends TopSecretRequestProcessor {

	@Value("${minutes.tolerance.valid.data}")
	private int minutesTolerance;

	private static final Logger logger = LogManager.getLogger(TopSecretSplitRequestProcessor.class);

	public TopSecretResponse process(TopSecretRequest topSecretRequest)
			throws JsonProcessingException, InsufficientAmountOfData {
		logger.info("**** Processing request ****");
		List<Satellite> satellites = satelliteRepository.findAll();
		List<StarshipDataReceived> validData = getValidData(satellites);
		if (validData.isEmpty()) {
			logger.error("---- Not enough data ----");
			throw new InsufficientAmountOfData("The amount of data required is not enough");
		}
		List<Point2D> points = new ArrayList<>();
		double[] distances = new double[validData.size()];
		String[][] messages = new String[validData.size()][];
		prepareRequestsData(validData, points, distances, messages);
		LocationResponse locationResponse = sendLocationRequest(points, distances);
		MessageResponse messageResponse = sendMessageRequest(messages);
		return new TopSecretResponse(locationResponse.getLocation(), messageResponse.getMessage());
	}

	private void prepareRequestsData(List<StarshipDataReceived> validData, List<Point2D> points, double[] distances,
			String[][] messages) throws JsonProcessingException {
		for (int index = 0; index < validData.size(); index++) {
			points.add(validData.get(index).getSatellite().getPosition());
			distances[index] = validData.get(index).getDistance();
			messages[index] = objectMapper.readValue(validData.get(index).getMessage(), String[].class);
		}
	}

	private List<StarshipDataReceived> getValidData(List<Satellite> satellites) {
		List<StarshipDataReceived> validData = new ArrayList<>();
		for (Satellite satellite : satellites) {
			StarshipDataReceived starshipDataReceived = satellite.getStarshipDataReceived().stream()
					.max(Comparator.comparing(StarshipDataReceived::getDateCreated)).orElse(null);
			if (starshipDataReceived != null
					&& starshipDataReceived.getDateCreated().isAfter(LocalDateTime.now().minusMinutes(1))) {
				logger.debug("** Valid data found - Satellite: {} - Created date: {} **",
						starshipDataReceived.getSatellite().getName(), starshipDataReceived.getDateCreated());
				validData.add(starshipDataReceived);
			}
		}
		logger.debug("** Amount of valid data: {} **", validData.size());
		return validData;
	}
}

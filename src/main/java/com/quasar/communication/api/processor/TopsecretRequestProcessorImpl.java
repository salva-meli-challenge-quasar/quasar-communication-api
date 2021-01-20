package com.quasar.communication.api.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.api.core.model.Point2D;
import com.quasar.api.core.request.LocationRequest;
import com.quasar.api.core.request.MessageRequest;
import com.quasar.api.core.response.LocationResponse;
import com.quasar.api.core.response.MessageResponse;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.model.TopsecretRequest;
import com.quasar.communication.api.model.TopsecretResponse;
import com.quasar.communication.api.repository.SatelliteRepository;
import com.quasar.communication.api.request.sender.RequestSender;

@Service
public class TopsecretRequestProcessorImpl {

	@Autowired
	SatelliteRepository satelliteRepository;
	@Autowired
	RequestSender requestSender;
	@Autowired
	ObjectMapper objectMapper;
	@Value("${location.api.url}")
	private String locationAPIUrl;
	@Value("${location.api.service}")
	private String locationAPIService;
	@Value("${message.api.url}")
	private String messageAPIUrl;
	@Value("${message.api.service}")
	private String messageAPIService;

	public TopsecretResponse process(TopsecretRequest topsecretRequest)
			throws NoSuchSatelliteException, JsonProcessingException, MissingDataException {
		List<Point2D> points = new ArrayList<>();
		double[] distances = new double[topsecretRequest.getStarshipData().size()];
		String[][] messages = new String[topsecretRequest.getStarshipData().size()][];
		for (int index = 0; index < topsecretRequest.getStarshipData().size(); index++) {
			processStarshipData(topsecretRequest.getStarshipData().get(index), points, distances, messages, index);
		}
		LocationResponse locationResponse = sendLocationRequest(points, distances);
		MessageResponse messageResponse = sendMessageRequest(messages);
		return new TopsecretResponse(locationResponse.getLocation(), messageResponse.getMessage());
	}

	private MessageResponse sendMessageRequest(String[][] messages)
			throws JsonProcessingException {
		return objectMapper.readValue(requestSender.send(messageAPIUrl + messageAPIService,
				MediaType.APPLICATION_JSON, objectMapper.writeValueAsString(new MessageRequest(messages))),
				MessageResponse.class);
	}

	private LocationResponse sendLocationRequest(List<Point2D> points, double[] distances)
			throws JsonProcessingException {
		return objectMapper.readValue(
				requestSender.send(locationAPIUrl + locationAPIService, MediaType.APPLICATION_JSON,
						objectMapper.writeValueAsString(new LocationRequest(points, distances))),
				LocationResponse.class);
	}

	private void processStarshipData(StarshipData starshipData, List<Point2D> points, double[] distances,
			String[][] messages, int index) throws MissingDataException, NoSuchSatelliteException {
		validateStarshipData(starshipData);
		Satellite satellite = satelliteRepository.findByName(starshipData.getSatelliteName().toLowerCase());
		if (satellite == null) {
			throw new NoSuchSatelliteException(
					String.format("Satellite with name: %s does not exist", starshipData.getSatelliteName()));
		}
		points.add(satellite.getPosition());
		distances[index] = starshipData.getDistance();
		messages[index] = starshipData.getMessage();
	}

	public void validateStarshipData(StarshipData starshipData) throws MissingDataException {
		if (starshipData.getDistance() == null || starshipData.getMessage() == null
				|| starshipData.getSatelliteName() == null || starshipData.getSatelliteName().trim().isEmpty()) {
			throw new MissingDataException("There is missing data - check your request");
		}
	}
}

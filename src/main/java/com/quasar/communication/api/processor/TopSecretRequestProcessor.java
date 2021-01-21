package com.quasar.communication.api.processor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.api.core.model.Point2D;
import com.quasar.api.core.request.LocationRequest;
import com.quasar.api.core.request.MessageRequest;
import com.quasar.api.core.response.LocationResponse;
import com.quasar.api.core.response.MessageResponse;
import com.quasar.communication.api.exception.InsufficientAmountOfData;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.TopSecretRequest;
import com.quasar.communication.api.model.TopSecretResponse;
import com.quasar.communication.api.repository.SatelliteRepository;
import com.quasar.communication.api.request.sender.RequestSender;

public abstract class TopSecretRequestProcessor {

	@Autowired
	private RequestSender requestSender;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	protected SatelliteRepository satelliteRepository;
	@Value("${location.api.url}")
	protected String locationAPIUrl;
	@Value("${location.api.service}")
	protected String locationAPIService;
	@Value("${message.api.url}")
	protected String messageAPIUrl;
	@Value("${message.api.service}")
	protected String messageAPIService;

	private static final Logger logger = LogManager.getLogger(TopSecretRequestProcessor.class);

	protected MessageResponse sendMessageRequest(String[][] messages) throws JsonProcessingException {
		logger.debug("** Sending messageRequest **");
		MessageResponse messageResponse = objectMapper.readValue(requestSender.send(messageAPIUrl + messageAPIService,
				MediaType.APPLICATION_JSON, objectMapper.writeValueAsString(new MessageRequest(messages))),
				MessageResponse.class);
		logger.debug("** OK - messageResponse received **");
		return messageResponse;
	}

	protected LocationResponse sendLocationRequest(List<Point2D> points, double[] distances)
			throws JsonProcessingException {
		logger.debug("** Sending locationRequest **");
		LocationResponse locationResponse = objectMapper.readValue(
				requestSender.send(locationAPIUrl + locationAPIService, MediaType.APPLICATION_JSON,
						objectMapper.writeValueAsString(new LocationRequest(points, distances))),
				LocationResponse.class);
		logger.debug("** OK - locationResponse received **");
		return locationResponse;
	}

	public abstract TopSecretResponse process(TopSecretRequest topSecretRequest)
			throws NoSuchSatelliteException, JsonProcessingException, MissingDataException, InsufficientAmountOfData;
}

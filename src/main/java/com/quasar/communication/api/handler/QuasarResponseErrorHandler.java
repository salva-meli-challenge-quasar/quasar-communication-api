package com.quasar.communication.api.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.communication.api.exception.NotFoundException;

@Component
public class QuasarResponseErrorHandler implements ResponseErrorHandler {

	private static final Logger logger = LogManager.getLogger(QuasarResponseErrorHandler.class);

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				|| response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			logger.error("---- Error while processing request: SERVER ERROR ----");
			throw new IOException();
		}
		if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			JsonNode jsonNode = new ObjectMapper()
					.readTree(new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
							.lines().collect(Collectors.joining("\n")));
			logger.error("---- Error while processing request: {} ----", jsonNode.path("message").asText());
			throw new NotFoundException(HttpStatus.NOT_FOUND, jsonNode.path("message").asText());
		}
	}

}

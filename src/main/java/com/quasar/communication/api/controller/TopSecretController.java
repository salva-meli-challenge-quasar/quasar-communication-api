package com.quasar.communication.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.communication.api.exception.InsufficientAmountOfDataException;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.TopSecretRequest;
import com.quasar.communication.api.model.TopSecretResponse;
import com.quasar.communication.api.processor.TopSecretRequestProcessor;

@RestController
public class TopSecretController {

	@Autowired
	@Qualifier("topSecretUnifiedRequestProcessor")
	private TopSecretRequestProcessor topSecretRequestProcessor;

	@PostMapping(value = "/topsecret", consumes = "application/json", produces = "application/json")
	public TopSecretResponse topSecret(@Valid @RequestBody TopSecretRequest topsecretRequest)
			throws NoSuchSatelliteException, MissingDataException, JsonProcessingException, InsufficientAmountOfDataException {
		return topSecretRequestProcessor.process(topsecretRequest);
	}

}

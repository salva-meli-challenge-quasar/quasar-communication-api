package com.quasar.communication.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.TopsecretRequest;
import com.quasar.communication.api.model.TopsecretResponse;
import com.quasar.communication.api.processor.TopsecretRequestProcessor;

@RestController
public class TopSecretController {

	@Autowired
	private TopsecretRequestProcessor topsecretRequestProcessor;

	@PostMapping(value = "/topsecret", consumes = "application/json", produces = "application/json")
	public TopsecretResponse topsecret(@Valid @RequestBody TopsecretRequest topsecretRequest)
			throws NoSuchSatelliteException, MissingDataException, JsonProcessingException {
		return topsecretRequestProcessor.process(topsecretRequest);
	}
	
}

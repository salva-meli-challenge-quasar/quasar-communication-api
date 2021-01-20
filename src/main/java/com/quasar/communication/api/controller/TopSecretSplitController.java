package com.quasar.communication.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.communication.api.exception.InsufficientAmountOfData;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.manager.StarshipDataManager;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.model.TopSecretResponse;
import com.quasar.communication.api.processor.TopSecretSplitRequestProcessor;

@RestController
public class TopSecretSplitController {

	@Autowired
	StarshipDataManager starshipDataManager;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	@Qualifier("topSecretSplitRequestProcessor")
	TopSecretSplitRequestProcessor topSecretRequestProcessor;

	@PostMapping(value = "/topsecret_split/{satellite_name}", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public String topSecretSplitPost(@PathVariable("satellite_name") String satelliteName,
			@RequestBody StarshipData starshipData) throws JsonProcessingException, NoSuchSatelliteException {
		starshipData.setSatelliteName(satelliteName);
		return objectMapper.writeValueAsString(starshipDataManager.save(starshipData));
	}

	@GetMapping(value = "/topsecret_split", produces = "application/json")
	public TopSecretResponse topSecretSplitGetData()
			throws JsonProcessingException, InsufficientAmountOfData {
		return topSecretRequestProcessor.process(null);
	}
	
}

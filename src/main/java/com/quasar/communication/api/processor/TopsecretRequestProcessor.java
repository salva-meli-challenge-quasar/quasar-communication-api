package com.quasar.communication.api.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.TopsecretRequest;
import com.quasar.communication.api.model.TopsecretResponse;

public interface TopsecretRequestProcessor {

	public TopsecretResponse process(TopsecretRequest topsecretRequest)
			throws NoSuchSatelliteException, JsonProcessingException, MissingDataException;
	
}

package com.quasar.communication.api.processor;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quasar.communication.api.exception.MissingDataException;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.model.TopsecretRequest;
import com.quasar.communication.api.repository.SatelliteRepository;
import com.quasar.communication.api.request.sender.RequestSender;

@ExtendWith(MockitoExtension.class)
class TestTopsecretRequestProcessor {

	@Mock
	private SatelliteRepository satelliteRepository;
	@Mock
	private RequestSender requestSender;
	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private TopsecretRequestProcessorImpl topsecretRequestProcessor;


	@Test
	void testNotValidSatelliteName() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setSatelliteName("satellite");
		starship.setDistance(10.0);
		starship.setMessage(new String[] { "mensaje" });
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		when(satelliteRepository.findByName("satellite")).thenReturn(null);
		assertThrows(NoSuchSatelliteException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}

	@Test
	void testEmptySatelliteName() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setSatelliteName("");
		starship.setDistance(10.0);
		starship.setMessage(new String[] { "mensaje" });
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		assertThrows(MissingDataException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}
	
	@Test
	void testEmptySatelliteNameWithSpaces() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setSatelliteName("        ");
		starship.setDistance(10.0);
		starship.setMessage(new String[] { "mensaje" });
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		assertThrows(MissingDataException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}

	@Test
	void testMissingSatelliteNameField() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setDistance(10.0);
		starship.setMessage(new String[] { "mensaje" });
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		assertThrows(MissingDataException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}

	@Test
	void testMissingDistanceField() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setSatelliteName("satellite");
		starship.setMessage(new String[] { "mensaje" });
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		assertThrows(MissingDataException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}

	@Test
	void testMissingMessageField() {
		TopsecretRequest topsecretRequest = new TopsecretRequest();
		List<StarshipData> starshipData = new ArrayList<>();
		StarshipData starship = new StarshipData();
		starship.setSatelliteName("satellite");
		starship.setDistance(10.0);
		starshipData.add(starship);
		topsecretRequest.setStarshipData(starshipData);
		assertThrows(MissingDataException.class, () -> {
			topsecretRequestProcessor.process(topsecretRequest);
		});
	}
}

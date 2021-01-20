package com.quasar.communication.api.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.api.core.model.Point2D;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.entity.StarshipDataReceived;
import com.quasar.communication.api.exception.NoSuchSatelliteException;
import com.quasar.communication.api.manager.StarshipDataManager;
import com.quasar.communication.api.model.StarshipData;
import com.quasar.communication.api.repository.SatelliteRepository;
import com.quasar.communication.api.repository.StarshipDataReceivedRepository;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TestTopSecretSplitController {

	@Autowired
	SatelliteRepository satelliteRepository;

	@Autowired
	StarshipDataReceivedRepository starshipDataReceivedRepository;
	
	@Autowired
	StarshipDataManager starshipDataManager;

	@Autowired
	TopSecretController topsecretController;

	@Autowired
	MockMvc mockMvc;

	@Value("${minutes.tolerance.valid.data}")
	private int tolerationTime;
	
	@Test
	void testOneRecordInBD() throws Exception {
		prepareOneSatelliteAndStarshipData();
		mockMvc.perform(MockMvcRequestBuilders.get("/topsecret_split"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void testTwoRecordsDifferentSatellitesInBD() throws Exception {
		prepareTwoSatellitesAndStarshipData();
		mockMvc.perform(MockMvcRequestBuilders.get("/topsecret_split"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	void testThreeRecordsDifferentSatellitesInBD() throws Exception {
		prepareThreeValidSatellitesAndStarshipData();
		mockMvc.perform(MockMvcRequestBuilders.get("/topsecret_split"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	void testThreeRecordsDifferentSatellitesAndOneOldInBD() throws Exception {
		prepareThreeValidSatellitesAndStarshipData();
		addOldRecord();
		mockMvc.perform(MockMvcRequestBuilders.get("/topsecret_split"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	private void prepareOneSatelliteAndStarshipData() throws JsonProcessingException, NoSuchSatelliteException {
		Satellite satellite1 = new Satellite();
		satellite1.setDateCreated(LocalDateTime.now());
		satellite1.setLastModificationDate(LocalDateTime.now());
		satellite1.setName("satellite1");
		satellite1.setPosition(new Point2D(50, 0));
		satelliteRepository.save(satellite1);
		StarshipData starshipData1 = new StarshipData();
		starshipData1.setDistance(250.0);
		starshipData1.setMessage(new String[] { "mensaje" });
		starshipData1.setSatelliteName("satellite1");
		starshipDataManager.save(starshipData1);
	}
	
	private void addOldRecord() throws JsonProcessingException, NoSuchSatelliteException {
		Satellite satellite4 = new Satellite();
		satellite4.setDateCreated(LocalDateTime.now());
		satellite4.setLastModificationDate(LocalDateTime.now());
		satellite4.setName("satellite4");
		satellite4.setPosition(new Point2D(150, 0));
		StarshipDataReceived starshipDataReceived = new StarshipDataReceived();
		starshipDataReceived.setDateCreated(LocalDateTime.now().minusMinutes(tolerationTime + 15));
		starshipDataReceived.setSatellite(satelliteRepository.save(satellite4));
		starshipDataReceived.setMessage("[\"mensaje\"]");
		starshipDataReceived.setDistance(900.0);
		starshipDataReceivedRepository.save(starshipDataReceived);
	}
	
	private void prepareTwoSatellitesAndStarshipData() throws JsonProcessingException, NoSuchSatelliteException {
		prepareOneSatelliteAndStarshipData();
		Satellite satellite2 = new Satellite();
		satellite2.setDateCreated(LocalDateTime.now());
		satellite2.setLastModificationDate(LocalDateTime.now());
		satellite2.setName("satellite2");
		satellite2.setPosition(new Point2D(200, 0));
		satelliteRepository.save(satellite2);
		StarshipData starshipData2 = new StarshipData();
		starshipData2.setDistance(400.0);
		starshipData2.setMessage(new String[] { "mensaje" });
		starshipData2.setSatelliteName("satellite2");
		starshipDataManager.save(starshipData2);
	}
	
	private void prepareThreeValidSatellitesAndStarshipData() throws JsonProcessingException, NoSuchSatelliteException {
		prepareTwoSatellitesAndStarshipData();
		Satellite satellite3 = new Satellite();
		satellite3.setDateCreated(LocalDateTime.now());
		satellite3.setLastModificationDate(LocalDateTime.now());
		satellite3.setName("satellite3");
		satellite3.setPosition(new Point2D(400, 200));
		satelliteRepository.save(satellite3);
		StarshipData starshipData3 = new StarshipData();
		starshipData3.setDistance(632.455532);
		starshipData3.setMessage(new String[] { "mensaje" });
		starshipData3.setSatelliteName("satellite3");
		starshipDataManager.save(starshipData3);
	}
}

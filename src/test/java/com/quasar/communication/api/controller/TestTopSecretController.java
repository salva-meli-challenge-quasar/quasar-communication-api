package com.quasar.communication.api.controller;

import java.nio.file.Files;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.quasar.api.core.model.Point2D;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.repository.SatelliteRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TestTopSecretController {

	@Autowired
	private SatelliteRepository satelliteRepository;
	
	@Autowired
	private MockMvc mockMvc;

	@Value("classpath:/requests/emptyRequest.json")
	private Resource emptyRequestResource;

	@Value("classpath:/requests/validRequest.json")
	private Resource validRequestResource;

	@Value("classpath:/requests/missingFieldRequest.json")
	private Resource missingFieldRequestResource;
	
	@Value("classpath:/requests/noSolutionRequest.json")
	private Resource noSolutionRequestResource;
	
	@Test
	void testEmptyRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.emptyRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/topsecret").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testValidRequest() throws Exception {
		addThreeSatellites();
		String json = new String(Files.readAllBytes(this.validRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/topsecret").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void testMissingFieldRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.noSolutionRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/topsecret").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	private void addThreeSatellites() {
		Satellite satellite1 = new Satellite();
		satellite1.setDateCreated(LocalDateTime.now());
		satellite1.setLastModificationDate(LocalDateTime.now());
		satellite1.setName("kenovi");
		satellite1.setPosition(new Point2D(50, 0));
		Satellite satellite2 = new Satellite();
		satellite2.setDateCreated(LocalDateTime.now());
		satellite2.setLastModificationDate(LocalDateTime.now());
		satellite2.setName("skywalker");
		satellite2.setPosition(new Point2D(200, 0));
		Satellite satellite3 = new Satellite();
		satellite3.setDateCreated(LocalDateTime.now());
		satellite3.setLastModificationDate(LocalDateTime.now());
		satellite3.setName("sato");
		satellite3.setPosition(new Point2D(400, 200));
		satelliteRepository.save(satellite1);
		satelliteRepository.save(satellite2);
		satelliteRepository.save(satellite3);
	}
	
}

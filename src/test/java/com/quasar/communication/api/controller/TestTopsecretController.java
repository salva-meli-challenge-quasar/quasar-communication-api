package com.quasar.communication.api.controller;

import java.nio.file.Files;

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

@SpringBootTest
@AutoConfigureMockMvc
public class TestTopsecretController {

	@Autowired
	TopsecretController topsecretController;

	@Autowired
	MockMvc mockMvc;

	@Value("classpath:/requests/emptyRequest.json")
	Resource emptyRequestResource;

	@Value("classpath:/requests/validRequest.json")
	Resource validRequestResource;

	@Test
	void testEmptyRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.emptyRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/topsecret").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testValidRequest() throws Exception {
		String json = new String(Files.readAllBytes(this.validRequestResource.getFile().toPath()));
		mockMvc.perform(MockMvcRequestBuilders.post("/topsecret").content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	}
}

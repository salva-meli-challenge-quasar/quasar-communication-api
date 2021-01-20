package com.quasar.communication.api.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quasar.api.core.model.Point2D;
import com.quasar.api.core.response.LocationResponse;
import com.quasar.api.core.response.MessageResponse;
import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.entity.StarshipDataReceived;
import com.quasar.communication.api.exception.InsufficientAmountOfData;
import com.quasar.communication.api.model.TopsecretResponse;
import com.quasar.communication.api.repository.SatelliteRepository;

@Service
public class TopSecretSplitRequestProcessor extends TopSecretRequestProcessor {

	@Autowired
	private SatelliteRepository satelliteRepository;
	
	@Value("${minutes.tolerance.valid.data}")
	private int minutesTolerance;
	
	public TopsecretResponse process() throws JsonProcessingException, InsufficientAmountOfData {
		List<Satellite> satellites = satelliteRepository.findAll();
		List<StarshipDataReceived> validData = getValidData(satellites);
		if(validData.isEmpty()) {
			throw new InsufficientAmountOfData("The amount of data required is not enough");
		}
		List<Point2D> points = new ArrayList<>();
		double[] distances = new double[validData.size()];
		String[][] messages = new String[validData.size()][];
		prepareRequestsData(validData, points, distances, messages);
		LocationResponse locationResponse = sendLocationRequest(points, distances);
		MessageResponse messageResponse = sendMessageRequest(messages);
		return new TopsecretResponse(locationResponse.getLocation(), messageResponse.getMessage());
	}

	private void prepareRequestsData(List<StarshipDataReceived> validData, List<Point2D> points, double[] distances,
			String[][] messages) throws JsonProcessingException {
		for(int index = 0; index < validData.size(); index ++) {
			points.add(validData.get(index).getSatellite().getPosition());
			distances[index] = validData.get(index).getDistance();
			messages[index] = objectMapper.readValue(validData.get(index).getMessage(), String[].class);
		}
	}

	private List<StarshipDataReceived> getValidData(List<Satellite> satellites) {
		 List<StarshipDataReceived> validData = new ArrayList<>();
		for (Satellite satellite : satellites) {
			StarshipDataReceived starshipDataReceived = satellite.getStarshipDataReceived().stream()
					.max(Comparator.comparing(StarshipDataReceived::getDateCreated)).orElse(null);
			if(starshipDataReceived != null && starshipDataReceived.getDateCreated().isAfter(LocalDateTime.now().minusMinutes(1))) {
			 validData.add(starshipDataReceived);
			}
		}
		return validData;
	}
}

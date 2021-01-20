package com.quasar.communication.api.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.quasar.communication.api.entity.Satellite;
import com.quasar.communication.api.repository.SatelliteRepository;

@Transactional
public class SatelliteDAO {

	@Autowired
	private SatelliteRepository satelliteRepository;
	
	public Satellite getsSatelliteByName(String name) {
		return satelliteRepository.findByName(name);
	}
	
}

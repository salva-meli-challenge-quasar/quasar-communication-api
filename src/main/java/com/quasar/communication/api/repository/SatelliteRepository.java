package com.quasar.communication.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quasar.communication.api.entity.Satellite;

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Integer> {

	public Satellite findByName(String name);
	
}

package com.quasar.communication.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quasar.communication.api.entity.StarshipDataReceived;

@Repository
public interface StarshipDataReceivedRepository extends JpaRepository<StarshipDataReceived, Integer> {

}

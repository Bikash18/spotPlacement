package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.SpotAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpotAvailabilityRepository extends MongoRepository<SpotAvailability, Integer > {

}



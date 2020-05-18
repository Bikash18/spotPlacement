package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.Map;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapRepository extends MongoRepository<Map, Integer > {

}

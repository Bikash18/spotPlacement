package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.Area;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AreaRepository extends MongoRepository<Area, String > {

}

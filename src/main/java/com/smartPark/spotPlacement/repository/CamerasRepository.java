package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.CamStatus;
import com.smartPark.spotPlacement.model.Cameras;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CamerasRepository extends MongoRepository<Cameras, String>{


}
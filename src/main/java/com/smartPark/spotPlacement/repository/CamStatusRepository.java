package com.smartPark.spotPlacement.repository;

import com.smartPark.spotPlacement.model.CamStatus;
import com.smartPark.spotPlacement.model.CamToGlobalId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CamStatusRepository extends MongoRepository<CamStatus, String> {

}

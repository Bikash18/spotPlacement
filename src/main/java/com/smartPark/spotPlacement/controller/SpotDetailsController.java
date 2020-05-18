
package com.smartPark.spotPlacement.controller;

import com.smartPark.spotPlacement.model.Map;
import com.smartPark.spotPlacement.service.SpotPlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RestController
    public class SpotDetailsController {

        @Autowired
        SpotPlacementService spotPlacementService;

        @GetMapping("/spots/details")
        public List<Map> getSpots() {
            return spotPlacementService.getSpotDetails();
        }


    }

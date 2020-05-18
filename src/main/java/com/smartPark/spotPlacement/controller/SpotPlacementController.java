package com.smartPark.spotPlacement.controller;

import com.smartPark.spotPlacement.model.*;
import com.smartPark.spotPlacement.service.SpotPlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SpotPlacementController {

    @Autowired
    SpotPlacementService spotPlacementService;

    @GetMapping("/spots/status")
    public List<SpotAvailability> getSpotStatus() {
        return spotPlacementService.getSpotStatus();
    }

    @PutMapping("/spots/status")
    public List<SpotAvailability> updateSpotStatus(@Valid @RequestBody ArrayList<SpotUpdateRequest> spotUpdateBody){
        System.out.println("Run ");
        return spotPlacementService.updateSpotStatus(spotUpdateBody);
    }
}

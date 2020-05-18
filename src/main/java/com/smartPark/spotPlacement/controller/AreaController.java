package com.smartPark.spotPlacement.controller;

import com.smartPark.spotPlacement.model.Area;
import com.smartPark.spotPlacement.service.SpotPlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AreaController {


    @Autowired
    SpotPlacementService spotPlacementService;

    @GetMapping("/spots/area")
    public List<Area> getArea() {
        return spotPlacementService.getAreaDetails();
    }

}

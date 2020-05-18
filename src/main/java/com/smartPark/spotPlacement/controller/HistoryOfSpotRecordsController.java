/*
 * @(#)HistoryOfSpotRecordsController.java 1.8 10/04/20
 * Copyright (c) 2020-2021
 */
package com.smartPark.spotPlacement.controller;

import com.smartPark.spotPlacement.service.SpotPlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * HistoryOfSpotRecordsController class is a rest api controller
 * @author LTTS-Compute vision
 * @version 1.0
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HistoryOfSpotRecordsController {

    @Autowired
    SpotPlacementService spotPlacementService;

    // TODO (BIKASH) : Return reposne need to
    @GetMapping("/historyOfSpotRecords/{startDateTime}/{endDateTime}/{intervalTimeInHour}")
    public Object[] getHistoryOfSpotRecords(@PathVariable int startDateTime, @PathVariable int endDateTime,@PathVariable int intervalTimeInHour) {
        Object[]  historyOfSpotRecords=null;
        try {
            historyOfSpotRecords = spotPlacementService.getHistoryOfSpotRecords(startDateTime, endDateTime,intervalTimeInHour);
        }catch(Exception e){
              e.getMessage();
        }
        return historyOfSpotRecords;
    }
}

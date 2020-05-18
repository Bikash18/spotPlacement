package com.smartPark.spotPlacementTest;

import com.smartPark.spotPlacement.service.SpotPlacementService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.junit.Before;

public class ServiceTest {
    @InjectMocks
    SpotPlacementService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

}

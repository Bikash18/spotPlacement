package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;

public class CamStatus {


    private String id;

    private long lastUpdate;

    public CamStatus(String id, long lastUpdate) {
        this.id = id;
        this.lastUpdate = lastUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

package com.smartPark.spotPlacement.model;

import java.util.ArrayList;

public class SpotUpdateRequest {

    private String camId;

    private ArrayList<SpotUpdateStatusRequest> spaceUpdates;

    public SpotUpdateRequest(String cam_id, ArrayList<SpotUpdateStatusRequest> space_updates) {
        this.camId = cam_id;
        this.spaceUpdates = space_updates;
    }

    public String getCamId() {
        return camId;
    }

    public void setCamId(String camId) {
        this.camId = camId;
    }

    public ArrayList<SpotUpdateStatusRequest> getSpaceUpdates() {
        return spaceUpdates;
    }

    public void setSpaceUpdates(ArrayList<SpotUpdateStatusRequest> spaceUpdates) {
        this.spaceUpdates = spaceUpdates;
    }
}

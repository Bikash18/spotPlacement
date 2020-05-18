package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "cam_status")
public class CamSpotStatus {

    @Id
    private String cam_id;

    // TODO : define array type
    private ArrayList<SpotStatus> space_updates;

    public CamSpotStatus(String cam_id, ArrayList<SpotStatus> space_updates) {
        this.cam_id = cam_id;
        this.space_updates = space_updates;
    }

    public String getCam_id() {
        return cam_id;
    }

    public void setCam_id(String cam_id) {
        this.cam_id = cam_id;
    }

    public ArrayList<SpotStatus> getSpace_updates() {
        return space_updates;
    }

    public void setSpace_updates(ArrayList<SpotStatus> space_updates) {
        this.space_updates = space_updates;
    }
}

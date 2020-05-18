package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cameras")
public class Cameras {

    @Id
   private String id;

   private String cam_friendly_name;

   private int number_of_spots;

   private int timeout;


    public Cameras(String id, String cam_friendly_name, int number_of_spots, int timeout) {
        this.id = id;
        this.cam_friendly_name = cam_friendly_name;
        this.number_of_spots = number_of_spots;
        this.timeout = timeout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCamFriendlyName() {
        return cam_friendly_name;
    }

    public void setCamFriendlyName(String camFriendlyName) {
        this.cam_friendly_name = camFriendlyName;
    }

    public int getNumberOfSpots() {
        return number_of_spots;
    }

    public void setNumberOfSpots(int numberOfSpots) {
        this.number_of_spots = numberOfSpots;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

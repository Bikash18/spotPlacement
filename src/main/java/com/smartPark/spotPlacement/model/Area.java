package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document(collection = "area")
public class Area {

    @Id
    private String id;

    private String image;

    private Object gps_points;

    private String scale;

    private String anchor;

    private String area_name;

    public Area(String id, String image, Object gps_points, String scale, String anchor, String area_name) {
        this.id = id;
        this.image = image;
        this.gps_points = gps_points;
        this.scale = scale;
        this.anchor = anchor;
        this.area_name = area_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getGps_points() {
        return gps_points;
    }

    public void setGps_points(Object gps_points) {
        this.gps_points = gps_points;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
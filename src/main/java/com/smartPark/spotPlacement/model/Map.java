package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Document(collection = "map")
public class Map {

    @Id
    @Valid
    @NotEmpty(message = "ID cannot be empty")
    @Size(min=7,max=16, message = "LandmarkId must be equal or greater than 8 characters and less than 16 characters")
    private int id;

    private String primary_camera;

    private String textlong;

    private Object gps_points;

    private String bkcolor;

    private String image;

    private String type;

    private String textshort;

    private Object cam_id_and_area;

    private String txtcolor;

    private String area;

    private String spotType;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrimary_camera() {
        return primary_camera;
    }

    public void setPrimary_camera(String primary_camera) {
        this.primary_camera = primary_camera;
    }

    public String getTextlong() {
        return textlong;
    }

    public void setTextlong(String textlong) {
        this.textlong = textlong;
    }

    public Object getGps_points() {
        return gps_points;
    }

    public void setGps_points(Object gps_points) {
        this.gps_points = gps_points;
    }

    public String getBkcolor() {
        return bkcolor;
    }

    public void setBkcolor(String bkcolor) {
        this.bkcolor = bkcolor;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextshort() {
        return textshort;
    }

    public void setTextshort(String textshort) {
        this.textshort = textshort;
    }

    public Object getCam_id_and_area() {
        return cam_id_and_area;
    }

    public void setCam_id_and_area(Object cam_id_and_area) {
        this.cam_id_and_area = cam_id_and_area;
    }

    public String getTxtcolor() {
        return txtcolor;
    }

    public void setTxtcolor(String txtcolor) {
        this.txtcolor = txtcolor;
    }

    public Map(int id, String primary_camera, String textlong, Object gps_points, String bkcolor, String image, String type, String textshort, Object cam_id_and_area, String txtcolor,String area, String spotType) {
        this.id = id;
        this.primary_camera = primary_camera;
        this.textlong = textlong;
        this.gps_points = gps_points;
        this.bkcolor = bkcolor;
        this.image = image;
        this.type = type;
        this.textshort = textshort;
        this.cam_id_and_area = cam_id_and_area;
        this.txtcolor = txtcolor;
        this.area = area;
        this.spotType = spotType;
    }
}
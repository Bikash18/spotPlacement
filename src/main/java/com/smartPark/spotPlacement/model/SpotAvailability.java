package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "spot_availability")
public class SpotAvailability {

    @Id
    private int id;

    private String status;

    private ArrayList<SpotStatus> cam_reports;

    private long date;

    public SpotAvailability(int id, String status, ArrayList<SpotStatus> cam_reports, long date) {
        this.id = id;
        this.status = status;
        this.cam_reports = cam_reports;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SpotStatus> getCam_reports() {
        return cam_reports;
    }

    public void setCam_reports(ArrayList<SpotStatus> cam_reports) {
        this.cam_reports = cam_reports;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

/*
 * @(#)HistoryOfSpotRecords.java 1.8 10/04/20
 * Copyright (c) 2020-2021
 */

package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * HistoryOfSpotRecords represents history of occupancy of spots
 * @author LTTS-Compute vision
 * @version 1.0
 */
@Document(collection = "history_of_spot_records")
public class HistoryOfSpotRecords {
    @Id
    private BigInteger id;

    private long global_id;

    private String status;

    private long date;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public long getGlobal_id() {
        return global_id;
    }

    public void setGlobal_id(long global_id) {
        this.global_id = global_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public HistoryOfSpotRecords(long global_id, String status, long date) {
        this.global_id = global_id;
        this.status = status;
        this.date = date;
    }
}



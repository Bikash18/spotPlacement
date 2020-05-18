package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "camid_to_global")
public class CamToGlobalId {

    @Id
private String id;

private int global_id;

    public CamToGlobalId(String id, int global_id) {
        this.id = id;
        this.global_id = global_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGlobalId() {
        return global_id;
    }

    public void setGlobalId(int globalId) {
        this.global_id = globalId;
    }
}

package com.smartPark.spotPlacement.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final String userName;

    public String getJwttoken() {
        return jwttoken;
    }

    public String getUserName() {
        return userName;
    }

    public JwtResponse(String jwttoken, String userName) {
        this.jwttoken = jwttoken;
        this.userName = userName;
    }
}
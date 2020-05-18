package com.smartPark.spotPlacement.model;

import java.io.Serializable;

public class MessageResponse  implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String msg;

    public String getMsg() {
        return msg;
    }

    public MessageResponse(String msg) {
        this.msg = msg;
    }
}

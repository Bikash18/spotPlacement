package com.smartPark.spotPlacement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "user")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;



    @NotBlank
    @Size(max = 120)
    private String password;

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }



    public String getPassword() {
        return password;
    }

    public User(String id, String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public User(){

    }
}

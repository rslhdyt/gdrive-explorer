package com.rslhdyt.google.drive.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.io.Serializable;

@Entity(name = "users")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 6283539024303006369L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    private String accessToken;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

package com.example.sanjeevaniadmin.Models;

public class UserModel {


    private String id;
    private String Name;
    private String Profile;


    public UserModel(String id, String name, String profile) {
        this.id = id;
        Name = name;
        Profile = profile;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }
}

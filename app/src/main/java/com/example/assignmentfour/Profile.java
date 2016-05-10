package com.example.assignmentfour;

/**
 * Created by dade on 10/02/16.
 */

public class Profile {
    private int id;
    private String name;
    private String bio;
    private String picture;

    public Profile() {
    }

    public Profile(String name, String bio, String picture) {
        super();
        this.name = name;
        this.bio = bio;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}

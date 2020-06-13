package com.huettner.dan.medicationreminder.client.applicationlogic.entity;

/**
 * Created by Liana on 2018-05-05.
 */

public class Patient {

    private String id;
    private String name;

    public Patient(String id, String name) {
        this.id = id;
        this.name = name;
   }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

}

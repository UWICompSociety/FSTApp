package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Matthew on 7/19/2016.
 */

@Table(name="Place")
public class Place extends Model implements Serializable{


    @SerializedName("id")
    @Column(name="placeId")
    int placeId;

    @SerializedName("fullname")
    @Column(name="fullname")
    String fullname;

    @SerializedName("shortname")
    @Column(name="shortname")
    String shortname;

    @SerializedName("location")
    @Column(name="location")
    String location;

    @SerializedName("department")
    @Column(name="department")
    String department;


    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static Place findOrCreateFromJson(Place new_place) {
        int placeId = new_place.getPlaceId();
        Place existingPlace =
                new Select().from(Place.class).where("placeId = ?", placeId).executeSingle();
        if (existingPlace != null) {
            // found and return existing
            return existingPlace;
        } else {
            // create and return new place
            Place place = new_place;
            place.save();
            return place;
        }
    }
}

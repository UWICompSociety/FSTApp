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
public class Place extends Model{


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

    public Place()
    {
        super();
    }

    public Place(int id,String fullname,String shortname,String location,String department)
    {
        super();
        this.placeId = id;
        this.fullname = fullname;
        this.shortname = shortname;
        this.location = location;
        this.department = department;
    }





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
            UpdatePlace(existingPlace,new_place);
            return existingPlace;
        } else {
            // create and return new place
            Place place = new_place;
            place.save();
            return place;
        }
    }

    private static void UpdatePlace(Place old_place,Place new_place)
    {
        old_place.setFullname(new_place.getFullname());
        old_place.setShortname(new_place.getShortname());
        old_place.setDepartment(new_place.getDepartment());
        old_place.setLocation(new_place.getLocation());
        old_place.save();
    }
}

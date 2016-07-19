package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhanelle on 6/22/2016.
 */

@Table(name = "Scholarship")
public class Scholarship extends Model {

    @SerializedName("id")
    @Column(name = "scholId")
    private int scholId;

    @SerializedName("name")
    @Column(name = "title")
    private String title;

    @SerializedName("description")
    @Column(name = "description")
    private String description;

    @SerializedName("detail")
    @Column(name = "detail")
    private String detail;

    @SerializedName("image_url")
    @Column(name = "image")
    private String image;

    public Scholarship() {
        super();
    }

    public Scholarship(int iD, String title, String description, String detail, String image) {
        super();

        this.scholId = iD;
        this.title = title; //name of scholarship
        this.description = description; //short preview description of scholarship
        this.detail = detail;  //detailed description of scholarship
        this.image = image;
    }

    /*
    Getters
     */
    public int getScholD() {
        return scholId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDetail() {
        return detail;
    }

    public String getImage() {
        return image;
    }

    /*
    Setters
     */

    public void setScholiD(int iD) {
        this.scholId = iD;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Scholarship findOrCreateFromJson(Scholarship new_schol) {
        int scholId = new_schol.getScholD();

        Scholarship existingSchol = new Select().from(Scholarship.class).where("scholId = ?", scholId).executeSingle();

        if (existingSchol != null)
        {
            // found and return existing
            return existingSchol;
        }

        else
        {
            // create and return new user
            Scholarship schol = new_schol ;
            schol.save();
            return schol;
        }
    }
}

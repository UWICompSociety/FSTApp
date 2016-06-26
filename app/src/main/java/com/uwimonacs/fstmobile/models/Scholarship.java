package com.uwimonacs.fstmobile.models;

/**
 * Created by Jhanelle on 6/22/2016.
 */
public class Scholarship {

    private int iD;
    private String title, description, detail;

    public Scholarship(int iD, String title, String description, String detail) {
        this.iD = iD;
        this.title = title; //name of scholarship
        this.description = description; //short preview description of scholarship
        this.detail = detail;  //detailed description of scholarship
    }

    /*
    Getters
     */
    public int getiD() {
        return iD;
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

    /*
    Setters
     */

    public void setiD(int iD) {
        this.iD = iD;
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
}

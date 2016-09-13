package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * @author sultanofcardio
 */
@Table(name="Alert")
public class Alert extends Model {

    @SerializedName("id")
    @Column(name="alertId")
    int alertId;

    @SerializedName("title")
    @Column(name="title")
    String title;

    @SerializedName("description")
    @Column(name="description")
    String description;

    public Alert(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public static Alert findOrCreateFromJson(Alert newAlert) {
        int alertId = newAlert.getAlertId();
        Alert existingAlert =
                new Select().from(Alert.class).where("alertId = ?", alertId).executeSingle();
        if (existingAlert != null) {
            // found and return existing
            updateAlert(existingAlert,newAlert);
            return existingAlert;
        } else {
            // create and return new alert
            Alert alert = newAlert;
            alert.save();
            return alert;
        }
    }

    private static void updateAlert(Alert oldAlert,Alert newAlert) {
        oldAlert.setTitle(newAlert.getTitle());
        oldAlert.setDescription(newAlert.getDescription());
        oldAlert.save();
    }
}

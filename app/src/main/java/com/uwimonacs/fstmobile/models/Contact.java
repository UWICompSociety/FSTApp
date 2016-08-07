package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Matthew on 6/27/2016.
 */

@Table(name = "Contact")
public class Contact extends Model {


    @SerializedName("id")
    @Column(name = "contactId")
    private int contactId;

    @SerializedName("name")
    @Column(name = "name")
    private String name;

    @SerializedName("number")
    @Column(name = "number")
    private String number;

    @SerializedName("email")
    @Column(name = "email")
    private String email;

    @SerializedName("website")
    @Column(name="website")
    private String website;



    public Contact()
    {
        super();
    }

    public Contact(int contactId,String name,String number)
    {
        super();

        this.contactId = contactId;
        this.name = name;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static Contact findOrCreateFromJson(Contact new_contact) {
        int contactId = new_contact.getContactId();
        Contact existingContact =
                new Select().from(Contact.class).where("contactId = ?", contactId).executeSingle();
        if (existingContact != null) {
            // found and return existing
            return existingContact;
        } else {
            // create and return new user
            Contact contact = new_contact;
            contact.save();
            return contact;
        }
    }
}

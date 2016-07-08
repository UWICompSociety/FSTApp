package com.uwimonacs.fstmobile.sync;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.rest.RestContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 7/5/2016.
 */
public class ContactSync {

    String url;
    ArrayList<Contact> contacts = new ArrayList<>();
    public ContactSync(String url)
    {
        this.url = url;
    }

    public boolean syncContacts()
    {

        RestContact restContact = new RestContact(url);

        contacts = restContact.getContacts(); //gets the list of contacts from rest api

        if(contacts == null) //if there are no contacts
            return false;

        if(contacts.size() == 0) //if the contacts list is empty
            return false;


        for(int i=0;i<contacts.size();i++)
        {
            Contact contact = contacts.get(i);

            Contact.findOrCreateFromJson(contact); //saves contact to database
        }

        return true;

    }

    /*private void initalizeData()
    {
        contacts.add(new Contact(1,"Faculty Office","9236728"));
        contacts.add(new Contact(2,"Physics Main Ofiice","9782728"));
        contacts.add(new Contact(3,"Chemistry Main Office","8785428"));
        contacts.add(new Contact(4,"Life Sciences Main Office","7483407"));
        contacts.add(new Contact(5,"Engineering Main Office","9916563"));


    }*/

}

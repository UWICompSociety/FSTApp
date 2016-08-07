package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.rest.RestContact;

import java.util.ArrayList;

/**
 * Created by Matthew on 7/5/2016.
 */
public class ContactSync {
    private final String url;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactSync(String url)
    {
        this.url = url;
    }

    public boolean syncContacts() {
        final RestContact restContact = new RestContact(url);

        contacts = restContact.getContacts(); // gets the list of contacts from rest api

        if (contacts == null) // if there are no contacts
            return false;

        if (contacts.size() == 0) // if the contacts list is empty
            return false;

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < contacts.size(); i++) {
                final Contact contact = contacts.get(i);

                Contact.findOrCreateFromJson(contact); // saves contact to database
            }
                ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }
}

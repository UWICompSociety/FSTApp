package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ContactListAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.sync.ContactSync;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class ContactsActivity extends AppCompatActivity {
    private RecyclerView contactList;
    private List<Contact> contacts = new ArrayList<>();
    private ContactListAdapter contactListAdapter;
    private String contactsUrl = Constants.CONTACTS_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactList = (RecyclerView) findViewById(R.id.listContact);

        getContactsFromDatabase();   // get all contacts from phone database

        LinearLayoutManager llm = new LinearLayoutManager(this);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(llm);

        contactListAdapter = new ContactListAdapter(this, contacts);

        contactList.setAdapter(contactListAdapter);

        new LoadContactsTask(this).execute(""); // runs the contacts sync task
    }

    private void initalizeData()
    {
        contacts.add(new Contact(1, "Faculty Office", "+18769236728"));
        contacts.add(new Contact(2, "Physics Main Office", "+18769782728"));
        contacts.add(new Contact(3, "Chemistry Main Office", "+18768785428"));
        contacts.add(new Contact(4, "Life Sciences Main Office", "+18767483407"));
        contacts.add(new Contact(5, "Engineering Main Office", "+18769916563"));
    }

    private void getContactsFromDatabase()
    {
        contacts = new Select().all().from(Contact.class).execute();
    }

    private class LoadContactsTask extends AsyncTask<String,Integer,Boolean>
    {
        Context ctxt;

        public LoadContactsTask(Context ctxt)
        {
            this.ctxt = ctxt; //application context
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt, "Loading contacts..", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ContactSync contactSync = new ContactSync(contactsUrl);
            return contactSync.syncContacts();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) //if sync was successful
            {
                getContactsFromDatabase(); //get the freshly synced contacts from database
                Toast.makeText(ctxt, "Contacts Loaded successfully", Toast.LENGTH_SHORT).show();

                //update the card list to show new contacts
                contactListAdapter.updateContacts(contacts);
            } else {
                //failed to sync maybe no internet or some error with api
                Toast.makeText(ctxt, "Sync failed",Toast.LENGTH_SHORT).show();
            }
        }


    }

}

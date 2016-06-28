package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ContactListAdapter;
import com.uwimonacs.fstmobile.models.Contact;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {


    private RecyclerView contactList;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ContactListAdapter contactListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //functionality to go back to previous activity

        contactList = (RecyclerView) findViewById(R.id.listContact);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(llm); //sets the layout manager

        initalizeData();

        contactListAdapter = new ContactListAdapter(this,contacts);

        contactList.setAdapter(contactListAdapter); //sets the adapter




    }

    private void initalizeData()
    {
        contacts.add(new Contact("Faculty Office","923-6728"));
        contacts.add(new Contact("Physics Main Ofiice","978-2728"));
        contacts.add(new Contact("Chemistry Main Office","878-5428"));
        contacts.add(new Contact("Life Sciences Main Office","748-3407"));
        contacts.add(new Contact("Engineering Main Office","991-6563"));


    }

}

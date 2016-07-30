package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ContactListAdapter;
import com.uwimonacs.fstmobile.helper.Connect;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.sync.ContactSync;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class ContactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView contactList;
    private List<Contact> contacts = new ArrayList<>();
    private ContactListAdapter contactListAdapter;
    private String contactsUrl = Constants.CONTACTS_URL;
    private Connect connect;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        initViews();

        setUpToolBar();

        setUpSwipeRefresh();

        connect = new Connect(this);

        getContactsFromDatabase();   // get all contacts from phone database

        if(contacts.size()>0) //if there are new items present remove place holder image and text
        {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView(); //set up recycler view

        setUpProgressBar(); //set up progress bar

        new LoadContactsTask(this).execute(""); // runs the contacts sync task
    }

    private void initViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contactList = (RecyclerView) findViewById(R.id.listContact);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView)findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void setUpToolBar()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView()
    {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(llm);
        contactListAdapter = new ContactListAdapter(this, contacts);
        contactList.setAdapter(contactListAdapter);
    }

    private void setUpSwipeRefresh()
    {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar()
    {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }



    private void getContactsFromDatabase()
    {
        contacts = new Select().all().from(Contact.class).execute();
    }

    private boolean isConnected() {
        return connect.isConnected();
    }

    private boolean hasInternet()
    {
        boolean hasInternet = false;
        try {
            hasInternet = connect.haveInternetConnectivity();
        } catch(Exception e)
        {
            hasInternet = false;
        }

        return  hasInternet;

    }

    @Override
    public void onRefresh() {
        new LoadContactsTask(this).execute(""); // runs the contacts sync task
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return true;
        }
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
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (contacts.size() == 0) { // check if any news are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ContactSync contactSync = new ContactSync(contactsUrl);
            if(!isConnected())  //if there is no internet connection
            {
                return false;
            }

            if(!hasInternet()) //if there is no internet
            {
                return false;
            }

            return contactSync.syncContacts();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if(result) //if sync was successful
            {
                getContactsFromDatabase(); //get the freshly synced contacts from database

                //update the card list to show new contacts
                contactListAdapter.updateContacts(contacts);
            } else {
                //failed to sync maybe no internet or some error with api
                if(contacts.size() == 0)
                {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }


    }

}

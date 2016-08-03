package com.uwimonacs.fstmobile.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ContactListAdapter;
import com.uwimonacs.fstmobile.util.ConnectUtils;
import com.uwimonacs.fstmobile.util.Constants;
import com.uwimonacs.fstmobile.models.Contact;
import com.uwimonacs.fstmobile.sync.ContactSync;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class ContactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{
    private RecyclerView contactList;
    private List<Contact> contacts = new ArrayList<>();
    private ContactListAdapter contactListAdapter;
    private String contactsUrl = Constants.CONTACTS_URL;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;
    private SearchView searchView;
    private CardView root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        initViews();

        setupReveal();

        setUpToolBar();

        setUpSwipeRefresh();

        getContactsFromDatabase();   // get all contacts from phone database

        if (contacts.size() > 0) {//if there are new items present remove place holder image and text
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView(); //set up recycler view

        setUpProgressBar(); //set up progress bar

        new LoadContactsTask(this).execute(); // runs the contacts sync task
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contactList = (RecyclerView) findViewById(R.id.listContact);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView)findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        root = (CardView) findViewById(R.id.root);
    }

    private void setupReveal(){
        if(Build.VERSION.SDK_INT >= 21) {
            root.setVisibility(View.INVISIBLE);
            ViewTreeObserver observer = root.getViewTreeObserver();
            if(observer.isAlive()){
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularReveal();
                        if(Build.VERSION.SDK_INT < 16)
                            root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        else
                            root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    @SuppressLint("NewApi")
    private void circularReveal(){
        int cx = root.getWidth() / 2;
        int cy = root.getHeight() / 2;
        float finalRadius = (float) Math.max(root.getWidth(), root.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(root, cx, cy, 0, finalRadius);
        anim.setDuration(1000);
        root.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_contacts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        contactListAdapter = new ContactListAdapter(this, contacts);
        contactList.setAdapter(contactListAdapter);
    }

    private void setUpSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar() {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }

    private void getContactsFromDatabase() {
        contacts = new Select().all().from(Contact.class).execute();
    }

    private boolean isConnected() {
        return ConnectUtils.isConnected(this);
    }

    private static boolean hasInternet() {
        boolean hasInternet;

        try {
            hasInternet = ConnectUtils.haveInternetConnectivity();
        } catch(Exception e) {
            hasInternet = false;
        }

        return hasInternet;
    }

    @Override
    public void onRefresh() {
        new LoadContactsTask(this).execute(); // runs the contacts sync task
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Enter a name"); //sets the hint text

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false); //clears text from search view
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Contact> filteredModelList = filter(contacts, query);
        contactListAdapter.animateTo(filteredModelList,query);
        contactList.scrollToPosition(0);
        return true;
    }

    /**
     * takes a query and returns a list of contacts that match the query
     * @param models
     * @param query
     * @return
     */
    private List<Contact> filter(List<Contact> models, String query) {
        query = query.toLowerCase();
        final List<Contact> filteredModelList = new ArrayList<>();

        for (Contact model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query.toLowerCase())) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private class LoadContactsTask extends AsyncTask<Void,Void,Boolean> {
        final Context ctxt;

        public LoadContactsTask(Context ctxt)
        {
            this.ctxt = ctxt; // application context
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
        protected Boolean doInBackground(Void... params) {
            final ContactSync contactSync = new ContactSync(contactsUrl);

            if (!isConnected()) { // if there is no internet connection
                return false;
            }

            if (!hasInternet()) { // if there is no internet
                return false;
            }

            return contactSync.syncContacts();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if (result) { //if sync was successful
                getContactsFromDatabase(); // get the freshly synced contacts from database

                // update the card list to show new contacts
                contactListAdapter.updateContacts(contacts);
            } else {
                // failed to sync maybe no internet or some error with api
                if (contacts.size() == 0) {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}

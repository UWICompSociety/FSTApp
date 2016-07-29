package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ExpandableScholarshipAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.sync.ScholarshipSync;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class ExpandableScholarshipActivity extends AppCompatActivity {

    private List<Scholarship> schols;
    private String url = Constants.SCHOL_URL;
    private List<Card> cards;
    private ExpandableScholarshipAdapter mCardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_scholarship);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Creates list of card objects
        initMaterialCards();
        //getScholsFromDatabase();

        CardRecyclerView mRecyclerView = (CardRecyclerView) findViewById(R.id.card_recyclerview);

        mCardArrayAdapter = new ExpandableScholarshipAdapter(this, cards);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }

        new LoadScholsTask(this).execute("");
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

    /**
     * Gets the list of scholarships from the database
     */
    private void getScholsFromDatabase() {
        schols = new Select().all().from(Scholarship.class).execute();
    }

    /**
     * Initializes Cards
     */
    private void initMaterialCards() {
        cards = new ArrayList<>();
        //init();
        getScholsFromDatabase();

        for (int i = 0; i < schols.size();i++) {
            final int pos = i;

            //Create built in material design card from CardsLib library
            MaterialLargeImageCard card = new MaterialLargeImageCard(this);

            //Set relevant data for the card
            //card.setUrlCardThumbnail(schols.get(i).getImage());
            card.setDrawableIdCardThumbnail(R.drawable.ic_school_black_24dp);
            //card.setDrawableIdCardThumbnail(R.drawable.scholarship);
            card.setTitle(schols.get(i).getTitle());
            card.setSubTitle(schols.get(i).getDescription());

            //Create onClickListener for the card
            //Launches activity to show the details of the scholarship
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(view.getContext(), ScholarshipDetailsActivity.class);
                    intent.putExtra("scholName", schols.get(pos).getTitle());
                    intent.putExtra("scholDetails", schols.get(pos).getDetail());
                    intent.putExtra("scholImage", schols.get(pos).getImage());
                    view.getContext().startActivity(intent);
                }
            });

            //Build and add card to array of cards
            card.build();
            cards.add(card);
        }
    }

    /**
     * Loads and updates list of scholarships
     */
    private class LoadScholsTask extends AsyncTask<String,Integer,Boolean> {
        Context ctxt;

        public LoadScholsTask(Context ctxt) {
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt, "Loading Scholarships and Bursaries...",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ScholarshipSync scholSync = new ScholarshipSync(url);
            boolean result = scholSync.syncSchol();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                initMaterialCards();
                Toast.makeText(ctxt, "Scholarships loaded successfully", Toast.LENGTH_SHORT).show();
                mCardArrayAdapter.updateSchols(cards);
            }else {
                Toast.makeText(ctxt, "Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
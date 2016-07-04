package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Scholarship;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class ExpandableScholarshipActivity extends AppCompatActivity {

    private List<Scholarship> schols;
    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_scholarship);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //enable back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Creates list of card objects
        initMaterialCards();

        CardRecyclerView mRecyclerView = (CardRecyclerView) findViewById(R.id.card_recyclerview);

        //Initializes adapter
        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(this, cards);

        //Initializes and sets layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }
    }

    /**
     * Initializes test data
     */
    private void init() {
        schols = new ArrayList<>();
        schols.add(new Scholarship(1, "AFUWI Scholarships", "Description", "detail-1"));
        schols.add(new Scholarship(2, "Ambassador Glen A. Holden Bursary", "Description", "detail-2"));
        schols.add(new Scholarship(3, "Digicel Scholarships", "Description", "detail-3"));
        schols.add(new Scholarship(4, "Douane Henry Memorial Bursary (The)", "Description", "detail-4"));
        schols.add(new Scholarship(5, "Jamaica Government Exhibition", "Description", "detail-5"));
        schols.add(new Scholarship(6, "Joe Pereira Scholarship (The)", "Description", "detail-6"));
        schols.add(new Scholarship(7, "Principal's Scholarship For Excellence (The)Digicel Scholarships", "Description", "detail-7"));
        schols.add(new Scholarship(8, "UWI Visa Card Scholarship", "Description", "detail-8"));
    }

    /**
     * Initializes Cards using test data
     */
    private void initMaterialCards() {
        cards = new ArrayList<>();
        init();

        for (int i = 0; i < schols.size();i++) {
            final int pos = i;

            //Create built in material design card from CardsLib library
            MaterialLargeImageCard card = new MaterialLargeImageCard(this);

            //Set relevant data for the card
            card.setDrawableIdCardThumbnail(R.drawable.scholarship);
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
                    view.getContext().startActivity(intent);
                }
            });

            //Build and add card to array of cards
            card.build();
            cards.add(card);
        }
    }

}
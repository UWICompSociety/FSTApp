package com.uwimonacs.fstmobile.adapters;

import android.content.Context;

import com.uwimonacs.fstmobile.models.FAQ;
import com.uwimonacs.fstmobile.models.Scholarship;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;

/**
 * Created by Jhanelle on 7/17/2016.
 */
public class ExpandableScholarshipAdapter extends CardArrayRecyclerViewAdapter {

    private List<Card> cards;

    /**
     * Creates basic adapter for material card
     * @param ctxt
     * @param cards list of material cards
     */
    public ExpandableScholarshipAdapter (Context ctxt, List<Card> cards) {
        super(ctxt, cards);
        this.cards = cards;
    }

    /**
     * Updates the list of scholarships
     * @param newCards updated list of scholarship cards
     */
    public void updateSchols(List<Card> newCards)
    {
        this.cards = new ArrayList<>(newCards);
        notifyDataSetChanged();
    }
}

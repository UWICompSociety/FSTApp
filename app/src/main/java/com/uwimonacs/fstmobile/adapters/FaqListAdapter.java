package com.uwimonacs.fstmobile.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.FAQ;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brand_000 on 21/06/2016.
 * FAQList Adapter
 */
public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.FaqHolder> {
    private String filter = "";
    private List<FAQ> faqs;

    public static class FaqHolder extends RecyclerView.ViewHolder {
        final CardView cv;
        final TextView question;
        final TextView answer;

        FaqHolder(View v) {
            super(v);

            cv = (CardView) v.findViewById(R.id.cv);
            question = (TextView) v.findViewById(R.id.question);
            answer = (TextView) v.findViewById(R.id.answer);
        }
    }

    public FaqListAdapter(List<FAQ> faqs){
        this.faqs = faqs;
    }

    @Override
    public FaqHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.faq_card, viewGroup, false);

        return new FaqHolder(v);
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    @Override
    public void onBindViewHolder(FaqHolder fHolder, int i) {
        fHolder.question.setText(faqs.get(i).getQuestion());
        fHolder.answer.setText(faqs.get(i).getAnswer());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateFAQs(List<FAQ> newFags)
    {
        this.faqs = new ArrayList<>(newFags);
        notifyDataSetChanged();
    }

    public void animateTo(List<FAQ> models,String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.filter = filter;
    }

    private void applyAndAnimateRemovals(List<FAQ> newModels) {
        for (int i = faqs.size() - 1; i >= 0; i--) {
            final FAQ model = faqs.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<FAQ> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final FAQ model = newModels.get(i);
            if (!faqs.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<FAQ> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final FAQ model = newModels.get(toPosition);
            final int fromPosition = faqs.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public FAQ removeItem(int position) {
        final FAQ model = faqs.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, FAQ model) {
        faqs.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final FAQ model = faqs.remove(fromPosition);
        faqs.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}

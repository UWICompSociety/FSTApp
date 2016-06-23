package com.uwimonacs.fstmobile.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.FAQ;

import java.util.List;

/**
 * Created by Brand_000 on 21/06/2016.
 * FAQList Adapter
 */
public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.FaqHolder> {

    List<FAQ> faqs;

    public class FaqHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView question;
        TextView answer;

        FaqHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            question = (TextView)itemView.findViewById(R.id.question);
            answer = (TextView)itemView.findViewById(R.id.answer);
        }
    }

    public FaqListAdapter(List<FAQ> faqs){
        this.faqs = faqs;
    }

    @Override
    public FaqHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.faq_card, viewGroup, false);
        FaqHolder fh = new FaqHolder(v);
        return fh;
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
}

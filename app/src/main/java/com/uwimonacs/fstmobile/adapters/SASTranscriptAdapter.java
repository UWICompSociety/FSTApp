package com.uwimonacs.fstmobile.adapters;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Transcript;
import com.uwimonacs.fstmobile.models.TranscriptViewHolder;

import java.util.List;

public class SASTranscriptAdapter extends RecyclerView.Adapter<TranscriptViewHolder> {
    private List<Transcript.Term> terms;

    public SASTranscriptAdapter() {
        terms = MyApplication.getSasConfig().student.getTranscript().getTerms();
    }

    @Override
    public TranscriptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TranscriptViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_sastranscript_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TranscriptViewHolder holder, int position) {
        final Transcript.Term term = terms.get(position);
        holder.termName.setText(term.getName());
        final List<Transcript.Term.Course> courses = term.getCourses();
        holder.linearLayout.removeAllViews();
        for (final Transcript.Term.Course course: courses) {
            final TextView subjectView = new TextView(MyApplication.getContext());

            final ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT * 3);

            subjectView.setLayoutParams(params);
            subjectView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String subject = "Subject: " + course.getSubject() + " " + course.getCode();
            subjectView.setText(subject);
            if (Build.VERSION.SDK_INT < 23) {
                subjectView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black));
            } else {
                subjectView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black, null));
            }
            holder.linearLayout.addView(subjectView);

            final TextView titleView = new TextView(MyApplication.getContext());
            titleView.setLayoutParams(params);
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String title = "Title: " + course.getTitle();
            titleView.setText(title);
            if (Build.VERSION.SDK_INT < 23) {
                titleView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black));
            } else {
                titleView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black, null));
            }
            holder.linearLayout.addView(titleView);

            final TextView scoreView = new TextView(MyApplication.getContext());
            scoreView.setLayoutParams(params);
            scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String score = "Score: " + course.getScore();
            scoreView.setText(score);
            if (Build.VERSION.SDK_INT < 23) {
                scoreView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black));
            } else {
                scoreView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black, null));
            }
            holder.linearLayout.addView(scoreView);

            final TextView gradeView = new TextView(MyApplication.getContext());
            gradeView.setLayoutParams(params);
            gradeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String grade = "Grade: " + course.getGrade();
            gradeView.setText(grade);
            if (Build.VERSION.SDK_INT < 23) {
                gradeView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black));
            } else {
                gradeView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black, null));
            }
            holder.linearLayout.addView(gradeView);

            final TextView creditsView = new TextView(MyApplication.getContext());
            creditsView.setLayoutParams(params);
            creditsView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            final String creditHours = "Credit Hours: " + course.getCreditHours();
            creditsView.setText(creditHours);
            if (Build.VERSION.SDK_INT < 23) {
                creditsView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black));
            } else {
                creditsView.setTextColor(MyApplication.getContext().getResources().getColor(android.R.color.black, null));
            }
            holder.linearLayout.addView(creditsView);

            /*
            * Set a divider between courses
            * */
            final View v = View.inflate(MyApplication.getContext(), R.layout.divider, null);
            if (Build.VERSION.SDK_INT < 23) {
                v.setBackgroundColor(ContextCompat.getColor(MyApplication.getContext(),
                        android.R.color.black));
            } else {
                v.setBackgroundColor(MyApplication.getContext().getColor(android.R.color.black));
            }
            holder.linearLayout.addView(v);
        }
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }
}

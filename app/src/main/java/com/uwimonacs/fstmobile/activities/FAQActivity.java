package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.FaqListAdapter;
import com.uwimonacs.fstmobile.models.FAQ;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    private List<FAQ> faqs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAQs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView faqList = (RecyclerView)findViewById(R.id.faqlist);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        faqList.setLayoutManager(llm);

        initialize();

        FaqListAdapter adapter = new FaqListAdapter(faqs);
        faqList.setAdapter(adapter);
    }

    //Add FAQs to List
    private void initialize()
    {
        faqs.add(new FAQ(1,"How Many Credits Do I Need To Graduate?", "A Total of 93 Credits"));
        faqs.add(new FAQ(2,"Where is FST Dean's Office?", "Along The Spine"));
        faqs.add(new FAQ(3,"When does Copy-Works Open?", "8:30 AM Mondays - Friday"));
        faqs.add(new FAQ(4,"Where Can I Buy Books?", "At The Book Store"));
        faqs.add(new FAQ(5,"Where Can I Find The Computing Society?", "Computing Lecture Theatre"));
        faqs.add(new FAQ(6,"How Many First Year Computing Courses Are There?", "There are 5 Of Them"));
    }
}

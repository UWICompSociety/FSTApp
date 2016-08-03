package com.uwimonacs.fstmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.mukesh.MarkdownView;
import com.uwimonacs.fstmobile.R;

//import us.feras.mdv.MarkdownView;

public class ScholarshipDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets data for the relevant card details
        final Bundle extras = getIntent().getExtras();
        final String scholName = extras.getString("scholName");
        final String scholDetails = extras.getString("scholDetails");

        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(scholName);

        setContentView(R.layout.activity_scholarship_details);

        MarkdownView markdownView = (MarkdownView) findViewById(R.id.markdown_view);
        markdownView.setMarkDownText(scholDetails);


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
}

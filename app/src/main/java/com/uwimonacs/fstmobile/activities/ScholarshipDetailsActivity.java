package com.uwimonacs.fstmobile.activities;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;

import java.io.InputStream;
import java.net.URL;

public class ScholarshipDetailsActivity extends AppCompatActivity {

    private Bundle extras;
    private String scholName;
    private String scholDetails;
    private String imageURL;
    private ImageView  scholPic;
    private TextView scholNameTextView;
    private TextView scholDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Gets data for the relevant card details
        extras = getIntent().getExtras();
        scholName = extras.getString("scholName");
        scholDetails = extras.getString("scholDetails");
        imageURL = extras.getString("scholImage");

        setContentView(R.layout.activity_scholarship_details);

        scholPic = (ImageView) findViewById(R.id.photo);
        scholNameTextView = (TextView) findViewById(R.id.name);
        scholDetailsTextView = (TextView) findViewById(R.id.detail);

        //Sets details of scholarship to be displayed
        //scholPic.setImageResource(R.drawable.scholarship);
        scholPic.setImageResource(R.drawable.ic_school_black_24dp);
        /*
        try {
            scholPic.setImageDrawable(LoadImageFromWebOperations(imageURL));
        } catch (Exception e) {
            scholPic.setImageResource(R.drawable.ic_school_black_24dp);
        }*/
        scholNameTextView.setText(scholName);
        scholDetailsTextView.setText(Html.fromHtml(scholDetails).toString().trim());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return true;
        }
    }

    /**
     * Returns Drawable object of content at a URL
     * @param url URL location of image
     * @return Drawable object
     * @throws Exception
     */
    public static Drawable LoadImageFromWebOperations(String url) throws Exception {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
    }
}

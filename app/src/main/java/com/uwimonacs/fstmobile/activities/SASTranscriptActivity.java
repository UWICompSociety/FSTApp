package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.SASTranscriptAdapter;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Transcript;

public class SASTranscriptActivity extends AppCompatActivity {
    private SASConfig sasConfig;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sasConfig = MyApplication.getSasConfig();
        setContentView(R.layout.activity_sastranscript);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        Transcript transcript = sasConfig.student.getTranscript();
//        TextView degreeType = (TextView) findViewById(R.id.degree_type),
//                program = (TextView) findViewById(R.id.program),
//                faculty = (TextView) findViewById(R.id.faculty),
//                major = (TextView) findViewById(R.id.major),
//                minor = (TextView) findViewById(R.id.minor);
//
//        String sDegree = "Degree type: " + transcript.getDegree(),
//                sProgram = "Program: " + transcript.getProgram(),
//                sFaculty = "Faculty: " + transcript.getFaculty(),
//                sMajor = "Major: " + transcript.getMajor(),
//                sMinor = "Minor: " + transcript.getMinor();
//
//        degreeType.setText(sDegree);
//        program.setText(sProgram);
//        faculty.setText(sFaculty);
//        major.setText(sMajor);
//        minor.setText(sMinor);

        RecyclerView institutionCredit = (RecyclerView) findViewById(R.id.institution_credit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        institutionCredit.setLayoutManager(layoutManager);
        institutionCredit.setAdapter(new SASTranscriptAdapter());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                return true;
        }
    }
}

package com.uwimonacs.fstmobile.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.ScheduleListAdapter;
import com.uwimonacs.fstmobile.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private ScheduleListAdapter scheduleListAdapter;
    private List<Schedule> schedules = new ArrayList<>();
    private SharedPreferences mSharedPrefs;
    private TextView mPromptText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.schedulelist);
        mFloatingActionButton  = (FloatingActionButton) findViewById(R.id.fab);
        scheduleListAdapter = new ScheduleListAdapter(schedules);
        mPromptText  = (TextView) findViewById(R.id.promptText);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(scheduleListAdapter);

        mSharedPrefs = getPreferences(Context.MODE_PRIVATE);
        String name = mSharedPrefs.getString("KEY_NAME","");
        if(name!="")
        {
            schedules = Schedule.getSchedule(name);
            scheduleListAdapter.updateSchedules(schedules);
        }else{
            showDialog();
        }

        if(schedules.size()==0)
        {
            mPromptText.setVisibility(View.VISIBLE);
        }else{
            mPromptText.setVisibility(View.GONE);
        }

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_add_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText topicInput = (EditText) promptsView
                .findViewById(R.id.editTextName);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(schedules.size()==0)
                                {
                                    mPromptText.setVisibility(View.VISIBLE);
                                }else{
                                    mPromptText.setVisibility(View.GONE);
                                }
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String name = topicInput.getText().toString(); //gets topic of discussion
                        if(!TextUtils.isEmpty(name))
                        {
                            SharedPreferences.Editor editor = mSharedPrefs.edit();
                            editor.putString("KEY_NAME",name);
                            editor.commit();
                            schedules = Schedule.getSchedule(name);
                            scheduleListAdapter.updateSchedules(schedules);
                            dialog.dismiss();

                            if(schedules.size()==0)
                            {
                                mPromptText.setVisibility(View.VISIBLE);
                            }else{
                                mPromptText.setVisibility(View.GONE);
                            }
                        }else{
                            Toast.makeText(view.getContext(),"Enter name to view schedule",Toast.LENGTH_SHORT).show();
                        }


                        //Dismiss once everything is OK.
                    }
                });
            }
        });

        // show it
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


}

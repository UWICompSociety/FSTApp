package com.uwimonacs.fstmobile.models;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.PlayerActivity;

/**
 * Created by sultanofcardio on 6/21/16.
 */
public class CardOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        TextView title = (TextView) view.findViewById(R.id.video_title),
                description =  (TextView) view.findViewById(R.id.video_description),
                id = (TextView) view.findViewById(R.id.video_id);
        Intent intent = new Intent(view.getContext(), PlayerActivity.class);
        intent.putExtra("VIDEO_ID", id.getText().toString());
        intent.putExtra("VIDEO_TITLE", title.getText().toString());
        intent.putExtra("VIDEO_DESC", description.getText().toString());
        view.getContext().startActivity(intent);
    }
}
package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Alert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sultanofcardio
 */
public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.AlertViewHolder> {

    private List<Alert> alerts;
    private Context context;

    public AlertListAdapter(Context context, List<Alert> alerts) {
        this.context = context;

        //  sample data
        alerts.add(new Alert("No school tomorrow", "",
                "Attention! There will be no school tomorrow as a result of an impending tropical storm"));

        this.alerts = new ArrayList<>(alerts);
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_alert_item, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        Alert alert = alerts.get(holder.getAdapterPosition());

        String title = alert.getTitle();
        String desc = alert.getDescription();

        holder.alertName.setText(title);
        holder.alertDesc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public void updateAlertList(List<Alert> newAlerts) {
        this.alerts = newAlerts;
        notifyDataSetChanged();
    }

    public class AlertViewHolder extends RecyclerView.ViewHolder{

        public TextView alertName;
        public TextView alertDesc;

        public AlertViewHolder(final View itemView) {
            super(itemView);

            alertName = (TextView) itemView.findViewById(R.id.alertTitle);
            alertDesc = (TextView) itemView.findViewById(R.id.alertDesc);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, alertName.getText().toString()
                            + "\n\n" + alertDesc.getText().toString());
                    shareIntent.setType("text/plain");
                    itemView.getContext().startActivity(shareIntent);

                    return true;
                }
            });
        }
    }
}

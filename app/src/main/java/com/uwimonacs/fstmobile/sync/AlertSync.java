package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.Alert;
import com.uwimonacs.fstmobile.rest.RestAlert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sultanofcardio
 */
public class AlertSync {

    private final String url;
    private ArrayList<Alert> alerts = new ArrayList<>();

    public AlertSync(String url)
    {
        this.url = url;
    }

    public boolean syncAlerts() {

        final RestAlert restAlert = new RestAlert(url);

        alerts = restAlert.getAlerts(); // gets the list of alerts from rest api

        if (alerts == null) // if there are no alerts
            return false;

        if (alerts.size() == 0) // if the alerts list is empty
            return false;

        ActiveAndroid.beginTransaction();
        try {
            deleteStaleData();
            for (int i = 0; i < alerts.size(); i++) {
                final Alert alert = alerts.get(i);

                Alert.findOrCreateFromJson(alert); // saves alert to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }

    private void deleteStaleData()
    {

        List<Alert> staleAlerts = new Select().all().from(Alert.class).execute();
        for(int i=0;i<staleAlerts.size();i++)
        {
            if(!doesAlertExistInJson(staleAlerts.get(i)))
            {
                Alert.delete(Alert.class,staleAlerts.get(i).getId());
            }
        }
    }

    private boolean doesAlertExistInJson(Alert alert)
    {
        return alerts.contains(alert);
    }
}

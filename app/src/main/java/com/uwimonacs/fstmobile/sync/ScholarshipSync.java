package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.rest.RestScholarship;

import java.util.ArrayList;

public class ScholarshipSync {
    private String url;
    private ArrayList<Scholarship> schols = new ArrayList<>();

    public ScholarshipSync(String url)
    {
        this.url = url;
    }

    public boolean syncSchol() {
        final RestScholarship restSchol = new RestScholarship(url);

        schols = restSchol.getSchols();

        if (schols == null)
            return false;

        if (schols.size() == 0)
            return false;

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < schols.size(); i++) {
                Scholarship schol = schols.get(i);
                Scholarship.findOrCreateFromJson(schol);
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }
}

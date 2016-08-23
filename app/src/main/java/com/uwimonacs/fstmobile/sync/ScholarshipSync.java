package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.Place;
import com.uwimonacs.fstmobile.models.Scholarship;
import com.uwimonacs.fstmobile.rest.RestScholarship;

import java.util.ArrayList;
import java.util.List;

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
            deleteStaleData();
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

    private void deleteStaleData()
    {

        List<Scholarship> stale_schols = new Select().all().from(Scholarship.class).execute();
        for(int i=0;i<stale_schols.size();i++)
        {
            if(!doesScholarshipExistInJson(stale_schols.get(i)))
            {
                Scholarship.delete(Scholarship.class,stale_schols.get(i).getId());
            }
        }
    }

    private boolean doesScholarshipExistInJson(Scholarship schol)
    {
        return schols.contains(schol);
    }
}

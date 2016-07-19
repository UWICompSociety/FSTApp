package com.uwimonacs.fstmobile.sync;

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

    public boolean syncSchol()
    {
        RestScholarship restSchol = new RestScholarship(url);

        schols = restSchol.getSchols();

        if(schols == null){ return false; }
        if(schols.size() == 0){ return false; }

        for(int i = 0; i < schols.size(); i++)
        {
            Scholarship schol = schols.get(i);
            Scholarship.findOrCreateFromJson(schol);
        }

        return true;
    }
}

package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.FAQ;
import com.uwimonacs.fstmobile.models.ImageItem;
import com.uwimonacs.fstmobile.rest.RestFAQ;

import java.util.ArrayList;
import java.util.List;

public class FAQSync {
    private final String url;
    private ArrayList<FAQ> faqs = new ArrayList<>();

    public FAQSync(String url)
    {
        this.url = url;
    }

    public boolean syncFAQs() {
        final RestFAQ restFaq = new RestFAQ(url);

        faqs = restFaq.getFAQs();

        if (faqs == null)
            return false;

        if (faqs.size() == 0)
            return false;

        ActiveAndroid.beginTransaction();
        try {
            deleteStaleData();
            for (int i = 0; i < faqs.size(); i++) {
                FAQ faq = faqs.get(i);
                FAQ.findOrCreateFromJson(faq);
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }

    private void deleteStaleData()
    {

        List<FAQ> stale_faqs = new Select().all().from(FAQ.class).execute();
        for(int i=0;i<stale_faqs.size();i++)
        {
            if(!doesFAQExistInJson(stale_faqs.get(i)))
            {
                FAQ.delete(FAQ.class,stale_faqs.get(i).getId());
            }
        }
    }

    private boolean doesFAQExistInJson(FAQ faq)
    {
        return faqs.contains(faq);
    }


}

package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.uwimonacs.fstmobile.models.FAQ;
import com.uwimonacs.fstmobile.rest.RestFAQ;

import java.util.ArrayList;

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
}

package com.uwimonacs.fstmobile.sync;

import com.uwimonacs.fstmobile.models.FAQ;
import com.uwimonacs.fstmobile.rest.RestFAQ;

import java.util.ArrayList;

public class FAQSync {

    String url;
    ArrayList<FAQ> faqs = new ArrayList<>();

    public FAQSync(String url)
    {
        this.url = url;
    }

    public boolean syncFAQs()
    {
        RestFAQ restFaq = new RestFAQ(url);

        faqs = restFaq.getFAQs();

        if(faqs == null){ return false; }
        if(faqs.size() == 0){ return false; }

        for(int i = 0; i < faqs.size(); i++)
        {
            FAQ faq = faqs.get(i);
            FAQ.findOrCreateFromJson(faq);
        }

        return true;
    }
}

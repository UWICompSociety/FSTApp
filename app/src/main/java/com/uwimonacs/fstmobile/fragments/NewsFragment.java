package com.uwimonacs.fstmobile.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.NewsListAdapter;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.News;
import com.uwimonacs.fstmobile.sync.NewsSync;

import java.util.List;

/**
 * Created by Matthew on 6/20/2016.
 */
public class NewsFragment extends Fragment {

    private View view;
    private NewsListAdapter newsListAdapter;
    private RecyclerView newsListView;
    private List<News> newsItems;
    private String newsUrl  = Constants.NEWS_URL;


    public NewsFragment()
    {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_news,container,false); //inflates the layout for the view

        newsListView = (RecyclerView)view.findViewById(R.id.listNews);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());

        newsListView.setLayoutManager(llm);

        newsListView.setHasFixedSize(true);

        getNewsFromDatabase();

        if(newsItems.size()<=0)
            intializeExampleData();

        Toast.makeText(this.getActivity(),newsItems.size()+"",Toast.LENGTH_SHORT).show();

        newsListAdapter = new NewsListAdapter(view.getContext(),newsItems);

        newsListView.setAdapter(newsListAdapter);

        new LoadNewsTask(view.getContext()).execute("");

        return view;
    }


    private void getNewsFromDatabase()
    {
        newsItems = new Select().all().from(News.class).execute();
    }

    private void intializeExampleData()
    {

        newsItems.add(new News(1,R.drawable.coffee_cake,"No Classes Tommorow!","The sccholboard has advised that there will be no class tommorow due to..","Detail1"));
        newsItems.add(new News(2,R.drawable.donut_holes,"New Buildings","New buildings have been constructed at science and tech with the departments benefiting from this being..","Detail2"));
        newsItems.add(new News(3,R.drawable.muffins,"Deadline For Summer School","Deadline fo summer school registration is almost upon us ,all students who wish to..","Detail3"));


    }


    private class LoadNewsTask extends AsyncTask<String,Integer,Boolean>
    {
        Context ctxt;

        public LoadNewsTask(Context ctxt)
        {
            this.ctxt = ctxt;
        }
        @Override
        protected void onPreExecute() {

            Toast.makeText(ctxt,"Loading News..",Toast.LENGTH_SHORT).show();
        }



        @Override
        protected Boolean doInBackground(String... params) {
            NewsSync newsSync = new NewsSync(newsUrl);
            boolean result = newsSync.syncNews();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                getNewsFromDatabase();
                newsListAdapter.updateNews(newsItems);
                Toast.makeText(ctxt,"Successful",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ctxt,"Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
}

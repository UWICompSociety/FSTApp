package com.uwimonacs.fstmobile.fragments;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.NewsListAdapter;
import com.uwimonacs.fstmobile.helper.Connect;
import com.uwimonacs.fstmobile.helper.Constants;
import com.uwimonacs.fstmobile.models.News;
import com.uwimonacs.fstmobile.sync.NewsSync;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private NewsListAdapter newsListAdapter;
    private RecyclerView newsListView;
    private List<News> newsItems;
    private String newsUrl  = Constants.NEWS_URL;
    private LoadNewsTask loadNewsTask = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    Connect connect;

    public NewsFragment()
    {
        /* required empty constructor */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_news, container, false);

        newsListView = (RecyclerView)view.findViewById(R.id.listNews);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());

        newsListView.setLayoutManager(llm);

        newsListView.setHasFixedSize(true);

        connect = new Connect(this.getActivity());

        getNewsFromDatabase();

       // if(newsItems.size()<=0)
         //   intializeExampleData();

        Toast.makeText(this.getActivity(),newsItems.size()+"",Toast.LENGTH_SHORT).show();

        newsListAdapter = new NewsListAdapter(view.getContext(),newsItems);

        newsListView.setAdapter(newsListAdapter);

        new LoadNewsTask(this.getActivity()).execute("");
        //loadNewsTask.execute("");

        return view;
    }


    private void getNewsFromDatabase()
    {
        newsItems = new Select().all().from(News.class).execute();
    }

    private void intializeExampleData()
    {
        newsItems.add(new News(1, R.drawable.coffee_cake, "No Classes Tomorrow!",
                "The school board has advised that there will be no class tomorrow due to..",
                "Detail1"));

        newsItems.add(new News(2, R.drawable.donut_holes, "New Buildings",
                "New buildings have been constructed at science and tech with the departments "
                        + "benefiting from this being..",
                "Detail2"));

        newsItems.add(new News(3, R.drawable.muffins, "Deadline For Summer School",
                "Deadline fo summer school registration is almost upon us, all students who wish "
                        + "to..",
                "Detail3"));
    }

    private boolean isConnected() {
        return connect.isConnected();
    }

    private boolean hasInternet()
    {
        boolean hasInternet = false;
        try {
            hasInternet = connect.haveInternetConnectivity();
        } catch(Exception e)
        {
            hasInternet = false;
        }

        return  hasInternet;

    }

    @Override
    public void onRefresh() {
        new LoadNewsTask(getActivity()).execute("");
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

            if(!isConnected()) {
                return false;
            }

            if(!hasInternet())
            {
                return false;
            }

            return newsSync.syncNews();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            if(result)
            {
                getNewsFromDatabase();
                newsListAdapter.updateNews(newsItems);
                Toast.makeText(ctxt, "Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctxt, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

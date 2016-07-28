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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private Connect connect;
    private ImageView img_placeholder;
    private TextView tv_placeholder;
    private ProgressBar progressBar;

    public NewsFragment()
    {
        /* required empty constructor */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_news,container,false); //inflates the layout for the view


        initViews(); //initialize the views

        setUpSwipeRefresh(); //set up swipe refresh


        connect = new Connect(this.getActivity());

        getNewsFromDatabase(); //gets news items from the database

        if(newsItems.size()>0) //if there are new items present remove place holder image and text
        {
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);
        }

        setUpRecyclerView(); //set up recycler view

        setUpProgressBar(); //set up progress bar


        new LoadNewsTask(this.getActivity()).execute(""); //refresh the news items from internet


        return view;
    }




    private void initViews()
    {
        newsListView = (RecyclerView)view.findViewById(R.id.listNews);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        tv_placeholder = (TextView)view.findViewById(R.id.txt_notpresent);
        img_placeholder = (ImageView) view.findViewById(R.id.img_placeholder);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
    }
    private void setUpRecyclerView()
    {
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());

        newsListView.setLayoutManager(llm);

        newsListView.setHasFixedSize(true);

        newsListAdapter = new NewsListAdapter(view.getContext(),newsItems);

        newsListView.setAdapter(newsListAdapter);

    }

    private void setUpSwipeRefresh()
    {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpProgressBar()
    {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
    }
    private void getNewsFromDatabase()
    {
        newsItems = new Select().all().from(News.class).execute();
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
            img_placeholder.setVisibility(View.GONE);
            tv_placeholder.setVisibility(View.GONE);

            if (newsItems.size() == 0) { // check if any news are present
                progressBar.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout.isRefreshing())
                    progressBar.setVisibility(View.GONE);
            }

        }



        @Override
        protected Boolean doInBackground(String... params) {
            NewsSync newsSync = new NewsSync(newsUrl);

            if(!isConnected())  //if there is no internet connection
            {
                return false;
            }

            if(!hasInternet()) //if there is no internet
            {
                return false;
            }

            return newsSync.syncNews();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            swipeRefreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
            if(result)
            {
                getNewsFromDatabase();
                newsListAdapter.updateNews(newsItems);
            }else{
                if(newsItems.size() == 0)
                {
                    img_placeholder.setVisibility(View.VISIBLE);
                    tv_placeholder.setVisibility(View.VISIBLE);
                }
           
            }


        }
    }
}

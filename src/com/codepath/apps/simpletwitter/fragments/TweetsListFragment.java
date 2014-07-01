package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.EndlessScrollListener;
import com.codepath.apps.simpletwitter.R;
import com.codepath.apps.simpletwitter.TweetArrayAdapter;
import com.codepath.apps.simpletwitter.TwitterClient;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by yzhang29 on 6/28/14.
 */
public class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private PullToRefreshListView lvTweets;
    private TwitterClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
            	if(tweets.size()>0){
	                long maxId = tweets.get(tweets.size()-1).getUid()-1;
	                customLoadMoreDataFromApi(Long.toString(maxId));
            	}
            }
        });
        // Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
            	fetchTimelineAsync(lvTweets);
            }
        });
        return v;
    }

    public void fetchTimelineAsync(PullToRefreshListView lvTweets) {}
 
    
    // Append more data into the adapter
    public void customLoadMoreDataFromApi(String maxId) {}

    public void addAll(ArrayList<Tweet> tweets){
        aTweets.addAll(tweets);
    }
}

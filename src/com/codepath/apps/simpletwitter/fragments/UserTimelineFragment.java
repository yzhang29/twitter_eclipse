package com.codepath.apps.simpletwitter.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletwitter.TwitterClientApp;
import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

import org.json.JSONArray;

/**
 * Created by yzhang29 on 6/29/14.
 */
public class UserTimelineFragment extends TweetsListFragment {
    String userScreenName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userScreenName = getArguments().getString("userScreenName");
        populateTimeline();
    }

    public static UserTimelineFragment newInstance(String userScreenName) {
        UserTimelineFragment fragmentUser = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("userScreenName", userScreenName);
        fragmentUser.setArguments(args);
        return fragmentUser;
    }

    public void populateTimeline(){
        populateTimeline(null);
    }

    public void populateTimeline(String maxId){
        TwitterClientApp.getRestClient().getUserTimeline(userScreenName, maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray json) {
                Log.d("json", json.toString());
                addAll(Tweet.fromJsonArray(json));
            }

            @Override
            public void onFailure(Throwable e, String s) {
                super.onFailure(e, s);
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }

    @Override
    public void fetchTimelineAsync(final PullToRefreshListView lvTweets) {
    	 TwitterClientApp.getRestClient().getUserTimeline(userScreenName, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONArray json) {
                addAll(Tweet.fromJsonArray(json));
                lvTweets.onRefreshComplete();
            }
            @Override
            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
    	});

    }
    
    @Override
    public void customLoadMoreDataFromApi(String maxId) {
        populateTimeline(maxId);
    }
}

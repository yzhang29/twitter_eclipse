package com.codepath.apps.simpletwitter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.fragments.UserTimelineFragment;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;


public class ProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String username = getIntent().getStringExtra("userScreenName");
        attachUserFragment(username);
        if(username==null || username.isEmpty()){
            loadProfileInfo();
        }
        else loadUserInfo(username);
    }

    private void loadProfileInfo() {
        TwitterClientApp.getRestClient().getUserInfo(
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        User u = User.fromJson(json);
                        getActionBar().setTitle("@" + u.getName());
                        populateProfileHeader(u);
                    }

                    @Override
                    public void onFailure(Throwable e, String s) {
                        super.onFailure(e, s);
                        Log.d("debug", e.toString());
                        Log.d("debug", s.toString());
                    }

                }
        );
    }

    private void loadUserInfo(String userScreenName) {
        TwitterClientApp.getRestClient().getOtherUserInfo(userScreenName,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        User u = User.fromJson(json);
                        getActionBar().setTitle("@" + u.getName());
                        populateProfileHeader(u);
                    }

                    @Override
                    public void onFailure(Throwable e, String s) {
                        super.onFailure(e, s);
                        Log.d("debug", e.toString());
                        Log.d("debug", s.toString());
                    }

                }
        );
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
    }


    public void attachUserFragment(String userScreenName){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimelineFragment fragmentUser = UserTimelineFragment.newInstance(userScreenName);
        ft.replace(R.id.flUserContainer, fragmentUser);
        ft.commit();
    }
}

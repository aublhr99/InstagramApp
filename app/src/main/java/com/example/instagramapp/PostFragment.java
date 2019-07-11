package com.example.instagramapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramapp.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PostFragment extends Fragment {

    List<Post> posts;
    PostAdapter postAdapter;
    LinearLayoutManager llm;
    RecyclerView rvPosts;
    SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_post, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        posts = new ArrayList<>();
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        llm =  new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(llm);
        postAdapter = new PostAdapter(posts);
        rvPosts.setAdapter(postAdapter);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        loadTopPosts();
        swipeUpRefresh();
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        posts.add(null);
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                        posts.set(i, objects.get(i));
                        postAdapter.notifyItemInserted(i);
                    }
                    Collections.reverse(posts);
                    swipeContainer.setRefreshing(false);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void swipeUpRefresh() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                postAdapter.clear();
                loadTopPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
}

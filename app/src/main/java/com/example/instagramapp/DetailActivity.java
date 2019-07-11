package com.example.instagramapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramapp.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivPostImage;
    TextView tvCaption;
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivPostImage = (ImageView) findViewById(R.id.ivPostImage);
        tvCaption = (TextView) findViewById(R.id.tvCaption);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);

        getCurrentPost();
    }

    private void getCurrentPost() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.getInBackground(getIntent().getStringExtra("postId"), new GetCallback<Post>() {
            public void done(Post item, ParseException e) {
                if (e == null) {
                    // Access data using the `get` methods for the object
                    String body = item.getDescription();
                    // Access special values that are built-in to each object
                    String username = getIntent().getStringExtra("username");
                    Date createdAt = item.getCreatedAt();
                    // Do whatever you want with the data...
                    tvCaption.setText(body);
                    tvUsername.setText(username);
                    tvTimestamp.setText(item.getRelativeTime(item.getCreatedAt().toString()));

                    Glide.with(DetailActivity.this)
                            .load(item.getImage().getUrl())
                            .into(ivPostImage);
                } else {
                    Log.e("Details Activity", "Couldn't get post.");
                    Toast.makeText(DetailActivity.this, "Couldn't get post.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}

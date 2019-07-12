package com.example.instagramapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramapp.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView tvUsername;
    ImageView ivPostImage;
    TextView tvCaption;
    TextView tvTimestamp;
    TextView tvLikes;
    ImageButton ibLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivPostImage = (ImageView) findViewById(R.id.ivPostImage);
        tvCaption = (TextView) findViewById(R.id.tvCaption);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        ibLike = (ImageButton) findViewById(R.id.ibLike);
        tvLikes = (TextView) findViewById(R.id.tvLikes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        actionBar.setIcon(R.drawable.nux_dayone_landing_logo);

        getCurrentPost();
    }

    private void getCurrentPost() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Specify the object id
        query.getInBackground(getIntent().getStringExtra("postId"), new GetCallback<Post>() {
            public void done(final Post item, ParseException e) {
                if (e == null) {
                    // Access data using the `get` methods for the object
                    String body = item.getDescription();
                    // Access special values that are built-in to each object
                    String username = getIntent().getStringExtra("username");
                    Date createdAt = item.getCreatedAt();
                    // Do whatever you want with the data...
                    tvCaption.setText(Html.fromHtml(item.getDescription()));
                    tvUsername.setText(username);
                    tvTimestamp.setText(item.getRelativeTime(item.getCreatedAt().toString()));
                    tvLikes.setText(Integer.toString(item.getNumLikes()));

                    if (item.isLiked()) {
                        ibLike.setImageResource(R.drawable.ufi_heart_active);
                        ibLike.setColorFilter(Color.argb(255,255,0,0));
                    } else {
                        ibLike.setImageResource(R.drawable.ufi_heart);
                        ibLike.setColorFilter(Color.argb(255,0,0,0));
                    }


                    Glide.with(DetailActivity.this)
                            .load(item.getImage().getUrl())
                            .into(ivPostImage);

                    ibLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!item.isLiked()) {
                                item.likePost(ParseUser.getCurrentUser());
                                ibLike.setImageResource(R.drawable.ufi_heart_active);
                                ibLike.setColorFilter(Color.argb(255,255,0,0));
                            } else {
                                item.unlike(ParseUser.getCurrentUser());
                                ibLike.setImageResource(R.drawable.ufi_heart);
                                ibLike.setColorFilter(Color.argb(255,0,0,0));
                            }
                            item.saveInBackground();
                            tvLikes.setText(Integer.toString(item.getNumLikes()));
                        }
                    });
                } else {
                    Log.e("Details Activity", "Couldn't get post.");
                    Toast.makeText(DetailActivity.this, "Couldn't get post.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}

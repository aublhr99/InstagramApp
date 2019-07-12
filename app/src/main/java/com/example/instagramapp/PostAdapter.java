package com.example.instagramapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramapp.model.Post;
import com.parse.ParseUser;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    static List<Post> mPosts;
    static Context context;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);

        return viewHolder;
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Post post = mPosts.get(position);

        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvCaption.setText(post.getDescription());
        viewHolder.tvTimestamp.setText(post.getRelativeTime(post.getCreatedAt().toString()));
        viewHolder.tvLikes.setText(Integer.toString(post.getNumLikes()));

        if (post.isLiked()) {
            viewHolder.ibLike.setImageResource(R.drawable.ufi_heart_active);
            viewHolder.ibLike.setColorFilter(Color.argb(255,255,0,0));
        } else {
            viewHolder.ibLike.setImageResource(R.drawable.ufi_heart);
            viewHolder.ibLike.setColorFilter(Color.argb(255,0,0,0));
        }

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPostImage);

        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.isLiked()) {
                    post.likePost(ParseUser.getCurrentUser());
                    viewHolder.ibLike.setImageResource(R.drawable.ufi_heart_active);
                    viewHolder.ibLike.setColorFilter(Color.argb(255,255,0,0));
                } else {
                    post.unlike(ParseUser.getCurrentUser());
                    viewHolder.ibLike.setImageResource(R.drawable.ufi_heart);
                    viewHolder.ibLike.setColorFilter(Color.argb(255,0,0,0));
                }
                post.saveInBackground();
                viewHolder.tvLikes.setText(Integer.toString(post.getNumLikes()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPostImage;
        TextView tvUsername;
        TextView tvCaption;
        TextView tvTimestamp;
        TextView tvLikes;
        ImageButton ibLike;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);

            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemView.setOnClickListener(this);
            //Toast.makeText(context, "Clicked!", Toast.LENGTH_LONG).show();
            int position = getAdapterPosition();

            Post post = mPosts.get(position);

            Intent details = new Intent(context, DetailActivity.class);
            details.putExtra("scrollPosition", position);
            details.putExtra("postId", post.getObjectId());
            details.putExtra("username", post.getUser().getUsername());

            context.startActivity(details);
        }
    }
}
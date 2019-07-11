package com.example.instagramapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramapp.model.Post;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Post post = mPosts.get(position);

        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvCaption.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPostImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPostImage;
        TextView tvUsername;
        TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPostImage = (ImageView) itemView.findViewById(R.id.ivPostImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            itemView.setOnClickListener(this);
//            //Toast.makeText(context, "Clicked!", Toast.LENGTH_LONG).show();
//            int position = getAdapterPosition();
//
//            Post post = mPosts.get(position);
//
//            Intent details = new Intent(context, DetailsActivity.class);
//            details.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
//            details.putExtra(User.class.getSimpleName(), Parcels.wrap(post.user));
//            details.putExtra("scrollPosition", position);
//
//            context.startActivity(details);
        }
    }
}
package com.example.instagramapp.model;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_USER = "user";
    private static final String KEY_IMAGE = "image";

    public JSONArray getFans() {
        if (getJSONArray("likedBy") == null) {
            return new JSONArray();
        } else {
            return getJSONArray("likedBy");
        }
    }

    public void likePost(ParseUser u) {
        add("likedBy", u);
    }

    public boolean isLiked() {
        JSONArray fans = getFans();
        if (fans != null) {
            for (int i = 0; i < fans.length(); i++) {
                try {
                    if (fans.getJSONObject(i).getString("objectId").equals(ParseUser.getCurrentUser().getObjectId())) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public String getDescription() {
        String caption = getString(KEY_DESCRIPTION);
        String description ="<b>" + getUser().getUsername().toString() + "</b> " + caption;
        return description;
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getMedia() {
        return getParseFile("media");
    }

    public void setMedia(ParseFile parseFile) {
        put("media", parseFile);
    }

    public int getNumLikes() {
        return getFans().length();
    }

    public void unlike(ParseUser currentUser) {
        ArrayList<ParseUser> user = new ArrayList<>();
        user.add(currentUser);
        removeAll("likedBy", user);
    }

    public String getCaption() {
        return getString(KEY_DESCRIPTION);
    }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");
            return this;
        }
    }

    public String getRelativeTime(String parseDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        long dateMillis = 0;
        try {
            dateMillis = sf.parse(parseDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate.toUpperCase();
    }
}

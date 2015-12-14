package com.example.cpcs.dailyselfie;

import android.net.Uri;

/**
 * Created by cpcs on 11/11/15.
 */
public class Photo {
    private Uri mUri;
    private String mDate;


    public Photo(Uri uri, String date) {
        mUri = uri;
        mDate = date;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getDate() {
        return mDate;
    }
}

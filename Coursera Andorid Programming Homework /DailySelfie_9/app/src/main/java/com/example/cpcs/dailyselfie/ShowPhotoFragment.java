package com.example.cpcs.dailyselfie;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by cpcs on 11/11/15.
 */


public class ShowPhotoFragment extends Fragment {
    private Uri mUri = null;
    private static final String IMAGE_URI = "image_uri";

    public static ShowPhotoFragment newInstance(String uriString) {
        Bundle args = new Bundle();
        args.putString(IMAGE_URI, uriString);
        ShowPhotoFragment fragment = new ShowPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(false);
        String uriString = getArguments().getString(IMAGE_URI, null);
        mUri = TextUtils.isEmpty(uriString)?null:Uri.parse(uriString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_photo_fragment, container, false);
        if (mUri != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.one_photo);
            imageView.setImageURI(mUri);
        }
        return view;

    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

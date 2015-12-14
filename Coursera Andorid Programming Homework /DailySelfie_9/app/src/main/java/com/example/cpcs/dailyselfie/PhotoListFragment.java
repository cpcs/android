package com.example.cpcs.dailyselfie;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by cpcs on 11/11/15.
 */

public class PhotoListFragment extends Fragment {
    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mAdapter;
    private File mImageFile;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String MESSAGE_DELETE = "Do you want to delete it?";
    private static final String MESSAGE_DELETE_ALL = "Do you want to delete all?";
    private static final String YES = "yes";
    private static final String NO = "no";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private static final String FILE_PREFIX = "cpcs_";
    private static final String FILE_SUFFIX = ".jpg";
    private static final File  PICTURE_DIRECTORY = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private class PhotoComparator implements Comparator<Photo> {
        @Override
        public int compare(Photo photo1, Photo photo2) {
            return photo1.getDate().compareTo(photo2.getDate());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_list_fragment, container, false);
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.photo_list);
        mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = null;
        updateUI();
        return view;

    }

    private void updateUI() {
        if (mAdapter == null) {
            List<Photo> photos = new ArrayList<>();
            if (!PICTURE_DIRECTORY.exists()) {
                PICTURE_DIRECTORY.mkdirs();
            }
            for (File file : PICTURE_DIRECTORY.listFiles()) {
                if (file.isFile()) {
                    photos.add(new Photo(
                            Uri.fromFile(file),
                            DATE_FORMAT.format(file.lastModified())
                    ));
                }
            }
            Collections.sort(photos, new PhotoComparator());
            mAdapter = new PhotoAdapter(photos);
            mPhotoRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private File createImageFile()  {
        try {
            String timeStamp = DATE_FORMAT.format(new Date());
            String imageFileName = FILE_PREFIX + timeStamp + FILE_SUFFIX;
            return new File(PICTURE_DIRECTORY, imageFileName);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_photo_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.camera) {
            dispatchTakePictureIntent();
            return true;
        }
        if (id == R.id.delete_all) {
            if (mAdapter != null) {
                mAdapter.removeAll();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageFile = createImageFile();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,  Uri.fromFile(mImageFile));
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            mAdapter.addPhoto(new Photo(Uri.fromFile(mImageFile),
                    DATE_FORMAT.format(mImageFile.lastModified())));

        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mTextView;
        private ImageButton mButton;
        private Photo mPhoto;


        public PhotoHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)
                    itemView.findViewById(R.id.photo);
            mTextView = (TextView)
                    itemView.findViewById(R.id.date);
            mButton = (ImageButton)
                    itemView.findViewById(R.id.delete_button);
            mButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               AlertDialog.Builder alertDialogBuilder =
                                                       new AlertDialog.Builder(
                                                       getActivity());
                                               alertDialogBuilder
                                                       .setMessage(MESSAGE_DELETE)
                                                       .setCancelable(false)
                                                       .setPositiveButton(YES,
                                                               new DialogInterface
                                                                       .OnClickListener() {
                                                                   public void
                                                                   onClick(DialogInterface dialog,
                                                                           int id) {
                                                                       mAdapter.removePhoto(
                                                                               getLayoutPosition());

                                                                   }
                                                               })
                                                       .setNegativeButton(NO,
                                                               new DialogInterface
                                                                       .OnClickListener() {
                                                                   public void
                                                                   onClick(DialogInterface dialog,
                                                                           int id) {
                                                                       dialog.cancel();
                                                                   }
                                                               });
                                               alertDialogBuilder.create().show();
                                           }
                                       });
                    itemView.setOnClickListener(this);

        }

        public void bindPhoto(Photo Photo) {
            mPhoto = Photo;
            mImageView.setImageURI(mPhoto.getUri());
            mTextView.setText(mPhoto.getDate());
        }

        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,
                    ShowPhotoFragment.newInstance(mPhoto.getUri().toString()));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<Photo> mPhotos;
        public PhotoAdapter(List<Photo> photos) {
            mPhotos = photos;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.photo_list_item, parent, false);
            return new PhotoHolder(view);

        }
        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            Photo photo = mPhotos.get(position);
            holder.bindPhoto(photo);
        }
        @Override
        public int getItemCount() {
            return mPhotos.size();
        }


        public void addPhoto(Photo photo) {

            mPhotos.add(photo);
            notifyItemInserted(mPhotos.size() - 1);

        }

        private void deleteFile(int position) {
            new File(mPhotos.get(position).getUri().getPath()).delete();
        }

        public void removePhoto(int position) {
            if ((position >= 0) && (position < mPhotos.size())) {
                deleteFile(position);
                mPhotos.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

        }

        public void removeAll() {
            if (!mPhotos.isEmpty()) {
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(
                                getActivity());
                alertDialogBuilder
                        .setMessage(MESSAGE_DELETE_ALL)
                        .setCancelable(false)
                        .setPositiveButton(YES,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int id) {
                                        for (int i = 0; i < mPhotos.size(); ++i) {
                                            deleteFile(i);
                                        }
                                        mPhotos.clear();
                                        mPhotoRecyclerView.removeAllViews();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(NO,
                                new DialogInterface.OnClickListener() {
                                    public void
                                    onClick(DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.create().show();
            }

        }

    }

}

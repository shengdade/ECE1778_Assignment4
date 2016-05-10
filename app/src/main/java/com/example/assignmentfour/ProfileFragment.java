package com.example.assignmentfour;

/**
 * Created by dade on 10/02/16.
 */

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final String KEY_POSITION = "position";

    static ProfileFragment newInstance(int position) {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return (frag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView profileName = (TextView) result.findViewById(R.id.profileName);
        TextView profileBio = (TextView) result.findViewById(R.id.profileBio);
        ImageView profilePicture = (ImageView) result.findViewById(R.id.profilePicture);
        Button btnDelete = (Button) result.findViewById(R.id.btnDelete);
        Button btnInfo = (Button) result.findViewById(R.id.btnInfo);
        final int position = getArguments().getInt(KEY_POSITION, -1);
        if (position != -1) {
            Profile profile = MainFragment.db.getProfile(position);
            profileName.setText(profile.getName());
            profileBio.setText(profile.getBio());
            new DownloadImageTask(profilePicture).execute(profile.getPicture());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteProfile(position);
                }
            });
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleProfile(position);
                }
            });
        }
        return (result);
    }

    private void deleteProfile(final int position) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage("Are you sure to delete this profile?");
        dlgAlert.setNegativeButton("No", null);
        dlgAlert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainFragment.db.deleteProfile(MainFragment.db.getProfile(position));
                        ProfileActivity.pager.getAdapter().notifyDataSetChanged();
                        Toast.makeText(getActivity(), "profile deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void googleProfile(int position) {
        Profile profile = MainFragment.db.getProfile(position);
        Uri uri = Uri.parse("http://www.google.com/#q=" + profile.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new URL(url).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

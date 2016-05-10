package com.example.assignmentfour;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by dade on 10/02/16.
 */

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    public static DatabaseHelper db = null;
    private AsyncTask task = null;
    private EditText editURL;

    private Button btnClear;
    private Button btnPopulate;
    private Button btnSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_main, container, false);

        //find three buttons
        btnClear = (Button) resultView.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearPressed(v);
            }
        });
        btnClear.setEnabled(false);
        btnPopulate = (Button) resultView.findViewById(R.id.btnPopulate);
        btnPopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopulatePressed(v);
            }
        });
        btnPopulate.setEnabled(true);
        btnSearch = (Button) resultView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchPressed(v);
            }
        });
        btnSearch.setEnabled(false);

        //find the URL EditText
        editURL = (EditText) resultView.findViewById(R.id.URL);

        //connect to database
        db = new DatabaseHelper(getActivity().getApplicationContext());

        return (resultView);
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel(false);
        }

        db.close();
        super.onDestroy();
    }

    private void onClearPressed(View v) {
        btnClear.setEnabled(false);
        btnPopulate.setEnabled(true);
        btnSearch.setEnabled(false);
        db.deleteAllProfiles();
        Toast.makeText(getActivity(), "database cleared", Toast.LENGTH_SHORT).show();
    }

    private void onPopulatePressed(View v) {
        String URL = editURL.getText().toString();
        task = new PopulateTask().execute(URL);
    }

    private void onSearchPressed(View v) {
        getActivity().startActivity(new Intent(this.getActivity(), ProfileActivity.class));
    }

    private class PopulateTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            if (!isCancelled()) {
                dialog = new ProgressDialog(getActivity());
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Loading");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            int addTimes = 0;
            if (!isCancelled()) {
                try {
                    URL url = new URL(params[0]);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    Profile profile = new Profile();
                    String str;
                    while ((str = in.readLine()) != null) {
                        profile.setName(str);
                        if ((str = in.readLine()) != null) {
                            profile.setBio(str);
                        }
                        if ((str = in.readLine()) != null) {
                            profile.setPicture(strToURL(str));
                        }
                        addTimes += db.addProfile(profile);
                    }
                    in.close();
                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e.getMessage());
                }
            }
            return (String.valueOf(addTimes));
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            String toast = "newly populated " + result + " profile";
            if (Integer.parseInt(result) > 1) toast += "s";
            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
            btnClear.setEnabled(true);
            btnPopulate.setEnabled(true);
            btnSearch.setEnabled(true);
            task = null;
        }
    }

    private String strToURL(String str) {
        URI baseUri = null;
        String result = "";
        try {
            URI txtUri = new URI(editURL.getText().toString());
            baseUri = txtUri.getPath().endsWith("/") ? txtUri.resolve("..") : txtUri.resolve(".");
        } catch (URISyntaxException e) {
            Log.e(TAG, "IURISyntaxException: " + e.getMessage());
        }
        if (baseUri != null) {
            result = baseUri.toString();
        }
        return (result + str);
    }
}
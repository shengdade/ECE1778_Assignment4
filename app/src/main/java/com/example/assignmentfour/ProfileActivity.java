package com.example.assignmentfour;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {
    public static ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ProfileAdapter(getFragmentManager()));
    }
}

package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Contact_Us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Contact Us");
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
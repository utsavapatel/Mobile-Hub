package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getWindow().setStatusBarColor(Color.WHITE);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        int uid = Integer.parseInt(preferences.getString(puserid,"-1"));

        Thread t = new Thread()
        {
            public void run()
            {
                try{
                    sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if(uid != -1)
                    {
                        Intent i = new Intent(Splash.this,Category.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(Splash.this,login.class);
                        startActivity(i);
                    }

                }
            }
        };
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

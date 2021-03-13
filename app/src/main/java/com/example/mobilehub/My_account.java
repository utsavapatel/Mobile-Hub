package com.example.mobilehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class My_account extends AppCompatActivity {
TextView uname_firstletter,uname,user_email,myprofile,mycart,myorder,mywishlist,givefeedback,about,contactus,logout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private long backPressedTime;
    private Toast backToast;

    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    public static final String pusername = "username";
    public static final String puseremail = "useremail";
    public static final String pusermobile = "usermobile";
    public static final String puserpass = "userpassword";
    public static final String puseraddress = "useraddress";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Account");
        uname_firstletter = findViewById(R.id.uname_firstletter);
        uname = findViewById(R.id.uname);
        user_email = findViewById(R.id.user_email);
        myprofile = findViewById(R.id.myprofile);
        mycart = findViewById(R.id.mycart);
        myorder = findViewById(R.id.myorder);
        mywishlist = findViewById(R.id.mywishlist);
        givefeedback = findViewById(R.id.givefeedback);
        about = findViewById(R.id.about);
        contactus = findViewById(R.id.contactus);
        logout = findViewById(R.id.logout);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        String username = preferences.getString(pusername,"");
        String user_emailid = preferences.getString(puseremail,"");
        char first_letter = username.charAt(0);
        uname_firstletter.setText("" + first_letter);
        uname.setText(username);
        user_email.setText(user_emailid);

        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,My_Profile.class);
                startActivity(i);
            }
        });

        mycart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,My_cart.class);
                startActivity(i);
            }
        });

        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,My_Orders.class);
                startActivity(i);
            }
        });
        mywishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,My_Wishlist.class);
                startActivity(i);
            }
        });

        givefeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,give_feedback.class);
                startActivity(i);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,About_us.class);
                startActivity(i);
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_account.this,Contact_Us.class);
                startActivity(i);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent i = new Intent(My_account.this,login.class);
                startActivity(i);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        Intent i = new Intent(My_account.this,Category.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.all_products:
                        Intent iii = new Intent(My_account.this,All_products.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist_nav:
                        Intent ii = new Intent(My_account.this,My_Wishlist.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account_nav:
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {


        if(backPressedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            finishAffinity();
            //super.onBackPressed();
            return;
        }
        else
        {
            backToast = Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }
}
package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilehub.API_Helper.JsonField;
import com.example.mobilehub.API_Helper.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class My_Profile extends AppCompatActivity {

    TextView uname_firstletter;
    EditText etacc_uname,etacc_email,etacc_mobile,etacc_add;
    Button btnsavechanges,btnchangepass;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    public static final String pusername = "username";
    public static final String puseremail = "useremail";
    public static final String pusermobile = "usermobile";
    public static final String puseraddress = "useraddress";

    @Override
    public void onBackPressed() {
        Intent i = new Intent(My_Profile.this,My_account.class);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Profile");
        uname_firstletter = findViewById(R.id.uname_firstletter);

        etacc_uname = findViewById(R.id.etacc_uname);
        etacc_email = findViewById(R.id.etacc_email);
        etacc_mobile = findViewById(R.id.etacc_mobile);
        etacc_add = findViewById(R.id.etacc_add);
        btnsavechanges = findViewById(R.id.btnsavechanges);
        btnchangepass = findViewById(R.id.btnchangepass);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(My_Profile.this);


        String username = preferences.getString(pusername,"");
        String user_emailid = preferences.getString(puseremail,"");
        String umobile = preferences.getString(pusermobile,"");
        String uadd = preferences.getString(puseraddress,"").replace("<br>","\n");
        char first_letter = username.charAt(0);


        uname_firstletter.setText("" + first_letter);
        etacc_uname.setText(username);
        etacc_email.setText(user_emailid);
        etacc_mobile.setText(umobile);
        etacc_add.setText(uadd);

        btnsavechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updteProfile();
            }
        });
        etacc_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(My_Profile.this, "Sorry, You can't change your email address!", Toast.LENGTH_SHORT).show();
            }
        });
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_Profile.this,change_password.class);
                startActivity(i);
            }
        });
    }

    private void updteProfile() {
        progressDialog.setMessage("Saving Profile...");
        progressDialog.show();
        String userid = preferences.getString(puserid,"");

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.SAVE_PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                editor.putString(pusername,etacc_uname.getText().toString());
                editor.putString(pusermobile,etacc_mobile.getText().toString());
                editor.putString(puseraddress,etacc_add.getText().toString().replace("\n","<br>"));
                editor.commit();
                Toast.makeText(My_Profile.this, "Profile Updated Succesfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(My_Profile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_ID,userid);
                p.put(JsonField.KEY_USER_NAME,etacc_uname.getText().toString());
                p.put(JsonField.KEY_USER_MOBILE,etacc_mobile.getText().toString());
                p.put(JsonField.KEY_USER_ADDRESS,etacc_add.getText().toString().replace("\n","<br>"));
                return p;
            }
        };
        //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rq = Volley.newRequestQueue(My_Profile.this);
        rq.add(sr);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
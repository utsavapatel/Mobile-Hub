package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class email_verification extends AppCompatActivity {
    TextView tvsentEmail;
    EditText et_eotp;
    Button ebtnverify_otp;
    Intent i;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        getSupportActionBar().hide();

        tvsentEmail = findViewById(R.id.tvsentEmail);
        et_eotp = findViewById(R.id.et_eotp);
        ebtnverify_otp = findViewById(R.id.ebtnverify_otp);

        progressDialog = new ProgressDialog(email_verification.this);
        i = getIntent();
        String sentEmailon = i.getStringExtra("mail");
        String sentOTP = i.getStringExtra("sentotp");

        tvsentEmail.setText(sentEmailon);

        ebtnverify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sentOTP.equals(et_eotp.getText().toString()))
                {
                    Toast.makeText(email_verification.this, "Email Verified Successfully", Toast.LENGTH_SHORT).show();
                    signUp();
                }
                else
                {
                    Toast.makeText(email_verification.this, "OTP did not matched!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signUp() {

        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.KEY_SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(email_verification.this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                Intent signup_to_login = new Intent(email_verification.this,login.class);
                startActivity(signup_to_login);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(email_verification.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                String uname = i.getStringExtra("uname");
                String esentEmailon = i.getStringExtra("email");
                String umobile = i.getStringExtra("mobile");
                String upass = i.getStringExtra("pass");
                String uadd = i.getStringExtra("add");
                p.put(JsonField.KEY_USER_NAME,uname);
                p.put(JsonField.KEY_USER_EMAIL,esentEmailon);
                p.put(JsonField.KEY_USER_MOBILE,umobile);
                p.put(JsonField.KEY_USER_PASSWORD,upass);
                p.put(JsonField.KEY_USER_ADDRESS,uadd.replace("'",""));
                return p;
            }
        };
        //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rqsignup = Volley.newRequestQueue(email_verification.this);
        rqsignup.add(sr);
    }

}
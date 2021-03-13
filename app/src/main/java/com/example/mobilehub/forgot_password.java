package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class forgot_password extends AppCompatActivity {
 EditText etfp_email;
 Button btnrequest_otp;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        etfp_email = findViewById(R.id.etfp_email);
        btnrequest_otp = findViewById(R.id.btnrequest_otp);
        progressDialog = new ProgressDialog(forgot_password.this);
        btnrequest_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(forgot_password.this,otp_verification.class);
                //startActivity(i);
                if(checkEmail())
                {
                    sentOTP();
                }
            }
        });
    }

    private void sentOTP() {
        progressDialog.setMessage("Sending OTP...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.FORGOT_PASSWORD_OTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String msg = obj.getString("msg");
                    if(msg.equals("OTP sent Successfully"))
                    {
                        String otp = obj.getString("otp");
                        Toast.makeText(forgot_password.this, msg, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(forgot_password.this,otp_verification.class);
                        i.putExtra("email",etfp_email.getText().toString());
                        i.putExtra("sentotp",otp);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(forgot_password.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Toast.makeText(forgot_password.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_EMAIL,etfp_email.getText().toString());
                return p;
            }
        };
        RequestQueue rqsignup = Volley.newRequestQueue(forgot_password.this);
        rqsignup.add(sr);
    }

    private boolean checkEmail()
    {
        String e = etfp_email.getText().toString().trim();
        boolean a = false;
        if(etfp_email.getText().toString().trim().length() <= 0)
        {
            etfp_email.setError("Please Enter Email");
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            a = true;
        }
        else
        {
            etfp_email.setError("Enter Valid Email Address");
        }
        return a;
    }
}
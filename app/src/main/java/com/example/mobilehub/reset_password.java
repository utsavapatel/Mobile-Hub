package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class reset_password extends AppCompatActivity {

    TextView etresetnewpassword,etresetconfirmnewpassword;
    Button btnresetpass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().hide();

        etresetnewpassword = findViewById(R.id.etresetnewpassword);
        etresetconfirmnewpassword = findViewById(R.id.etresetconfirmnewpassword);
        btnresetpass = findViewById(R.id.btnresetpass);

        progressDialog = new ProgressDialog(reset_password.this);
        btnresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpass() && checkconfirmpass())
                {
                    resetPass();
                }
            }
        });
    }

    private void resetPass() {
        progressDialog.setMessage("Resetting Password...");
        progressDialog.show();

        Intent i = getIntent();
        String sentEmailon = i.getStringExtra("mail");
        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.RESET_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(reset_password.this, "Password Reset Succesfully", Toast.LENGTH_SHORT).show();
                Intent ii = new Intent(reset_password.this,login.class);
                startActivity(ii);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(reset_password.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_EMAIL,sentEmailon);
                p.put(JsonField.USER_PASSWORD,etresetnewpassword.getText().toString());
                return p;
            }
        };
        //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rq = Volley.newRequestQueue(reset_password.this);
        rq.add(sr);
    }

    private boolean checkpass()
    {
        boolean a = false;
        if(etresetnewpassword.getText().toString().trim().length() <= 0)
        {
            etresetnewpassword.setError("Enter New Password");
        }
        else if(etresetnewpassword.getText().toString().trim().length() < 6)
        {
            etresetnewpassword.setError("Your Password must be at least 6 characters long!");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkconfirmpass()
    {
        String pass = etresetnewpassword.getText().toString().trim();
        String cpass = etresetconfirmnewpassword.getText().toString().trim();
        boolean a = false;
        if(etresetconfirmnewpassword.getText().toString().trim().length() <= 0)
        {
            etresetconfirmnewpassword.setError("Confirm your New Password");
        }
        else if(etresetconfirmnewpassword.getText().toString().trim().length() < 6)
        {
            etresetconfirmnewpassword.setError("Your Confirm Password must be at least 6 characters long!");
        }
        else if(cpass.equals(pass))
        {
            a = true;
        }
        else
        {
            Toast.makeText(this, "Passwords does not matched!", Toast.LENGTH_SHORT).show();
        }
        return a;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(reset_password.this,login.class);
        startActivity(i);
        super.onBackPressed();
    }
}
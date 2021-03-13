package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class change_password extends AppCompatActivity {

    EditText etoldpassword,etnewpassword,etconfirmnewpassword;
    Button btnsubmit;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    public static final String puserpass = "userpassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setTitle("Change Password");

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        etoldpassword = findViewById(R.id.etoldpassword);
        etnewpassword = findViewById(R.id.etnewpassword);
        etconfirmnewpassword = findViewById(R.id.etconfirmnewpassword);

        btnsubmit = findViewById(R.id.btnsubmit);

        progressDialog = new ProgressDialog(change_password.this);


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkoldpass() && checkpass() && checkconfirmpass())
                {
                        changePassword();
                }
            }
        });
    }

    private void changePassword() {
        progressDialog.setMessage("Changing Password...");
        progressDialog.show();
        String userid = preferences.getString(puserid,"");

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.CHANGE_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                editor.putString(puserpass,etnewpassword.getText().toString());
                editor.commit();
                Toast.makeText(change_password.this, "Password Changed Succesfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(change_password.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_ID,userid);
                p.put(JsonField.USER_PASSWORD,etnewpassword.getText().toString());
                return p;
            }
        };
        //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rq = Volley.newRequestQueue(change_password.this);
        rq.add(sr);

    }

    private boolean checkoldpass()
    {
        boolean a = false;
        if(etoldpassword.getText().toString().trim().length() <= 0)
        {
            etoldpassword.setError("Enter Your Old Password");
        }
        else if(etoldpassword.getText().toString().trim().length() < 6)
        {
            etoldpassword.setError("Your Password must be at least 6 characters long!");
        }
        else
        {
            a = true;
        }
        return a;
    }

    private boolean checkpass()
    {
        boolean a = false;
        if(etnewpassword.getText().toString().trim().length() <= 0)
        {
            etnewpassword.setError("Enter New Password");
        }
        else if(etnewpassword.getText().toString().trim().length() < 6)
        {
            etnewpassword.setError("Your Password must be at least 6 characters long!");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkconfirmpass()
    {
        String useroldpass = preferences.getString(puserpass,"");
        String enteredOldPassword = etoldpassword.getText().toString();
        String pass = etnewpassword.getText().toString().trim();
        String cpass = etconfirmnewpassword.getText().toString().trim();
        boolean a = false;
        if(etconfirmnewpassword.getText().toString().trim().length() <= 0)
        {
            etconfirmnewpassword.setError("Confirm your New Password");
        }
        else if(etconfirmnewpassword.getText().toString().trim().length() < 6)
        {
            etconfirmnewpassword.setError("Your Confirm Password must be at least 6 characters long!");
        }
        else if(!useroldpass.equals(enteredOldPassword))
        {
            Toast.makeText(change_password.this, "Entered Old password is Wrong!", Toast.LENGTH_SHORT).show();
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
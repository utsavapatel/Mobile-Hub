package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class signup extends AppCompatActivity {

EditText username,email,mobile,password,confirm_pass,address,locality,city,state,pin;
Button btnsignup;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
        checkConnection();
        username = findViewById(R.id.etuname);
        email = findViewById(R.id.etemail);
        mobile = findViewById(R.id.etmobile);
        password = findViewById(R.id.etsignuppass);
        confirm_pass = findViewById(R.id.etconfirmpass);
        address = findViewById(R.id.etadd);
        locality = findViewById(R.id.etlocality);
        city = findViewById(R.id.etcity);
        state = findViewById(R.id.etstate);
        pin = findViewById(R.id.etpin);
        progressDialog = new ProgressDialog(signup.this);
        btnsignup = findViewById(R.id.btnsignup);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkuname() &&  checkEmail()  && checkcontact() && checkpass() && checkconfirmpass() && checkadd() && checklocality() && checkcity() && checkstate() && checkpin()){
                        sendVerificationEmail();
                }
            }
        });
    }

    private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null == activeNetwork)
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationEmail() {

        progressDialog.setMessage("Sending Verification Email...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.EMAIL_VERIFICATION_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               progressDialog.dismiss();
               try {
                   JSONObject obj = new JSONObject(response);
                   String msg = obj.getString("msg");
                   if(msg.equals("OTP sent Successfully"))
                   {
                       String otp = obj.getString("otp");
                       Toast.makeText(signup.this, msg, Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(signup.this,email_verification.class);
                       String finaladd = address.getText().toString() + ",<br>" + locality.getText().toString() + ",<br>" + city.getText().toString() + ",<br>" + state.getText().toString() + "-" + pin.getText().toString() + ".";
                       i.putExtra("uname",username.getText().toString());
                       i.putExtra("email",email.getText().toString());
                       i.putExtra("mobile",mobile.getText().toString());
                       i.putExtra("pass",password.getText().toString());
                       i.putExtra("add",finaladd);
                       i.putExtra("mail",email.getText().toString());
                       i.putExtra("sentotp",otp);
                       startActivity(i);
                   }
                   else
                   {
                       Toast.makeText(signup.this, msg, Toast.LENGTH_SHORT).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
               Toast.makeText(signup.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> p = new HashMap<>();
               p.put(JsonField.KEY_USER_NAME,username.getText().toString());
               p.put(JsonField.KEY_USER_EMAIL,email.getText().toString());
               return p;
           }
       };
        RequestQueue rqsignup = Volley.newRequestQueue(signup.this);
        rqsignup.add(sr);
    }

    private boolean checkuname()
    {
        boolean a = false;
        if(username.getText().toString().trim().length() <= 0)
        {
            username.setError("Please Enter Name");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkEmail()
    {
        String e = email.getText().toString().trim();
        boolean a = false;
        if(email.getText().toString().trim().length() <= 0)
        {
            email.setError("Please Enter Email");
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            a = true;
        }
        else
        {
            email.setError("Enter Valid Email Address");
        }
        return a;
    }
    private boolean checkcontact()
    {
        boolean a = false;
        if(mobile.getText().toString().trim().length() <= 0)
        {
            mobile.setError("Please Enter Mobile Number");
        }
        else if(mobile.getText().toString().trim().length() == 10)
        {
            a = true;
        }
        else
        {
            mobile.setError("Please Enter Valid Mobile Number");
        }
        return a;
    }
    private boolean checkpass()
    {
        boolean a = false;
        if(password.getText().toString().trim().length() <= 0)
        {
            password.setError("Please Enter Password");
        }
        else if(password.getText().toString().trim().length() < 6)
        {
            password.setError("Your Password must be at least 6 characters long!");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkconfirmpass()
    {
        String pass = password.getText().toString().trim();
        String cpass = confirm_pass.getText().toString().trim();
        boolean a = false;
        if(confirm_pass.getText().toString().trim().length() <= 0)
        {
            confirm_pass.setError("Please Confirm your Password");
        }
        else if(confirm_pass.getText().toString().trim().length() < 6)
        {
            confirm_pass.setError("Your Confirm Password must be at least 6 characters long!");
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
    private boolean checkadd()
    {
        boolean a = false;
        if(address.getText().toString().trim().length() <= 0)
        {
            address.setError("Please Enter Address");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checklocality()
    {
        boolean a = false;
        if(locality.getText().toString().trim().length() <= 0)
        {
            locality.setError("Please Enter Locality");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkcity()
    {
        boolean a = false;
        if(city.getText().toString().trim().length() <= 0)
        {
            city.setError("Please Enter Town/City");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkstate()
    {
        boolean a = false;
        if(state.getText().toString().trim().length() <= 0)
        {
            state.setError("Please Enter State");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkpin()
    {
        boolean a = false;
        if(pin.getText().toString().trim().length() <= 0)
        {
            pin.setError("Enter PIN code");
        }
        else if(pin.getText().toString().trim().length() == 6)
        {
            a = true;
        }
        else
        {
            pin.setError("Enter Valid PIN code");
        }
        return a;
    }
}
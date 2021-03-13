package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.mobilehub.Adapter.productAdapter;
import com.example.mobilehub.Model.productModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText email,password;
Button login;
CheckBox terms_conditions;
TextView signup,forgotpass;
ProgressDialog progressDialog;
SharedPreferences preferences;
SharedPreferences.Editor editor;
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

        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        checkConnection();

        email = findViewById(R.id.etemaillogin);
        password = findViewById(R.id.passwordlogin);
        signup = findViewById(R.id.txtsignup);
        forgotpass = findViewById(R.id.txtforgotpass);
        login = findViewById(R.id.btnlogin);
        terms_conditions = findViewById(R.id.cb);
        progressDialog = new ProgressDialog(login.this);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmail() && checkpass() && checkTandC())
                {
                    getUsers();
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_to_signup = new Intent(login.this,signup.class);
                startActivity(login_to_signup);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotintent = new Intent(login.this,forgot_password.class);
                startActivity(forgotintent);
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

    private void getUsers() {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(login.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.KEY_USER_EMAIL,email.getText().toString());
                p.put(JsonField.KEY_USER_PASSWORD,password.getText().toString());
                return p;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.USER_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.USER_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objProduct = jsonArray.optJSONObject(i);
                        String userid = objProduct.getString(JsonField.USER_ID);
                        String username = objProduct.getString(JsonField.USER_NAME);
                        String useremail = objProduct.getString(JsonField.USER_EMAIL);
                        String usermobile = objProduct.getString(JsonField.USER_MOBILE);
                        String userpassword = objProduct.getString(JsonField.USER_PASSWORD);
                        String useraddress = objProduct.getString(JsonField.USER_ADDRESS);


                        if(email.getText().toString().equals(useremail) && password.getText().toString().equals(userpassword)) {
                            editor.putString(puserid,userid);
                            editor.putString(pusername,username);
                            editor.putString(puseremail,useremail);
                            editor.putString(pusermobile,usermobile);
                            editor.putString(puserpass,userpassword);
                            editor.putString(puseraddress,useraddress);
                            editor.commit();
                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                             Intent logintoCategory = new Intent(login.this,Category.class);
                            startActivity(logintoCategory);
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(login.this, "Enter Correct login Credentials", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
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
    private boolean checkpass()
    {
        boolean a = false;
        if(password.getText().toString().trim().length() <= 0)
        {
            password.setError("Please Enter Password");
        }
        else
        {
            a = true;
        }
        return a;
    }
    private boolean checkTandC()
    {
        boolean a = false;
        a = terms_conditions.isChecked();
        if(a == false)
        {
            Toast.makeText(login.this, "Please accept our terms and conditions", Toast.LENGTH_SHORT).show();
        }
        return a;
    }
}

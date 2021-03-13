package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

public class give_feedback extends AppCompatActivity {

    EditText etfuname,etfeedmessage;
    RatingBar ratingbar;
    Button btnfeedbacksubmit;

    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    public static final String pusername = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Feedback");
        etfuname = findViewById(R.id.etfuname);
        etfeedmessage = findViewById(R.id.etfeedmessage);

        ratingbar = findViewById(R.id.ratingbar);
        btnfeedbacksubmit = findViewById(R.id.btnfeedbacksubmit);

        progressDialog = new ProgressDialog(give_feedback.this);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        String uname = preferences.getString(pusername,"");

        etfuname.setText(uname);

        btnfeedbacksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etfeedmessage.getText().toString().length() <= 0)
                {
                    etfeedmessage.setError("Message can't be blank");
                }
                else if(ratingbar.getRating() <= 0)
                {
                    Toast.makeText(give_feedback.this, "Please Rate Us", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    submitFeedback();
                }
            }
        });
    }

    private void submitFeedback() {
        progressDialog.setMessage("Submitting Feedback...");
        progressDialog.show();
        String uid = preferences.getString(puserid,"-1");
        int no_ofstars = ratingbar.getNumStars();
        float getRate = ratingbar.getRating();
        String rate = "" + getRate + "/" + no_ofstars;
        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.FEEDBACK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(give_feedback.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(give_feedback.this,Category.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(give_feedback.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_ID,uid);
                p.put(JsonField.FEEDBACK_MESSGE,etfeedmessage.getText().toString());
                p.put(JsonField.FEEDBACK_RATING,rate);
                return p;
            }
        };
        RequestQueue rqsignup = Volley.newRequestQueue(give_feedback.this);
        rqsignup.add(sr);
    }
}
package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class otp_verification extends AppCompatActivity {
   TextView sentEmail;
   EditText et_otp;
   Button btnverify_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        getSupportActionBar().hide();

        sentEmail = findViewById(R.id.sentEmail);
        et_otp = findViewById(R.id.et_otp);
        btnverify_otp = findViewById(R.id.btnverify_otp);

        Intent i = getIntent();
        String sentEmailon = i.getStringExtra("email");
        String sentOTP = i.getStringExtra("sentotp");

        sentEmail.setText(sentEmailon);

        btnverify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sentOTP.equals(et_otp.getText().toString()))
                {
                    Toast.makeText(otp_verification.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(otp_verification.this,reset_password.class);
                    i.putExtra("mail",sentEmail.getText().toString());
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(otp_verification.this, "OTP did not matched!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(otp_verification.this,login.class);
        startActivity(i);
        super.onBackPressed();
    }
}
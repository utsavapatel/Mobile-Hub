package com.example.mobilehub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import pl.droidsonroids.gif.GifImageView;

import static java.security.AccessController.getContext;

public class order_success extends AppCompatActivity {
    Button btnshopmore,btnviewOrder;
 GifImageView gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        this.getWindow().setStatusBarColor(Color.WHITE);
        getSupportActionBar().hide();

        btnshopmore = findViewById(R.id.btnshopmore);
        btnviewOrder = findViewById(R.id.btnviewOrder);

        gif = findViewById(R.id.gif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.order_success) // Replace with a valid url
                .addListener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        return false;
                    }
                })
                .into(gif);

        btnshopmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(order_success.this,Category.class);
                startActivity(i);
            }
        });
        btnviewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(order_success.this,My_Orders.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(order_success.this,Category.class);
        startActivity(i);
        super.onBackPressed();
    }
}
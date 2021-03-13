package com.example.mobilehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mobilehub.API_Helper.JsonField;
import com.example.mobilehub.API_Helper.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class product_details extends AppCompatActivity {
    ImageView p_image1,p_image2;
    TextView p_nameTxt,p_pricetxt,p_stocktxt,p_detailstxt;
    Button btnWishlist,btnCart;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String pid,uid,cuid,cpid;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_cart_icon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_cart:
                Intent toCart = new Intent(product_details.this,My_cart.class);
                startActivity(toCart);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        p_image1 = findViewById(R.id.pimage1);
        p_image2 = findViewById(R.id.pimage2);

        p_nameTxt = findViewById(R.id.p_nameTxt);
        p_pricetxt = findViewById(R.id.p_priceTtxt);
        p_stocktxt = findViewById(R.id.p_stock);
        p_detailstxt = findViewById(R.id.p_detailsTxt);

        btnWishlist = findViewById(R.id.btnaddToWishlist);
        btnCart = findViewById(R.id.btnaddToCart);

        progressDialog = new ProgressDialog(product_details.this);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();
        uid = preferences.getString(puserid,"0");
        /*cpid = preferences.getString(cartPid,"0");
        cuid = preferences.getString(cartUid,"0");*/

        Intent intent = getIntent();
        pid = intent.getStringExtra("proid");
        String p_image = intent.getStringExtra("pimg");
        String p_name = intent.getStringExtra("pname");
        String p_price = intent.getStringExtra("pprice");
        String p_stock = intent.getStringExtra("pqty");
        String p_details = intent.getStringExtra("pdetails");

        setTitle(p_name);

        Glide.with(product_details.this).load(WebUrl.IMAGE_URL+p_image).into(p_image1);
        Glide.with(product_details.this).load(WebUrl.IMAGE_URL+p_image).into(p_image2);

        p_nameTxt.setText(p_name);
        p_pricetxt.setText(p_price);
        p_detailstxt.setText(p_details);

        if(p_stock.equals("0"))
        {
            p_stocktxt.setText("Out of Stock");
            p_stocktxt.setTextColor(Color.parseColor("#B12704"));
        }
        else
        {
            p_stocktxt.setText("In Stock");
            p_stocktxt.setTextColor(Color.parseColor("#007600"));
        }

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.COMPARE_CART_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String msg = obj.getString("msg");
                    if(msg.equals("Product is already added"))
                    {
                        btnCart.setText("View Cart");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Something Went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.CART_USER_ID,uid);
                p.put(JsonField.CART_PRODUCT_ID,pid);
                return p;
            }
        };
        RequestQueue rqcart = Volley.newRequestQueue(product_details.this);
        rqcart.add(sr);

        StringRequest srwishlist = new StringRequest(Request.Method.POST, WebUrl.COMPARE_WISHLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String msg = obj.getString("msg");
                    if(msg.equals("Product is already added wishlist"))
                    {
                        btnWishlist.setText("View Wishlist");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Something Went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.CART_USER_ID,uid);
                p.put(JsonField.CART_PRODUCT_ID,pid);
                return p;
            }
        };
        RequestQueue rqwishlist = Volley.newRequestQueue(product_details.this);
        rqwishlist.add(srwishlist);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCart.getText().toString().equals("View Cart"))
                {
                    Intent i = new Intent(product_details.this,My_cart.class);
                    startActivity(i);
                }
                else if (p_stocktxt.getText().toString().equals("Out of Stock"))
                {
                    Toast.makeText(product_details.this, "This Product is Unavailable.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addToCart();
                }
            }
        });

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnWishlist.getText().toString().equals("View Wishlist"))
                {
                    Intent i = new Intent(product_details.this,My_Wishlist.class);
                    startActivity(i);
                }
                else if (p_stocktxt.getText().toString().equals("Out of Stock"))
                {
                    Toast.makeText(product_details.this, "This Product is Unavailable.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addToWishlist();
                }
            }
        });
    }

    private void addToWishlist() {
        progressDialog.setMessage("Adding to wishlist...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.WISHLIST_INSERT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Added to Wishlist Successful", Toast.LENGTH_SHORT).show();
                //btnCart.setText("View Cart");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Something Went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                btnWishlist.setText("View Wishlist");
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.CART_USER_ID,uid);
                p.put(JsonField.CART_PRODUCT_ID,pid);
                return p;
            }
        };
        RequestQueue rqsignup = Volley.newRequestQueue(product_details.this);
        rqsignup.add(sr);
    }

    private void addToCart() {

        progressDialog.setMessage("Adding to cart...");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.CART_INSERT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Added to Cart Successful", Toast.LENGTH_SHORT).show();
                //btnCart.setText("View Cart");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(product_details.this, "Something Went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                btnCart.setText("View Cart");
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.CART_USER_ID,uid);
                p.put(JsonField.CART_PRODUCT_ID,pid);
                return p;
            }
        };
        RequestQueue rqsignup = Volley.newRequestQueue(product_details.this);
        rqsignup.add(sr);
    }
}
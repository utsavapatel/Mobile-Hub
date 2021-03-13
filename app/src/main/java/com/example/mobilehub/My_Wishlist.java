package com.example.mobilehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.mobilehub.API_Helper.JsonField;
import com.example.mobilehub.API_Helper.WebUrl;
import com.example.mobilehub.Adapter.Cart_Adapter;
import com.example.mobilehub.Adapter.Wishlist_Adapter;
import com.example.mobilehub.Model.Cart_Model;
import com.example.mobilehub.Model.Wishlist_Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_Wishlist extends AppCompatActivity {
    TextView wishlistEmpty;
    RecyclerView rvwishlistItem;
    ArrayList<Wishlist_Model> Wishlist_Item;
    ProgressBar pbar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    String cuid;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__wishlist);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.wishlist_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        Intent ii = new Intent(My_Wishlist.this,Category.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.all_products:
                        Intent iii = new Intent(My_Wishlist.this,All_products.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist_nav:
                        return true;
                    case R.id.account_nav:
                        Intent i = new Intent(My_Wishlist.this,My_account.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();
        cuid = preferences.getString(puserid,"-1");

        rvwishlistItem = findViewById(R.id.rvwishlist);
        wishlistEmpty = findViewById(R.id.wishlist_emptyMsg);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(My_Wishlist.this);
        rvwishlistItem.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvwishlistItem.addItemDecoration(dividerItemDecoration);
        pbar = findViewById(R.id.progressBar);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(product.this,2);
        //rvproduct.setLayoutManager(gridLayoutManager);
        setTitle("My Wishlist");
        getWishlistItem();

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void getWishlistItem() {

        pbar.setVisibility(View.VISIBLE);
        Wishlist_Item = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.VIEW_WISHLIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbar.setVisibility(View.INVISIBLE);
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.VIEW_CART_USER_ID,cuid);
                return p;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(My_Wishlist.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.VIEW_CART_PRODUCT_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.VIEW_WISHLIST_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objCartItem = jsonArray.optJSONObject(i);
                        String wishlist = objCartItem.getString(JsonField.VIEW_CART_USER_ID);
                        String wishlist_productid = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_ID);
                        String wishlist_productname = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_NAME);
                        String wishlist_productprice = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_PRICE);
                        String wishlist_productimage = objCartItem.getString(JsonField.VIEW_CART_PRODUCT__IMAGE);

                        Wishlist_Model wishlistItem = new Wishlist_Model();
                        wishlistItem.setUid(wishlist);
                        wishlistItem.setPro_id(wishlist_productid);
                        wishlistItem.setWishlist_product_name(wishlist_productname);
                        wishlistItem.setWishlist_product_price(wishlist_productprice);
                        wishlistItem.setWishlist_procuct_image(wishlist_productimage);
                        Wishlist_Item.add(wishlistItem);
                    }
                    Wishlist_Adapter wishlist_Adapter = new Wishlist_Adapter(My_Wishlist.this,Wishlist_Item);
                    rvwishlistItem.setAdapter(wishlist_Adapter);
                }
            }
            else
            {
                wishlistEmpty.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {


        if(backPressedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            finishAffinity();
            //super.onBackPressed();
            return;
        }
        else
        {
            backToast = Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }
}

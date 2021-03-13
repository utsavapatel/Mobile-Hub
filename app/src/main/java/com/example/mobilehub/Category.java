package com.example.mobilehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.mobilehub.API_Helper.JsonField;
import com.example.mobilehub.API_Helper.WebUrl;
import com.example.mobilehub.Adapter.Category_Adapter;
import com.example.mobilehub.Model.CategoryModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {
  RecyclerView rvcategory;
  ArrayList<CategoryModel> listCategory;
  TextView txtCat;
    private long backPressedTime;
    private Toast backToast;
    ImageSlider slider;
    ProgressBar pbar;
    BottomNavigationView bottomNavigationView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_cart_icon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_cart:
                Intent toCart = new Intent(Category.this,My_cart.class);
                startActivity(toCart);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //checkConnection();

        slider = findViewById(R.id.slider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("http://utsavapatel.000webhostapp.com/samsung.jpg"));
        slideModels.add(new SlideModel("http://utsavapatel.000webhostapp.com/iphone_banner.jpg"));
        slideModels.add(new SlideModel("http://utsavapatel.000webhostapp.com/oneplus.jpg"));
        slideModels.add(new SlideModel("http://utsavapatel.000webhostapp.com/oneplus2.jpg"));
        slideModels.add(new SlideModel("http://utsavapatel.000webhostapp.com/mi.jpg"));
        slider.setImageList(slideModels,true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pbar = findViewById(R.id.progressBar);
        txtCat = findViewById(R.id.txtCat);
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home_nav:
                        return true;
                    case R.id.all_products:
                        Intent iii = new Intent(Category.this,All_products.class);
                        startActivity(iii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist_nav:
                        Intent ii = new Intent(Category.this,My_Wishlist.class);
                        startActivity(ii);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account_nav:
                        Intent i = new Intent(Category.this,My_account.class);
                        startActivity(i);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //getSupportActionBar().setIcon(R.drawable.cart_icon);
        rvcategory = findViewById(R.id.rvcategory);

        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Category.this);
       //rvcategory.setLayoutManager(linearLayoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Category.this,2);
        rvcategory.setLayoutManager(gridLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvcategory.addItemDecoration(dividerItemDecoration);
        DividerItemDecoration dividerItemDecorationa = new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvcategory.addItemDecoration(dividerItemDecorationa);

        getCategory();
    }

   /*private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null == activeNetwork)
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void getCategory() {

        pbar.setVisibility(View.VISIBLE);
        listCategory = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.CATEGORY_DISPLAY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                txtCat.setVisibility(View.VISIBLE);
                pbar.setVisibility(View.GONE);
                slider.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.INVISIBLE);
                ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

                if(null == activeNetwork)
                {
                    Toast.makeText(Category.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Category.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Category.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.CATEGORY_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.CATEGORY_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objCategory = jsonArray.optJSONObject(i);
                        String categoryid = objCategory.getString(JsonField.CATEGORY_ID);
                        String categoryname = objCategory.getString(JsonField.CATEGORY_NAME);
                        String categoryimage = objCategory.getString(JsonField.CATEGORY_IMAGE);

                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCategory_id(categoryid);
                        categoryModel.setCategory_name(categoryname);
                        categoryModel.setCategory_image(categoryimage);
                        listCategory.add(categoryModel);
                    }
                    Category_Adapter category_adapter = new Category_Adapter(Category.this,listCategory);
                    rvcategory.setAdapter(category_adapter);
                }
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
package com.example.mobilehub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import com.example.mobilehub.Adapter.Category_Adapter;
import com.example.mobilehub.Adapter.productAdapter;
import com.example.mobilehub.Model.CategoryModel;
import com.example.mobilehub.Model.productModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_products extends AppCompatActivity {
    RecyclerView rvallproductdisplay;
    ArrayList<productModel> listProduct;
    ProgressBar pbar;
    productAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvallproductdisplay = findViewById(R.id.rvproductdisplay);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(All_products.this);
        rvallproductdisplay.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvallproductdisplay.addItemDecoration(dividerItemDecoration);
        pbar = findViewById(R.id.progressBar);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(product.this,2);
        //rvproduct.setLayoutManager(gridLayoutManager);

        setTitle("Products");
        getProduct();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(All_products.this,Category.class);
        startActivity(i);
        return true;
    }

    private void getProduct() {

        pbar.setVisibility(View.VISIBLE);
        listProduct = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.ALL_PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbar.setVisibility(View.GONE);
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(All_products.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.PRODUCT_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.PRODUCT_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objProduct = jsonArray.optJSONObject(i);
                        String productid = objProduct.getString(JsonField.PRODUCT_ID);
                        String productname = objProduct.getString(JsonField.PRODUCT_NAME);
                        String productprice = objProduct.getString(JsonField.PRODUCT_PRICE);
                        String productdetails = objProduct.getString(JsonField.PRODUCT_DETAILS);
                        String productquantity = objProduct.getString(JsonField.PRODUCT_quantity);
                        String productimage = objProduct.getString(JsonField.PRODUCT_IMAGE);

                        productModel productModel = new productModel();
                        productModel.setProduct_id(productid);
                        productModel.setProduct_name(productname);
                        productModel.setProduct_price(productprice);
                        productModel.setProduct_details(productdetails);
                        productModel.setProduct_quantity(productquantity);
                        productModel.setProduct_image(productimage);
                        listProduct.add(productModel);
                    }
                    productAdapter = new productAdapter(All_products.this,listProduct);
                    rvallproductdisplay.setAdapter(productAdapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.icon_search);
        MenuItem itemcart = menu.findItem(R.id.icon_cart);

        itemcart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent toCart = new Intent(All_products.this,My_cart.class);
                startActivity(toCart);
                return false;
            }
        });
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.hintSearchMess) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(All_products.this,Category.class);
        startActivity(i);
        super.onBackPressed();
    }
}
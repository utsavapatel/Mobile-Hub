 package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.example.mobilehub.Adapter.View_Order_Adapter;
import com.example.mobilehub.Model.CategoryModel;
import com.example.mobilehub.Model.View_order_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class My_Orders extends AppCompatActivity {
    RecyclerView rvorders;
    ArrayList<View_order_Model> listOrder_item;
    ProgressBar pbar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__orders);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Orders");
        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        rvorders = findViewById(R.id.rvorders);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(My_Orders.this);
        //linearLayoutManager.setReverseLayout(true);
        rvorders.setLayoutManager(linearLayoutManager);

        pbar = findViewById(R.id.progressBar);
        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(Category.this,2);
        rvcategory.setLayoutManager(gridLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvcategory.addItemDecoration(dividerItemDecoration);
        DividerItemDecoration dividerItemDecorationa = new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvcategory.addItemDecoration(dividerItemDecorationa);*/

        getOrders();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void getOrders() {
        pbar.setVisibility(View.VISIBLE);
        listOrder_item = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.VIEW_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbar.setVisibility(View.INVISIBLE);
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.INVISIBLE);
                Toast.makeText(My_Orders.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String uid = preferences.getString(puserid,"");
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.USER_ID,uid);
                return p;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(My_Orders.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.CATEGORY_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.ORDER_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objOrder = jsonArray.optJSONObject(i);
                        String Orderid = objOrder.getString(JsonField.ORDER_ID);
                        String item = objOrder.getString("purchased_item");
                        String odate = objOrder.getString(JsonField.ORDER_DATE);
                        String price = objOrder.getString("total");
                        String status = objOrder.getString(JsonField.ORDER_STATUS);

                        View_order_Model order_model = new View_order_Model();
                        order_model.setOrder_id(Orderid);
                        order_model.setOrder_product_name(item);
                        order_model.setOrder_date(odate);
                        order_model.setOrder_product_price(price);
                        order_model.setOrder_status(status);
                        listOrder_item.add(order_model);
                    }
                    View_Order_Adapter Order_adapter = new View_Order_Adapter(My_Orders.this,listOrder_item);
                    Collections.reverse(listOrder_item);
                    rvorders.setAdapter(Order_adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(My_Orders.this,My_account.class);
        overridePendingTransition(0,0);
        startActivity(i);
        super.onBackPressed();
    }
}
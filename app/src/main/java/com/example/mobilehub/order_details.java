package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.example.mobilehub.Adapter.Order_Details_Adapter;
import com.example.mobilehub.Adapter.View_Order_Adapter;
import com.example.mobilehub.Model.View_order_Model;
import com.example.mobilehub.Model.orderDetail_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class order_details extends AppCompatActivity {
    RecyclerView rvorderDetail;
    ArrayList<orderDetail_Model> listOrderDetail_item;
    TextView yourOrderlabel,orderDetailslabel,txt_odid,txt_payment,txt_dorder_date,txt_dorder_amount,txt_contact_number,
            txt_uname,txt_add,odsubtotal,odgrandtotal,odtxt_order_status;
    LinearLayout odetailLinear,yourOrderLinear;

    ProgressBar pbar;
    Intent i;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String main_key = "user_data";
    public static final String pusername = "username";
    public static final String pusermobile = "usermobile";
    public static final String puseraddress = "useraddress";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Order Summary");

        odetailLinear = findViewById(R.id.odetailLinear);
        yourOrderLinear = findViewById(R.id.yourOrderLinear);

        rvorderDetail = findViewById(R.id.rvorderDetail);
        yourOrderlabel = findViewById(R.id.yourOrderlabel);
        orderDetailslabel = findViewById(R.id.orderDetailslabel);
        txt_odid = findViewById(R.id.txt_odid);
        txt_payment = findViewById(R.id.txt_payment);
        txt_dorder_date = findViewById(R.id.txt_dorder_date);
        txt_dorder_amount = findViewById(R.id.txt_dorder_amount);
        txt_contact_number = findViewById(R.id.txt_contact_number);
        txt_uname = findViewById(R.id.txt_uname);
        txt_add = findViewById(R.id.txt_add);
        odsubtotal = findViewById(R.id.odsubtotal);
        odgrandtotal = findViewById(R.id.odgrandtotal);
        odtxt_order_status = findViewById(R.id.odtxt_order_status);

        i = getIntent();
        String orderid = i.getStringExtra("orderID");
        String orderDate = i.getStringExtra("orderDate");
        String orderTotal = i.getStringExtra("orderTotal");
        String orderStatus = i.getStringExtra("orderStatus");

        txt_odid.setText(orderid);
        txt_dorder_date.setText(orderDate);
        txt_dorder_amount.setText("₹" + orderTotal);
        odsubtotal.setText("₹" + orderTotal);
        odgrandtotal.setText("₹" + orderTotal);
        odtxt_order_status.setText(orderStatus);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();

        String umobile = preferences.getString(pusermobile,"");
        String username = preferences.getString(pusername,"");
        String add = preferences.getString(puseraddress,"").replace("<br>","\n");

        txt_contact_number.setText(umobile);
        txt_uname.setText(username);
        txt_add.setText(add);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(order_details.this);
        rvorderDetail.setLayoutManager(linearLayoutManager);

        pbar = findViewById(R.id.progressBar);

        getOrderDetails();
    }

    private void getOrderDetails() {
        pbar.setVisibility(View.VISIBLE);
        listOrderDetail_item = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.ORDER_DETAILS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pbar.setVisibility(View.INVISIBLE);
                orderDetailslabel.setVisibility(View.VISIBLE);
                odetailLinear.setVisibility(View.VISIBLE);
                yourOrderlabel.setVisibility(View.VISIBLE);
                yourOrderLinear.setVisibility(View.VISIBLE);
                parseJson(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbar.setVisibility(View.INVISIBLE);
                Toast.makeText(order_details.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String orderidpass = i.getStringExtra("orderID");
                Map<String,String> p = new HashMap<>();
                p.put(JsonField.ORDER_ID,orderidpass);
                return p;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(order_details.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.CATEGORY_FLAG);

            if(flag == 1)
            {
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.ORDER_DETAILS_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objOrder = jsonArray.optJSONObject(i);
                        String pname = objOrder.getString(JsonField.PRODUCT_NAME);
                        String payment = objOrder.getString("payment_method");
                        String price = objOrder.getString(JsonField.PRODUCT_PRICE);
                        String pimage = objOrder.getString(JsonField.PRODUCT_IMAGE);
                        String qty = objOrder.getString(JsonField.PURCHASED_QTY);

                        txt_payment.setText(payment);
                        orderDetail_Model orderDetail_model = new orderDetail_Model();
                        orderDetail_model.setOdpname(pname);
                        orderDetail_model.setOdpprice(price);
                        orderDetail_model.setOdpqty(qty);
                        orderDetail_model.setOdpimage(pimage);
                        listOrderDetail_item.add(orderDetail_model);
                    }
                    Order_Details_Adapter OrderDetails_adapter = new Order_Details_Adapter(order_details.this,listOrderDetail_item);
                    rvorderDetail.setAdapter(OrderDetails_adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
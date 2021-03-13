package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.mobilehub.Adapter.productAdapter;
import com.example.mobilehub.Model.Cart_Model;
import com.example.mobilehub.Model.productModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_cart extends AppCompatActivity {
    TextView cartEmpty,msg;
    ImageView cartIcon;
    Button btnShopping,btncheckout;
    RecyclerView rvcartItem;
    ArrayList<Cart_Model> Cartlist_Item;
    long total = 0;
    String checkout_order;
    Intent checkout;
    LinearLayout addLinear,summaryLinear;
    TextView addusername,useradd,usermobile,addlabel,summarylabel,subtotal,grandtotal;
    ProgressBar pbar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";
    public static final String pusername = "username";
    public static final String puseremail = "useremail";
    public static final String pusermobile = "usermobile";
    public static final String puserpass = "userpassword";
    public static final String puseraddress = "useraddress";
    String cuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();
        cuid = preferences.getString(puserid,"-1");
        String uname = preferences.getString(pusername,"");
        String add = preferences.getString(puseraddress,"").replace("<br>","\n");
        String umobile = preferences.getString(pusermobile,"");

        rvcartItem = findViewById(R.id.rvcart);
        cartEmpty = findViewById(R.id.cart_emptyMsg);
        msg = findViewById(R.id.extramsg);
        cartIcon = findViewById(R.id.cartIcon);

        addLinear = findViewById(R.id.addLinear);
        summaryLinear = findViewById(R.id.summaryLinear);

        summarylabel = findViewById(R.id.summarylabel);
        addusername = findViewById(R.id.addusername);
        useradd = findViewById(R.id.useradd);
        usermobile = findViewById(R.id.usermobile);
        addlabel = findViewById(R.id.addlabel);

        subtotal = findViewById(R.id.subtotal);
        grandtotal = findViewById(R.id.grandtotal);
        btncheckout = findViewById(R.id.btncheckout);

        addusername.setText(uname);
        useradd.setText(add);
        usermobile.setText("+91 - " + umobile);

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart_Adapter Cart_Adapter = new Cart_Adapter(My_cart.this,Cartlist_Item);
                checkout = new Intent(My_cart.this,place_and_confirm_order_.class);
                checkout_order = Cart_Adapter.checkout();
                checkout.putExtra("ordered_product_and_qty",checkout_order);
                checkout.putExtra("grandTotal",""+total);
                startActivity(checkout);
            }
        });
        btnShopping = findViewById(R.id.btnshopping);
        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(My_cart.this,Category.class);
                startActivity(i);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(My_cart.this);

        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rvcartItem.addItemDecoration(dividerItemDecoration);*/
        rvcartItem.setLayoutManager(linearLayoutManager);
        pbar = findViewById(R.id.progressBar);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(product.this,2);
        //rvproduct.setLayoutManager(gridLayoutManager);
        setTitle("My Cart");
        getCartItem();

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void getCartItem() {

        pbar.setVisibility(View.VISIBLE);
        Cartlist_Item = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrl.VIEW_CART_URL, new Response.Listener<String>() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(My_cart.this);
        requestQueue.add(stringRequest);
    }

    private void parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JsonField.VIEW_CART_PRODUCT_FLAG);

            if(flag == 1)
            {
                addLinear.setVisibility(View.VISIBLE);
                addlabel.setVisibility(View.VISIBLE);
                summaryLinear.setVisibility(View.VISIBLE);
                summarylabel.setVisibility(View.VISIBLE);
                btncheckout.setVisibility(View.VISIBLE);
                JSONArray jsonArray = jsonObject.optJSONArray(JsonField.VIEW_CART_ARRAY);

                if(jsonArray.length() > 0)
                {
                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject objCartItem = jsonArray.optJSONObject(i);
                        String cart_userid = objCartItem.getString(JsonField.VIEW_CART_USER_ID);
                        String cart_productid = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_ID);
                        String cart_productname = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_NAME);
                        String cart_productprice = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_PRICE);
                        String cart_productquantity = objCartItem.getString(JsonField.VIEW_CART_PRODUCT_QUANTITY);
                        String cart_productimage = objCartItem.getString(JsonField.VIEW_CART_PRODUCT__IMAGE);
                        String pro_qty = objCartItem.getString(JsonField.PRODUCT_quantity);
                        String cart_product_price = objCartItem.getString((JsonField.VIEW_CART_PRODUCT_PRODUCT));

                        Cart_Model cartItem = new Cart_Model();
                        cartItem.setUid(cart_userid);
                        cartItem.setPro_id(cart_productid);
                        cartItem.setCart_product_name(cart_productname);
                        cartItem.setCart_product_price(cart_productprice);
                        cartItem.setItem_qty(cart_productquantity);
                        cartItem.setCart_procuct_image(cart_productimage);
                        cartItem.setUpdated_price(cart_product_price);
                        cartItem.setProduct_qty(pro_qty);
                        Cartlist_Item.add(cartItem);
                    }
                    Cart_Adapter Cart_Adapter = new Cart_Adapter(My_cart.this,Cartlist_Item);
                    rvcartItem.setAdapter(Cart_Adapter);

                    total = Cart_Adapter.grandTotal();
                    subtotal.setText("₹" + total);
                    grandtotal.setText("₹" + total);
                    subtotal.setVisibility(View.VISIBLE);
                    grandtotal.setVisibility(View.VISIBLE);

                }
            }
            else
            {
                cartIcon.setVisibility(View.VISIBLE);
                cartEmpty.setVisibility(View.VISIBLE);
                msg.setVisibility(View.VISIBLE);
                btnShopping.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(My_cart.this,Category.class);
        startActivity(i);
        super.onBackPressed();
    }
}
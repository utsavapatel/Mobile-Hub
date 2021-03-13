package com.example.mobilehub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class place_and_confirm_order_ extends AppCompatActivity {
    TextView orderitem,totalamount;
    RadioGroup rgb;
    RadioButton rbcod,rbdebitcredit;
    Button btnplaceorder;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String puserid = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_and_confirm_order_);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Checkout");

        orderitem = findViewById(R.id.orderitem);
        totalamount = findViewById(R.id.totalamount);

        rbcod = findViewById(R.id.rbcod);
        rbdebitcredit = findViewById(R.id.rbdebitcredit);
        rgb = findViewById(R.id.rgb);
        btnplaceorder = findViewById(R.id.btnplaceorder);

        Intent i = getIntent();
        String orderqty_and_proname = i.getStringExtra("ordered_product_and_qty").replace("<br>" , "\n");
        String total = i.getStringExtra("grandTotal");

        preferences = getSharedPreferences(main_key, MODE_PRIVATE);
        editor = preferences.edit();


        orderitem.setText(orderqty_and_proname);
        totalamount.setText("₹" + total);

        progressDialog = new ProgressDialog(place_and_confirm_order_.this);
        rgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rbdebitcredit.isChecked())
                {
                    btnplaceorder.setText("Pay ₹" + totalamount.getText().toString());
                }
                else
                {
                    btnplaceorder.setText("Place Order");
                }
            }
        });
        btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbcod.isChecked() || rbdebitcredit.isChecked())
                {
                    placeOrder();
                }
                else
                {
                    Toast.makeText(place_and_confirm_order_.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void placeOrder() {
        String pmethod;
        if(rbcod.isChecked())
        {
            pmethod = rbcod.getText().toString();
        }
        else
        {
            pmethod = rbdebitcredit.getText().toString();
        }
        String uid = preferences.getString(puserid,"");

        AlertDialog.Builder builder = new AlertDialog.Builder(place_and_confirm_order_.this);
        builder.setTitle("Please Confirm");
        builder.setMessage("Are sure want to place this order?")
                .setCancelable(false)
                .setPositiveButton("Go Ahead", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Placing Order...");
                        progressDialog.show();

                        StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.PLACE_ORDER_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                Intent i = new Intent(place_and_confirm_order_.this,order_success.class);
                                startActivity(i);
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
                                p.put(JsonField.USER_ID,uid);
                                p.put("purchased_item",orderitem.getText().toString());
                                p.put("total",totalamount.getText().toString().replace("₹",""));
                                p.put(JsonField.PAYMENT_METHOD,pmethod);
                                return p;
                            }
                        };
                        RequestQueue rq = Volley.newRequestQueue(place_and_confirm_order_.this);
                        rq.add(sr);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
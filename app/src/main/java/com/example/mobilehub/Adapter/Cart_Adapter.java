package com.example.mobilehub.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.mobilehub.Category;
import com.example.mobilehub.Model.Cart_Model;
import com.example.mobilehub.My_cart;
import com.example.mobilehub.R;
import com.example.mobilehub.login;
import com.example.mobilehub.signup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.ViewHolder> {
    Context cart_context;
    ArrayList<Cart_Model> list_cartItem;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String cartUid = "userid";
    public static final String cartPid = "productid";

    public Cart_Adapter(Context cart_context, ArrayList<Cart_Model> list_cartItem) {
        this.cart_context = cart_context;
        this.list_cartItem = list_cartItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        cart_context = parent.getContext();
        View cView = LayoutInflater.from(cart_context).inflate(R.layout.my_cart_row_item,parent,false);
        return new ViewHolder(cView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart_Model cartItemModel = list_cartItem.get(position);
        String ppid = cartItemModel.getPro_id();
        String uid = cartItemModel.getUid();
        String cart_pro_name = cartItemModel.getCart_product_name();
        String cart_pro_price = cartItemModel.getCart_product_price();
        String cart_pro_item = cartItemModel.getItem_qty();
        String pro_qty = cartItemModel.getProduct_qty();
        String updated_cartprice = cartItemModel.getUpdated_price();

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(cart_context);

        preferences = cart_context.getSharedPreferences(main_key, cart_context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(cartUid,uid);
        editor.putString(cartPid,ppid);
        editor.commit();

        holder.cart_pname.setText(cart_pro_name);
        holder.cart_item_qty.setText(cart_pro_item);

        if(holder.cart_item_qty.getText().toString().equals("1"))
        {
            holder.cart_pprice.setText(cart_pro_price);
        }
        else
        {
            holder.cart_pprice.setText(updated_cartprice);
        }


        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(cart_context);
                builder.setTitle("Please Confirm");
                builder.setMessage("Are sure want to remove this product from your cart?")
                        .setCancelable(false)
                        .setPositiveButton("Go Ahead", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Removing Product from Cart...");
                                progressDialog.show();

                                StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.REMOVE_CART_ITEM_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(cart_context, "Product Removed from cart Successfully", Toast.LENGTH_SHORT).show();
                                        list_cartItem.remove(position);
                                        notifyDataSetChanged();
                                        Intent i = new Intent(cart_context,My_cart.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        cart_context.startActivity(i);
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
                                        p.put(JsonField.VIEW_CART_USER_ID,uid);
                                        p.put(JsonField.PRODUCT_ID,ppid);
                                        return p;
                                    }
                                };
                                RequestQueue rqsignup = Volley.newRequestQueue(cart_context);
                                rqsignup.add(sr);
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
        });
        holder.increment_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = holder.cart_item_qty.getText().toString();
                int a = Integer.parseInt(i);
                if(!pro_qty.equals(""+a))
                {
                    a++;
                    holder.cart_item_qty.setText(""+a);
                    String price = cartItemModel.getCart_product_price();
                    int ac_price = Integer.parseInt(price);
                    int new_price = ac_price*a;
                    holder.cart_pprice.setText(""+new_price);
                    String updated_qty = holder.cart_item_qty.getText().toString();
                    String updated_price = holder.cart_pprice.getText().toString();
                    //updateCart();
                    progressDialog.setMessage("Updating Cart...");
                    progressDialog.show();

                    StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.UPDATE_CART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(cart_context, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(cart_context,My_cart.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            cart_context.startActivity(i);
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
                            p.put(JsonField.VIEW_CART_USER_ID,uid);
                            p.put(JsonField.PRODUCT_ID,ppid);
                            p.put(JsonField.VIEW_CART_PRODUCT_QUANTITY,updated_qty);
                            p.put(JsonField.VIEW_CART_PRODUCT_PRODUCT,updated_price);
                            return p;
                        }
                    };
                    //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue rqsignup = Volley.newRequestQueue(cart_context);
                    rqsignup.add(sr);
                }
                else
                    {
                    Toast.makeText(cart_context, "Sorry, No more than " + pro_qty + " items available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.decrement_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = holder.cart_item_qty.getText().toString();
                int a = Integer.parseInt(i);
                if(a > 1)
                {
                    a--;
                    holder.cart_item_qty.setText(""+a);
                    String price = holder.cart_pprice.getText().toString();
                    int actual_price = Integer.parseInt(price);
                    String ac_price = cartItemModel.getCart_product_price();
                    int old_price = Integer.parseInt(ac_price);
                    int newp = actual_price-old_price;
                    holder.cart_pprice.setText(""+newp);

                    String updated_qty = holder.cart_item_qty.getText().toString();
                    String updated_price = holder.cart_pprice.getText().toString();
                    //updateCart();
                    progressDialog.setMessage("Updating Cart...");
                    progressDialog.show();

                    StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.UPDATE_CART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(cart_context, "Cart Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(cart_context,My_cart.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            cart_context.startActivity(i);

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
                            p.put(JsonField.VIEW_CART_USER_ID,uid);
                            p.put(JsonField.PRODUCT_ID,ppid);
                            p.put(JsonField.VIEW_CART_PRODUCT_QUANTITY,updated_qty);
                            p.put(JsonField.VIEW_CART_PRODUCT_PRODUCT,updated_price);
                            return p;
                        }
                    };
                    //sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue rqsignup = Volley.newRequestQueue(cart_context);
                    rqsignup.add(sr);
                }

            }
        });
        Glide.with(cart_context).load(WebUrl.IMAGE_URL + cartItemModel.getCart_procuct_image()).into(holder.cart_pimage);
    }

    public long grandTotal() {
        long totalPrice = 0;
        for (int i = 0; i < list_cartItem.size(); i++) {
            if(list_cartItem.get(i).getItem_qty().equals("1"))
            {
                totalPrice += Integer.parseInt(list_cartItem.get(i).getCart_product_price());
            }
            else
            {
                totalPrice += Integer.parseInt(list_cartItem.get(i).getUpdated_price());
            }

        }
        return totalPrice;
    }

    public String checkout() {
        String ordered_pname = "";
        for (int i = 0; i < list_cartItem.size(); i++)
        {
            ordered_pname = ordered_pname.concat(list_cartItem.get(i).getItem_qty() + " x " + list_cartItem.get(i).getCart_product_name() + "<br>");
        }
        return ordered_pname;
    }

    @Override
    public int getItemCount() {
        return list_cartItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView cart_pimage;
        TextView cart_pname,cart_pprice,cart_item_qty;
        ImageButton remove_item,increment_qty,decrement_qty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_pimage = itemView.findViewById(R.id.cart_product_image);

            cart_pname = itemView.findViewById(R.id.cart_product_name_txt);
            cart_pprice = itemView.findViewById(R.id.cart_product_price_txt);
            cart_item_qty = itemView.findViewById(R.id.cart_item_qty_text);

            remove_item = itemView.findViewById(R.id.btnremoveitem);
            increment_qty = itemView.findViewById(R.id.btn_increament);
            decrement_qty = itemView.findViewById(R.id.btn_decreament);
        }
    }
}

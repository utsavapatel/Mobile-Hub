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

import com.example.mobilehub.Model.Wishlist_Model;
import com.example.mobilehub.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.ViewHolder> {
    Context wishlist_context;
    ArrayList<Wishlist_Model> list_wishlistItem;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String main_key = "user_data";
    public static final String cartUid = "userid";
    public static final String cartPid = "productid";

    public Wishlist_Adapter(Context wishlist_context, ArrayList<Wishlist_Model> list_wishlistItem) {
        this.wishlist_context = wishlist_context;
        this.list_wishlistItem = list_wishlistItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        wishlist_context = parent.getContext();
        View cView = LayoutInflater.from(wishlist_context).inflate(R.layout.wishlist_row_item,parent,false);
        return new ViewHolder(cView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wishlist_Model wishlist_model = list_wishlistItem.get(position);
        String ppid = wishlist_model.getPro_id();
        String uid = wishlist_model.getUid();
        String wishlist_pro_name = wishlist_model.getWishlist_product_name();
        String wishlist_pro_price = wishlist_model.getWishlist_product_price();
        Glide.with(wishlist_context).load(WebUrl.IMAGE_URL + wishlist_model.getWishlist_procuct_image()).into(holder.wishlist_pimage);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(wishlist_context);

        holder.wishlist_pname.setText(wishlist_pro_name);
        holder.wishlist_pprice.setText(wishlist_pro_price);

        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(wishlist_context);
                builder.setMessage("Are sure want to remove this product from your wishlist?")
                        .setCancelable(false)
                        .setPositiveButton("Go Ahead", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Removing Product from Wishlist...");
                                progressDialog.show();

                                StringRequest sr = new StringRequest(Request.Method.POST, WebUrl.REMOVE_WISHLIST_ITEM_URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        Toast.makeText(wishlist_context, "Product Removed from Wishlist Successfully", Toast.LENGTH_SHORT).show();
                                        list_wishlistItem.remove(position);
                                        notifyDataSetChanged();

                                        if(list_wishlistItem.size() <= 0)
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(wishlist_context);
                                            builder.setMessage("Your wishlist is Empty! Do you want to go back?")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent i = new Intent(wishlist_context,Category.class);
                                                            wishlist_context.startActivity(i);
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
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
                                RequestQueue rqsignup = Volley.newRequestQueue(wishlist_context);
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
    }


    @Override
    public int getItemCount() {
        return list_wishlistItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView wishlist_pimage;
        TextView wishlist_pname,wishlist_pprice;
        ImageButton remove_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wishlist_pimage = itemView.findViewById(R.id.wishlist_product_image);

            wishlist_pname = itemView.findViewById(R.id.wishlist_product_name_txt);
            wishlist_pprice = itemView.findViewById(R.id.wishlist_product_price_txt);
            remove_item = itemView.findViewById(R.id.btnremovewishlistitem);

        }
    }


}

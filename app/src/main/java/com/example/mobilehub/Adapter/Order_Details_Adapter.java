package com.example.mobilehub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobilehub.API_Helper.WebUrl;
import com.example.mobilehub.Category;
import com.example.mobilehub.Model.CategoryModel;
import com.example.mobilehub.Model.View_order_Model;
import com.example.mobilehub.Model.orderDetail_Model;
import com.example.mobilehub.R;
import com.example.mobilehub.product;

import java.util.ArrayList;

public class Order_Details_Adapter extends RecyclerView.Adapter<Order_Details_Adapter.ViewHolder> {
    Context order_detail_context;
    ArrayList<orderDetail_Model> listOrderDetail;
    orderDetail_Model order_detail_model;

    public Order_Details_Adapter(Context order_detail_context, ArrayList<orderDetail_Model> listOrderDetail) {
        this.order_detail_context = order_detail_context;
        this.listOrderDetail = listOrderDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        order_detail_context = parent.getContext();
        View mview = LayoutInflater.from(order_detail_context).inflate(R.layout.order_detail_row,parent,false);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        order_detail_model = listOrderDetail.get(position);

        String opname = order_detail_model.getOdpname();
        String opqty = order_detail_model.getOdpqty();
        String opprice = order_detail_model.getOdpprice();

        holder.orderDetail_product_name_txt.setText(opname);
        holder.orderDetail_product_qty_txt.setText("x " + opqty);
        holder.orderDetail_product_price_txt.setText("₹" + opprice);
        Glide.with(order_detail_context).load(WebUrl.IMAGE_URL + order_detail_model.getOdpimage()).into(holder.orderDetail_product_image);
    }

    @Override
    public int getItemCount() {
        return listOrderDetail.size();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView orderDetail_product_image;
        TextView orderDetail_product_name_txt,orderDetail_product_qty_txt,orderDetail_product_price_txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetail_product_image = itemView.findViewById(R.id.orderDetail_product_image);
            orderDetail_product_name_txt = itemView.findViewById(R.id.orderDetail_product_name_txt);
            orderDetail_product_qty_txt = itemView.findViewById(R.id.orderDetail_product_qty_txt);
            orderDetail_product_price_txt = itemView.findViewById(R.id.orderDetail_product_price_txt);
        }
    }
}

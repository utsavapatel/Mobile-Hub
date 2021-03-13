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
import com.example.mobilehub.R;
import com.example.mobilehub.order_details;
import com.example.mobilehub.product;

import java.util.ArrayList;

public class View_Order_Adapter extends RecyclerView.Adapter<View_Order_Adapter.ViewHolder> {
    Context order_context;
    ArrayList<View_order_Model> listOrder;
    View_order_Model order_model;

    public View_Order_Adapter(Context order_context, ArrayList<View_order_Model> listOrder) {
        this.order_context = order_context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        order_context = parent.getContext();
        View mview = LayoutInflater.from(order_context).inflate(R.layout.row_view_order_item,parent,false);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        order_model = listOrder.get(position);

        String oid = order_model.getOrder_id();
        String item = order_model.getOrder_product_name();
        String odate = order_model.getOrder_date();
        String total = order_model.getOrder_product_price();
        String status = order_model.getOrder_status();

        holder.txt_oid.setText(oid);
        holder.txt_item.setText(item);
        holder.txt_order_date.setText(odate);
        holder.txt_order_amount.setText("₹" + total);
        holder.txt_order_status.setText(status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Odid = listOrder.get(position).getOrder_id();
                String oddate = listOrder.get(position).getOrder_date();
                String order_total = listOrder.get(position).getOrder_product_price();
                String order_status = listOrder.get(position).getOrder_status();
                Intent i = new Intent(order_context, order_details.class);
                i.putExtra("orderID",Odid);
                i.putExtra("orderDate",oddate);
                i.putExtra("orderTotal",order_total);
                i.putExtra("orderStatus",order_status);
                order_context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView txt_oid,txt_item,txt_order_date,txt_order_amount,txt_order_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_oid = itemView.findViewById(R.id.txt_oid);
            txt_item = itemView.findViewById(R.id.txt_item);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_order_amount = itemView.findViewById(R.id.txt_order_amount);
            txt_order_status = itemView.findViewById(R.id.txt_order_status);
        }

    }
}

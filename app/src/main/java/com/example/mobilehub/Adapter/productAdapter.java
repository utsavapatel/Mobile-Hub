package com.example.mobilehub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobilehub.API_Helper.WebUrl;
import com.example.mobilehub.Model.productModel;
import com.example.mobilehub.R;
import com.example.mobilehub.product_details;

import java.util.ArrayList;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> implements Filterable {
    Context contextproduct;
    ArrayList<productModel> listProduct;
    ArrayList<productModel> backup;

    public productAdapter(Context contextproduct, ArrayList<productModel> listProduct) {
        this.contextproduct = contextproduct;
        this.listProduct = listProduct;
        backup = new ArrayList<>(listProduct);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        contextproduct = parent.getContext();
        View productView = LayoutInflater.from(contextproduct).inflate(R.layout.product_row_item,parent,false);
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     productModel productModel = listProduct.get(position);
      String pname = productModel.getProduct_name();
      String pprice = productModel.getProduct_price();
      holder.txtproductname.setText(pname);
      holder.txtproductprice.setText(pprice);
      Glide.with(contextproduct).load(WebUrl.IMAGE_URL+productModel.getProduct_image()).into(holder.imgproduct);

        String pid = listProduct.get(position).getProduct_id();
        String pimage = listProduct.get(position).getProduct_image();
        String proname = listProduct.get(position).getProduct_name();
        String proprice = listProduct.get(position).getProduct_price();
        String pdetails = listProduct.get(position).getProduct_details().replace("<br>","");
        String pqty = listProduct.get(position).getProduct_quantity();
        Intent i = new Intent(contextproduct, product_details.class);
        i.putExtra("proid",pid);
        i.putExtra("pimg",pimage);
        i.putExtra("pname",proname);
        i.putExtra("pprice",proprice);
        i.putExtra("pdetails",pdetails);
        i.putExtra("pqty",pqty);

        holder.btnviewproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextproduct.startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextproduct.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {

            ArrayList<productModel> filteredData = new ArrayList<>();

            if(keyword.toString().isEmpty())
            {
                filteredData.addAll(backup);
            }
            else
            {
                for(productModel obj : backup)
                {
                    if(obj.getProduct_name().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                    {
                        filteredData.add(obj);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listProduct.clear();
            listProduct.addAll((ArrayList<productModel>)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       ImageView imgproduct;
       TextView txtproductname,txtproductprice;
       Button btnviewproduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgproduct = itemView.findViewById(R.id.product_image);
            txtproductname = itemView.findViewById(R.id.product_name_txt);
            txtproductprice = itemView.findViewById(R.id.product_price_txt);
            btnviewproduct = itemView.findViewById(R.id.btnviewproduct);
        }
    }
}

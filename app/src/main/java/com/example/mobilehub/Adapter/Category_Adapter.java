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
import com.example.mobilehub.R;
import com.example.mobilehub.product;

import java.util.ArrayList;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    Context context;
    ArrayList<CategoryModel> listCategory;
    String cid,cname;
    CategoryModel categoryModel;

    public Category_Adapter(Context context, ArrayList<CategoryModel> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View mview = LayoutInflater.from(context).inflate(R.layout.category_row_item,parent,false);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    categoryModel = listCategory.get(position);

    cname = categoryModel.getCategory_name();
    holder.cat_name.setText(cname);
    Glide.with(context).load(WebUrl.IMAGE_URL+categoryModel.getCategory_image()).into(holder.cat_image);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cid = listCategory.get(position).getCategory_id();
            String Category = listCategory.get(position).getCategory_name();
            Intent i = new Intent(context,product.class);
            i.putExtra("categoryId",cid);
            i.putExtra("cname",Category);
            context.startActivity(i);
        }
    });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder
    {
       ImageView cat_image;
       TextView cat_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_image = itemView.findViewById(R.id.category_image);
            cat_name = itemView.findViewById(R.id.category_txt);

        }

    }
}

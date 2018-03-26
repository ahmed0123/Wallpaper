package com.example.android.wallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.R;

/**
 * Created by Ahmed El Hendawy on 2018/03/26.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	
	public TextView category_name;
	public ImageView background_image;
	
	ItemClickListener itemClickListener;
	
	public CategoryViewHolder(View itemView) {
		super(itemView);
		category_name = itemView.findViewById(R.id.categoryName);
		background_image = itemView.findViewById(R.id.categoryImage);
		
		itemView.setOnClickListener(this);
		
	}
	
	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}
	
	@Override
	public void onClick(View view) {
		itemClickListener.onClick(view, getAdapterPosition());
	}
}

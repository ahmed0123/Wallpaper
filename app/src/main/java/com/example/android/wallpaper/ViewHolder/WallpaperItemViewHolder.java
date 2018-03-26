package com.example.android.wallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.R;

/**
 * Created by Ahmed El Hendawy on 2018/03/26.
 */

public class WallpaperItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	
	public ImageView wallpaperImage;
	
	private ItemClickListener itemClickListener;
	
	public WallpaperItemViewHolder(View itemView) {
		super(itemView);
		
		wallpaperImage = itemView.findViewById(R.id.wallpaperImageView);
		
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

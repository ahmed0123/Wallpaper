package com.example.android.wallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.Activites.ViewWallpaperActivity;
import com.example.android.wallpaper.Database.Recents;
import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.Model.WallpaperItem;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.example.android.wallpaper.ViewHolder.WallpaperItemViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed El Hendawy on 2018/03/27.
 */

public class RecentAdapter extends RecyclerView.Adapter<WallpaperItemViewHolder> {
	
	private Context context;
	private List<Recents> recents;
	
	public RecentAdapter(Context context, List<Recents> recents) {
		this.context = context;
		this.recents = recents;
	}
	
	@NonNull
	@Override
	public WallpaperItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.waallpaper_list_item, parent, false);
		int height = parent.getMeasuredHeight() / 2;
		itemView.setMinimumHeight(height);
		return new WallpaperItemViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(@NonNull final WallpaperItemViewHolder holder, final int position) {
		Picasso.get()
				.load(recents.get(position).getImageLink())
				.networkPolicy(NetworkPolicy.OFFLINE)
				.into(holder.wallpaperImage, new Callback() {
					@Override
					public void onSuccess() {
					
					}
					
					@Override
					public void onError(Exception e) {
						Picasso.get()
								.load(recents.get(position).getImageLink())
								.error(R.drawable.ic_collections_black_24dp)
								.into(holder.wallpaperImage, new Callback() {
									@Override
									public void onSuccess() {
									
									}
									
									@Override
									public void onError(Exception e) {
										Log.e("Error", "couldn't fetch image");
									}
								});
					}
				});
		holder.setItemClickListener(new ItemClickListener() {
			@Override
			public void onClick(View view, int position) {
				Intent intent = new Intent(context, ViewWallpaperActivity.class);
				WallpaperItem wallpaperItem = new WallpaperItem();
				wallpaperItem.setCategoryId(recents.get(position).getCategoryId());
				wallpaperItem.setImageLink(recents.get(position).getImageLink());
				Constants.select_background = wallpaperItem;
				Constants.selected_Background_key = recents.get(position).getKey();
				context.startActivity(intent);
			}
		});
	}
	
	@Override
	public int getItemCount() {
		return recents.size();
	}
}

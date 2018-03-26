package com.example.android.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.Model.WallpaperItem;
import com.example.android.wallpaper.Utils.Constants;
import com.example.android.wallpaper.ViewHolder.WallpaperItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class WallpaperListActivity extends AppCompatActivity {
	
	Query query;
	FirebaseRecyclerOptions<WallpaperItem> options;
	FirebaseRecyclerAdapter<WallpaperItem, WallpaperItemViewHolder> adapter;
	
	RecyclerView wallpaperList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper_list);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(Constants.CATEGORY_NAME);
		setSupportActionBar(toolbar);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		wallpaperList = findViewById(R.id.wallpaperList);
		wallpaperList.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(WallpaperListActivity.this, 2);
		wallpaperList.setLayoutManager(gridLayoutManager);
		
		loadBackgroundList();
	}
	
	private void loadBackgroundList() {
		query = FirebaseDatabase.getInstance().getReference(Constants.BACKGROUND_PREFERNCE)
				.orderByChild("categoryId").equalTo(Constants.CATEGORY_ID_SELECTED);
		options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
				.setQuery(query, WallpaperItem.class)
				.build();
		
		adapter = new FirebaseRecyclerAdapter<WallpaperItem, WallpaperItemViewHolder>(options) {
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
			protected void onBindViewHolder(@NonNull final WallpaperItemViewHolder holder, int position, @NonNull final WallpaperItem model) {
				Picasso.get()
						.load(model.getImageLink())
						.networkPolicy(NetworkPolicy.OFFLINE)
						.into(holder.wallpaperImage, new Callback() {
							@Override
							public void onSuccess() {
							
							}
							
							@Override
							public void onError(Exception e) {
								Picasso.get()
										.load(model.getImageLink())
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
						Intent intent = new Intent(WallpaperListActivity.this, ViewWallpaperActivity.class);
						Constants.select_background = model;
						startActivity(intent);
					}
				});
			}
		};
		adapter.startListening();
		wallpaperList.setAdapter(adapter);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (adapter != null) {
			adapter.startListening();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.startListening();
		}
	}
	
	@Override
	public void onStop() {
		if (adapter != null) {
			adapter.stopListening();
		}
		super.onStop();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
}

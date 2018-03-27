package com.example.android.wallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.Activites.ViewWallpaperActivity;
import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.Model.WallpaperItem;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.example.android.wallpaper.ViewHolder.WallpaperItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {
	
	
	private static TrendingFragment INSATNCE = null;
	RecyclerView recyclerView;
	FirebaseDatabase database;
	DatabaseReference categoryBackground;
	FirebaseRecyclerOptions<WallpaperItem> options;
	FirebaseRecyclerAdapter<WallpaperItem, WallpaperItemViewHolder> adapter;
	
	public TrendingFragment() {
		
		database = FirebaseDatabase.getInstance();
		categoryBackground = database.getReference(Constants.BACKGROUND_PREFERNCE);
		
		Query query = categoryBackground.orderByChild("viewCount")
				.limitToLast(10);
		
		options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
				.setQuery(query, WallpaperItem.class)
				.build();
		adapter = new FirebaseRecyclerAdapter<WallpaperItem, WallpaperItemViewHolder>(options) {
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
						Intent intent = new Intent(getActivity(), ViewWallpaperActivity.class);
						Constants.select_background = model;
						Constants.selected_Background_key = adapter.getRef(position).getKey();
						startActivity(intent);
					}
				});
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
		};
		
	}
	
	public static TrendingFragment getInsatnce() {
		
		if (INSATNCE == null) {
			INSATNCE = new TrendingFragment();
		}
		
		return INSATNCE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_trending, container, false);
		recyclerView = view.findViewById(R.id.trendList);
		recyclerView.setHasFixedSize(true);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setStackFromEnd(true);
		linearLayoutManager.setReverseLayout(true);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		loadTrendList();
		
		return view;
	}
	
	private void loadTrendList() {
		adapter.startListening();
		recyclerView.setAdapter(adapter);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.startListening();
		}
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (adapter != null) {
			adapter.startListening();
		}
	}
	
	@Override
	public void onStop() {
		adapter.stopListening();
		super.onStop();
	}
}

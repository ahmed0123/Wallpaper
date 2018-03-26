package com.example.android.wallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.Interfaces.ItemClickListener;
import com.example.android.wallpaper.Model.CategoryItem;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.example.android.wallpaper.ViewHolder.CategoryViewHolder;
import com.example.android.wallpaper.WallpaperListActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
	
	private static CategoryFragment INSATNCE = null;
	FirebaseDatabase database;
	DatabaseReference categoryBackground;
	FirebaseRecyclerOptions<CategoryItem> options;
	FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;
	RecyclerView categoriesList;
	
	public CategoryFragment() {
		database = FirebaseDatabase.getInstance();
		categoryBackground = database.getReference(Constants.CATEGORY_PREFERNCE);
		
		options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
				.setQuery(categoryBackground, CategoryItem.class)
				.build();
		
		adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
			@Override
			protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final CategoryItem model) {
				Picasso.get()
						.load(model.getImageLink())
						.networkPolicy(NetworkPolicy.OFFLINE)
						.into(holder.background_image, new Callback() {
							@Override
							public void onSuccess() {
							
							}
							
							@Override
							public void onError(Exception e) {
								Picasso.get()
										.load(model.getImageLink())
										.error(R.drawable.ic_collections_black_24dp)
										.into(holder.background_image, new Callback() {
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
				
				holder.category_name.setText(model.getName());
				
				holder.setItemClickListener(new ItemClickListener() {
					@Override
					public void onClick(View view, int position) {
						Constants.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey();
						Constants.CATEGORY_NAME = model.getName();
						startActivity(new Intent(getActivity(), WallpaperListActivity.class));
						
					}
				});
			}
			
			@NonNull
			@Override
			public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
				
				return new CategoryViewHolder(itemView);
			}
		};
	}
	
	public static CategoryFragment getInstance(){
		
		if (INSATNCE == null){
			INSATNCE = new CategoryFragment();
		}
		return INSATNCE;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_category, container, false);
		categoriesList = view.findViewById(R.id.categoriesList);
		categoriesList.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
		categoriesList.setLayoutManager(gridLayoutManager);
		
		setCategory();
		
		return view;
	}
	
	private void setCategory() {
		adapter.startListening();
		categoriesList.setAdapter(adapter);
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
}

package com.example.android.wallpaper.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
	
	private static CategoryFragment INSATNCE = null;
	
	public CategoryFragment() {
		// Required empty public constructor
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
		return inflater.inflate(R.layout.fragment_category, container, false);
	}
	
}

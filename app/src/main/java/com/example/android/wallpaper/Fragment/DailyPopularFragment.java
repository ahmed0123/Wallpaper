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
public class DailyPopularFragment extends Fragment {
	
	
	private static DailyPopularFragment INSATNCE = null;
	
	public DailyPopularFragment() {
		// Required empty public constructor
	}
	public static DailyPopularFragment getInsatnce(){
		
		if (INSATNCE == null) {
			INSATNCE = new DailyPopularFragment();
		}
		
		return INSATNCE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_daily_popular, container, false);
	}
	
}

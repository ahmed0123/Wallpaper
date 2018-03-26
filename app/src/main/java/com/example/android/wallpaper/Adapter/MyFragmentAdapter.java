package com.example.android.wallpaper.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.wallpaper.Fragment.CategoryFragment;
import com.example.android.wallpaper.Fragment.DailyPopularFragment;
import com.example.android.wallpaper.Fragment.RecentsFragment;

/**
 * Created by Ahmed El Hendawy on 2018/03/25.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
	
	private Context context;
	
	public MyFragmentAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}
	
	@Override
	public Fragment getItem(int position) {
		
		if (position == 0)
			return CategoryFragment.getInstance();
		else if (position == 1)
			return DailyPopularFragment.getInsatnce();
		else if (position == 2)
			return RecentsFragment.getInstance();
		else return null;
	}
	
	@Override
	public int getCount() {
		return 3;
	}
	
	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return "Category";
			
			case 1:
				return "DailyPopular";
			
			case 2:
				return "Recents";
			
		}
		return "";
	}
}

package com.example.android.wallpaper.Database.DataSource;

import com.example.android.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ahmed El Hendawy on 2018/03/27.
 */

public class RecentRepository implements IRecentsDataSource {
	
	
	private static RecentRepository instance;
	private IRecentsDataSource mLocalDataSource;
	
	public RecentRepository(IRecentsDataSource mLocalDataSource) {
		this.mLocalDataSource = mLocalDataSource;
	}
	
	public static RecentRepository getinstance(IRecentsDataSource mLocalDataSource) {
		
		if (instance == null) {
			instance = new RecentRepository(mLocalDataSource);
		}
		return instance;
	}
	
	@Override
	public Flowable<List<Recents>> getAllRecents() {
		return mLocalDataSource.getAllRecents();
	}
	
	@Override
	public void insertRecents(Recents... recents) {
		mLocalDataSource.insertRecents(recents);
	}
	
	@Override
	public void updateRecents(Recents... recents) {
		mLocalDataSource.updateRecents(recents);
	}
	
	@Override
	public void deleteRecents(Recents... recents) {
		mLocalDataSource.deleteRecents(recents);
	}
	
	@Override
	public void deleteAllRecents() {
		mLocalDataSource.deleteAllRecents();
	}
}

package com.example.android.wallpaper.Database.DataSource;

import com.example.android.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ahmed El Hendawy on 2018/03/27.
 */

public interface IRecentsDataSource {
	
	Flowable<List<Recents>> getAllRecents();
	
	void insertRecents(Recents... recents);
	
	void updateRecents(Recents... recents);
	
	void deleteRecents(Recents... recents);
	
	void deleteAllRecents();
}

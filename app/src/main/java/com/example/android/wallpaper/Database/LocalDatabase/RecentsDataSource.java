package com.example.android.wallpaper.Database.LocalDatabase;

import com.example.android.wallpaper.Database.DataSource.IRecentsDataSource;
import com.example.android.wallpaper.Database.Recents;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Ahmed El Hendawy on 2018/03/27.
 */

public class RecentsDataSource implements IRecentsDataSource {
	
	private static RecentsDataSource instance;
	private RecentsDAO recentsDAO;
	
	public RecentsDataSource(RecentsDAO recentsDAO) {
		this.recentsDAO = recentsDAO;
	}
	
	public static RecentsDataSource getInstance(RecentsDAO recentsDAO) {
		if (instance == null) {
			instance = new RecentsDataSource(recentsDAO);
		}
		return instance;
	}
	
	@Override
	public Flowable<List<Recents>> getAllRecents() {
		
		return recentsDAO.getAllRecents();
	}
	
	@Override
	public void insertRecents(Recents... recents) {
		recentsDAO.insertRecents(recents);
	}
	
	@Override
	public void updateRecents(Recents... recents) {
		recentsDAO.updateRecents(recents);
	}
	
	@Override
	public void deleteRecents(Recents... recents) {
		recentsDAO.deleteRecents(recents);
	}
	
	@Override
	public void deleteAllRecents() {
		recentsDAO.deleteAllRecents();
	}
}

package com.example.android.wallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.wallpaper.Adapter.RecentAdapter;
import com.example.android.wallpaper.Database.DataSource.RecentRepository;
import com.example.android.wallpaper.Database.LocalDatabase.LocalDatabase;
import com.example.android.wallpaper.Database.LocalDatabase.RecentsDataSource;
import com.example.android.wallpaper.Database.Recents;
import com.example.android.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RecentsFragment extends Fragment {
	
	
	private static RecentsFragment INSTANCE = null;
	CompositeDisposable compositeDisposable;
	RecentRepository recentRepository;
	RecyclerView recentsRecycler;
	List<Recents> recentsList;
	RecentAdapter adapter;
	Context context;
	
	
	@SuppressLint("ValidFragment")
	public RecentsFragment(Context context) {
		this.context = context;
		
		compositeDisposable = new CompositeDisposable();
		LocalDatabase database = LocalDatabase.getInstance(context);
		recentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));
	}
	
	public static RecentsFragment getInstance(Context context) {
		
		if (INSTANCE == null) {
			INSTANCE = new RecentsFragment(context);
		}
		return INSTANCE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View itemView = inflater.inflate(R.layout.fragment_recents, container, false);
		recentsRecycler = itemView.findViewById(R.id.recentsList);
		recentsRecycler.setHasFixedSize(true);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
		recentsRecycler.setLayoutManager(gridLayoutManager);
		recentsList = new ArrayList<>();
		adapter = new RecentAdapter(context, recentsList);
		recentsRecycler.setAdapter(adapter);
		
		loadRecent();
		
		return itemView;
		
	}
	
	private void loadRecent() {
		Disposable disposable = recentRepository.getAllRecents()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Consumer<List<Recents>>() {
					@Override
					public void accept(List<Recents> recents) throws Exception {
						onGetAllRecentsSuccess(recents);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.d("ERROR", throwable.getMessage());
						
					}
				});
		compositeDisposable.add(disposable);
	}
	
	private void onGetAllRecentsSuccess(List<Recents> recents) {
		recentsList.clear();
		recentsList.addAll(recents);
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		compositeDisposable.clear();
	}
}

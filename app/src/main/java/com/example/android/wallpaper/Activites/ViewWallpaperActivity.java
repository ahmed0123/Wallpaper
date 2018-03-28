package com.example.android.wallpaper.Activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.wallpaper.Database.DataSource.RecentRepository;
import com.example.android.wallpaper.Database.LocalDatabase.LocalDatabase;
import com.example.android.wallpaper.Database.LocalDatabase.RecentsDataSource;
import com.example.android.wallpaper.Database.Recents;
import com.example.android.wallpaper.Helper.SaveImageHelper;
import com.example.android.wallpaper.Model.WallpaperItem;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ViewWallpaperActivity extends AppCompatActivity {
	
	CoordinatorLayout coordinatorLayout;
	CollapsingToolbarLayout collapsingToolbarLayout;
	FloatingActionButton wallpaperFab, downloadFab;
	ImageView imageView;
	CompositeDisposable compositeDisposable;
	RecentRepository recentRepository;
	
	FloatingActionMenu mainFloating;
	com.github.clans.fab.FloatingActionButton fbShare;
	
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	
	
	private Target target = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
			Snackbar.make(coordinatorLayout, "Wallpaper was set", Snackbar.LENGTH_SHORT).show();
			try {
				wallpaperManager.setBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onBitmapFailed(Exception e, Drawable errorDrawable) {
		
		}
		
		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
		
		}
	};
	private Target facebookConvertBitmap = new Target() {
		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
			
			SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
			
			if (shareDialog.canShow(SharePhotoContent.class)) {
				SharePhotoContent content = new SharePhotoContent.Builder()
						.addPhoto(sharePhoto)
						.build();
				shareDialog.show(content);
			}
		}
		
		@Override
		public void onBitmapFailed(Exception e, Drawable errorDrawable) {
		
		}
		
		@Override
		public void onPrepareLoad(Drawable placeHolderDrawable) {
		
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_wallpaper);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
		
		setRoomDatabase();
		
		coordinatorLayout = findViewById(R.id.rootLayout);
		collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
		collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
		
		collapsingToolbarLayout.setTitle(Constants.CATEGORY_NAME);
		imageView = findViewById(R.id.image_thumb);
		
		Picasso.get()
				.load(Constants.select_background.getImageLink())
				.into(imageView);
		
		mainFloating = findViewById(R.id.menu);
		fbShare = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.facebook_share);
		fbShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
					
					@Override
					public void onSuccess(Sharer.Result result) {
						Toast.makeText(ViewWallpaperActivity.this, "Shared Successful", Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onCancel() {
						Toast.makeText(ViewWallpaperActivity.this, "Shared canceled", Toast.LENGTH_SHORT).show();
						
					}
					
					@Override
					public void onError(FacebookException error) {
						Toast.makeText(ViewWallpaperActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
						
					}
				});
				
				Picasso.get().load(Constants.select_background.getImageLink()).into(facebookConvertBitmap);
			}
			
		});
		//save image background to database
		addToRecents();
		wallpaperFab = findViewById(R.id.fabWallpaper);
		wallpaperFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Picasso.get()
						.load(Constants.select_background.getImageLink())
						.into(target);
				
			}
		});
		
		downloadFab = findViewById(R.id.fabDownload);
		downloadFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (ActivityCompat.checkSelfPermission(ViewWallpaperActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE);
					}
				} else {
					AlertDialog alertDialog = new SpotsDialog(ViewWallpaperActivity.this);
					alertDialog.show();
					alertDialog.setMessage("Please wait");
					
					String fileName = UUID.randomUUID().toString() + ".png";
					Picasso.get()
							.load(Constants.select_background.getImageLink())
							.into(new SaveImageHelper(
									getBaseContext(),
									alertDialog,
									getApplicationContext().getContentResolver(),
									fileName,
									"Wallpaper App"));
				}
			}
		});
		increaseViewCount();
	}
	
	private void increaseViewCount() {
		FirebaseDatabase.getInstance().getReference(Constants.BACKGROUND_PREFERNCE).child(Constants.selected_Background_key)
				.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						
						if (dataSnapshot.hasChild("viewCount")) {
							WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
							long count = wallpaperItem.getViewCount() + 1;
							
							Map<String, Object> update_value = new HashMap<>();
							update_value.put("viewCount", count);
							
							FirebaseDatabase.getInstance()
									.getReference(Constants.BACKGROUND_PREFERNCE)
									.child(Constants.selected_Background_key)
									.updateChildren(update_value)
									.addOnSuccessListener(new OnSuccessListener<Void>() {
										@Override
										public void onSuccess(Void aVoid) {
										
										}
									})
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											Toast.makeText(ViewWallpaperActivity.this, "cannot update view count", Toast.LENGTH_SHORT).show();
										}
									});
						} else {
							Map<String, Object> update_value = new HashMap<>();
							update_value.put("viewCount", Long.valueOf(1));
							
							FirebaseDatabase.getInstance()
									.getReference(Constants.BACKGROUND_PREFERNCE)
									.child(Constants.selected_Background_key)
									.updateChildren(update_value)
									.addOnSuccessListener(new OnSuccessListener<Void>() {
										@Override
										public void onSuccess(Void aVoid) {
										
										}
									})
									.addOnFailureListener(new OnFailureListener() {
										@Override
										public void onFailure(@NonNull Exception e) {
											Toast.makeText(ViewWallpaperActivity.this, "cannot update view count", Toast.LENGTH_SHORT).show();
										}
									});
						}
					}
					
					@Override
					public void onCancelled(DatabaseError databaseError) {
					
					}
				});
	}
	
	private void addToRecents() {
		Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
			
			@Override
			public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
				Recents recents = new Recents(
						Constants.select_background.getImageLink(),
						Constants.select_background.getCategoryId(),
						String.valueOf(System.currentTimeMillis()),
						Constants.selected_Background_key);
				
				recentRepository.insertRecents(recents);
				
				emitter.onComplete();
			}
			
			
		}).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) throws Exception {
					
					}
				}, new Consumer<Throwable>() {
					
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e("ERROR", throwable.getMessage());
					}
				}, new Action() {
					@Override
					public void run() throws Exception {
					
					}
				});
		
		compositeDisposable.add(disposable);
		
	}
	
	private void setRoomDatabase() {
		compositeDisposable = new CompositeDisposable();
		LocalDatabase database = LocalDatabase.getInstance(this);
		recentRepository = RecentRepository.getInstance(RecentsDataSource.getInstance(database.recentsDAO()));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case Constants.REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					AlertDialog alertDialog = new SpotsDialog(ViewWallpaperActivity.this);
					alertDialog.show();
					alertDialog.setMessage("Please wait");
					
					String fileName = UUID.randomUUID().toString() + ".png";
					Picasso.get()
							.load(Constants.select_background.getImageLink())
							.into(new SaveImageHelper(
									getBaseContext(),
									alertDialog,
									getApplicationContext().getContentResolver(),
									fileName,
									"Wallpaper App"));
				} else {
					Toast.makeText(getApplicationContext(), "you edd to accept this permission to download this image", Toast.LENGTH_SHORT).show();
				}
		}
	}
	
	@Override
	protected void onDestroy() {
		
		Picasso.get().cancelRequest(target);
		compositeDisposable.clear();
		
		super.onDestroy();
	}
}

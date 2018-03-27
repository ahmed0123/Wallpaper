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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.wallpaper.Helper.SaveImageHelper;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class ViewWallpaperActivity extends AppCompatActivity {
	
	CoordinatorLayout coordinatorLayout;
	CollapsingToolbarLayout collapsingToolbarLayout;
	FloatingActionButton wallpaperFab, downloadFab;
	ImageView imageView;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_wallpaper);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		coordinatorLayout = findViewById(R.id.rootLayout);
		collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
		collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
		
		collapsingToolbarLayout.setTitle(Constants.CATEGORY_NAME);
		imageView = findViewById(R.id.image_thumb);
		
		Picasso.get()
				.load(Constants.select_background.getImageLink())
				.into(imageView);
		
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
		
		super.onDestroy();
	}
}

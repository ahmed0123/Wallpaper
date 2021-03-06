package com.example.android.wallpaper.Activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wallpaper.Adapter.MyFragmentAdapter;
import com.example.android.wallpaper.R;
import com.example.android.wallpaper.Utils.Constants;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	
	NavigationView navigationView;
	ViewPager viewPager;
	TabLayout tabLayout;
	DrawerLayout drawer;
	Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("Wallpaper");
		setSupportActionBar(toolbar);
		
		//set navigation view
		setNavDrawer();
		
		//check if not sign-in then navigate to sign up screen
		checkLoginStatus();
		
		// check for write external permission if not then request permission
		checkPermissionStatus();
		
		//set up view pager with tablayout
		setViewPager();
		
		
		loadUserInformation();
		
	}
	
	private void loadUserInformation() {
		if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			View headerView = navigationView.getHeaderView(0);
			TextView emailText = headerView.findViewById(R.id.txt_email);
			emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
		}
	}
	
	private void setViewPager() {
		viewPager = findViewById(R.id.viewpager);
		MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), this);
		viewPager.setAdapter(adapter);
		
		tabLayout = findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);
	}
	
	private void setNavDrawer() {
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
	}
	
	private void checkPermissionStatus() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_CODE);
			}
		}
	}
	
	private void checkLoginStatus() {
		
		if (FirebaseAuth.getInstance().getCurrentUser() == null) {
			
			startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), Constants.SIGN_IN_REQUEST_CODE);
			
		} else {
			
			Snackbar.make(drawer, new StringBuilder("Welcome")
					.append(FirebaseAuth.getInstance().getCurrentUser().getEmail())
					.toString(), Snackbar.LENGTH_SHORT).show();
			
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == Constants.SIGN_IN_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
				Snackbar.make(drawer, new StringBuilder("Welcome")
						.append(FirebaseAuth.getInstance().getCurrentUser().getEmail())
						.toString(), Snackbar.LENGTH_SHORT).show();
				
				checkPermissionStatus();
				
				setViewPager();
				
				loadUserInformation();
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case Constants.REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "you edd to accept this permission to download this image", Toast.LENGTH_SHORT).show();
				}
		}
	}
	
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_camera) {
		
		}
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}

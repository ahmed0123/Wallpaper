package com.example.android.wallpaper.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.android.wallpaper.R;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//splash screen
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
			startActivity(new Intent(MainActivity.this,HomeActivity.class));
			finish();
			}
		}, 5000);// wait for 3 second
	}
}

package com.kaist.crescendo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.kaist.crescendo.R;

public class IntroActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.activity_intro);
		
		Handler handler = new Handler();
		handler.postDelayed(run, 3000);
	}
	
	Runnable run = new Runnable() {
		
		@Override
		public void run() {
			
			Intent intent = new Intent();
			startActivity(intent.setClass(getApplicationContext(), MainActivity.class));
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed(); // it's intended
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		ImageView img = (ImageView)findViewById(R.id.imageanimation);
		AnimationDrawable frameAnimation =    (AnimationDrawable)img.getDrawable();
		frameAnimation.setCallback(img);
		frameAnimation.setVisible(true, true);
		frameAnimation.start();
	}
}

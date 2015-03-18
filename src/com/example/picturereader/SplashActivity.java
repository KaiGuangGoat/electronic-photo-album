package com.example.picturereader;

import com.example.picturereader.ui.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SplashActivity extends Activity{
	AppApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (AppApplication) getApplication();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(app.getResLayout("splash"));
		
		ImageView splash = (ImageView) findViewById(app.getResId("iv_splash"));
		Animation anim = new AlphaAnimation(0.2f, 1.0f);
		anim.setDuration(3000);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation paramAnimation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation paramAnimation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation paramAnimation) {
				// TODO Auto-generated method stub
				gotoMainActivity();
				
				finish();
			}
		});
		splash.setAnimation(anim);
	}
	
	private void gotoMainActivity(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}

}

package com.example.picturereader.ui;

import com.example.picturereader.AppApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public abstract class BaseActivity extends FragmentActivity{
	AppApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		app = (AppApplication) getApplication();
		init();
	}
	
	protected abstract void init();
}

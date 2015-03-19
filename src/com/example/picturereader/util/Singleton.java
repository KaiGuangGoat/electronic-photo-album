package com.example.picturereader.util;

import java.util.List;

import com.example.picturereader.AppApplication;
import com.example.picturereader.entity.ImageFolder;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class Singleton {
	
	private ImageFolder mImageFolder;
	
	private List<String> musicList;
	
	private AppApplication app;
	
//	Handler handler;
	private static class SingletonHolder{
		private static final Singleton instance = new Singleton();
	}
	
	private Singleton(){}
	
	public static Singleton getInstance(){
		return SingletonHolder.instance;
	}

	public ImageFolder getmImageFolder() {
		return mImageFolder;
	}

	public void setmImageFolder(ImageFolder mImageFolder) {
		this.mImageFolder = mImageFolder;
	}
	
	public List<String> getMusicList() {
		return musicList;
	}

	public void setMusicList(List<String> musicList) {
		this.musicList = musicList;
	}



	private OnTouchListener onTouchListener;
	public OnTouchListener getTouchLIstener(){
		if(onTouchListener == null){
			onTouchListener = new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction() == MotionEvent.ACTION_UP){
						v.performClick();
					}
					return false;
				}
			};
		}
		return onTouchListener;
	}
}

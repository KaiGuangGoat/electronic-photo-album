package com.example.picturereader.service;

import com.example.picturereader.R;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicPlayerService extends Service{
	
	private MediaPlayer music;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		music = MediaPlayer.create(getApplicationContext(), R.raw.sleep_away);
		music.setLooping(true);
		music.start();
	}
	
	@Override
	public void onDestroy() {
		music.stop();
		music.release();
		super.onDestroy();
	}
	
}

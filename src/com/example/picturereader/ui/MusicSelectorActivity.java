package com.example.picturereader.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.picturereader.R;
import com.example.picturereader.adapter.MusicSelectorAdapter;
import com.example.picturereader.adapter.MusicSelectorAdapter.MusicCheckBoxListener;
import com.example.picturereader.adapter.PictureSelectorAdapter.CheckBoxListener;
import com.example.picturereader.entity.MusicEntity;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.MediaResolve;
import com.example.picturereader.util.Singleton;
import com.example.picturereader.util.UIHelper;

public class MusicSelectorActivity extends BaseActivity implements MusicCheckBoxListener{

	private ListView musicListView;
	
	private MusicSelectorAdapter adapter;
	
	private ArrayList<MusicEntity> hadSelectedMusic;
	
	private Handler handler;
	
	@Override
	protected void init() {
		setContentView(R.layout.music_selector);
		hadSelectedMusic = new ArrayList<MusicEntity>();
		initHandler();
		getAllMusic();
		initView();
		
	}
	
	private void initHandler(){
		handler = new Handler(getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				adapter = new MusicSelectorAdapter(Singleton.getInstance().getMusicList(), getApplicationContext());
				adapter.setCheckBoxListener(MusicSelectorActivity.this);
				musicListView.setAdapter(adapter);
			}
		};
	}
	
	private void getAllMusic(){
		new Thread(){
			public void run() {
				MediaResolve.getMusic(getApplicationContext());
				handler.sendEmptyMessage(Constant.EMPTY_MSG_0);
			}
		}.start();
	}
	
	private void initView(){
		musicListView = (ListView) findViewById(R.id.lv_music);
	}
	
	public void sure(View view){
		Intent intent = new Intent();
		intent.putExtra(Constant.SELECTED_MUSIC_LIST, hadSelectedMusic);
		setResult(RESULT_OK, intent);
		finish();
	}


	@Override
	public void onChecked(MusicEntity musicEntity, int selectedNum) {
		// TODO Auto-generated method stub
		hadSelectedMusic.add(musicEntity);
	}

	@Override
	public void onCheckedCancle(MusicEntity musicEntity, int selectedNum) {
		// TODO Auto-generated method stub
		hadSelectedMusic.remove(musicEntity);
	}

}

package com.example.picturereader.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.picturereader.R;
import com.example.picturereader.entity.MusicEntity;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.MediaResolve;

public class MergerSettingActivity extends BaseActivity{
	
	private TextView tvimgDuration;
	private TextView tvmusicDuration;
	
	private RadioButton radioButtonImgEnd;
	private RadioButton radioButtonMusicEnd;
	
	private Spinner spinnerMode;
	
	private EditText editTextFileName;
	private EditText editTextFilePath;
	
	private ArrayList<String> imgList;
	private ArrayList<MusicEntity> musicList;
	
	private int sumImgDuration;
	private long sumMusicDuration;
	
	private static final int ONE_IMG_DURATION = 2;

	@Override
	protected void init() {
		setContentView(R.layout.merger_setting_layout);
		initData();
		initView();
	}
	
	private void initData(){
		Intent intent = getIntent();
		imgList = intent.getStringArrayListExtra(Constant.SELECTED_IMG_LIST);
		musicList = intent.getParcelableArrayListExtra(Constant.SELECTED_MUSIC_LIST);
		
		if(imgList != null){
			sumImgDuration = imgList.size()*ONE_IMG_DURATION;
		}
		
		if(musicList != null){
			for(MusicEntity music:musicList){
				sumMusicDuration += music.getDuration();
			}
		}
		
	}
	
	private void initView() {
		tvimgDuration = (TextView) findViewById(R.id.tv_img_duration);
		tvmusicDuration = (TextView) findViewById(R.id.tv_music_duration);

	}
	
	public void cancle(View view){
		
	}
	
	public void begin(View view){
		
	}

}

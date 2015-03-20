package com.example.picturereader.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.picturereader.R;
import com.example.picturereader.adapter.GridViewShowPicAdapter;
import com.example.picturereader.adapter.ListViewShowMusicAdapter;
import com.example.picturereader.adapter.MusicSelectorAdapter;
import com.example.picturereader.entity.MusicEntity;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.UIHelper;

public class MainLayoutActivity extends BaseActivity implements OnClickListener{
	
	private Button addMusic;
	private Button addPicture;
	private Button mergerVideo;
	
	private GridView gvShowPicture;
	private ListView lvShowMusic;
	
	private GridViewShowPicAdapter showPicAdapter;
	private ListViewShowMusicAdapter showMusicAdapter;
	
	private ArrayList<String> imgSelectedList;
	private ArrayList<MusicEntity> musicSelectList;

	@Override
	protected void init() {
		setContentView(R.layout.main);
		imgSelectedList = new ArrayList<String>();
		musicSelectList = new ArrayList<MusicEntity>();
		initView();
		
		
	}
	
	private void initView(){
		addMusic = (Button) findViewById(R.id.btn_add_music);
		addPicture = (Button) findViewById(R.id.btn_add_picture);
		mergerVideo = (Button) findViewById(R.id.btn_merger_video);
		addMusic.setOnClickListener(this);
		addPicture.setOnClickListener(this);
		mergerVideo.setOnClickListener(this);
		
		gvShowPicture = (GridView) findViewById(R.id.gv_picture);
		lvShowMusic = (ListView) findViewById(R.id.lv_music);
		
		showPicAdapter = new GridViewShowPicAdapter(imgSelectedList, this);
		gvShowPicture.setAdapter(showPicAdapter);
		
		showMusicAdapter = new ListViewShowMusicAdapter(musicSelectList, this);
		lvShowMusic.setAdapter(showMusicAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode,data);
		if(resultCode == RESULT_OK){
			if(requestCode == Constant.FOR_RESULT_SELECT_PICTURE){
				ArrayList<String> imgList = data.getStringArrayListExtra(Constant.SELECTED_IMG_LIST);
				for(String image:imgList){
					if(!imgSelectedList.contains(image)){
						imgSelectedList.add(image);
					}
				}
				gvShowPicture.setAdapter(showPicAdapter);
			}else if(requestCode == Constant.FOR_RESULT_SELECT_MUSIC){
				ArrayList<MusicEntity> musicList = data.getParcelableArrayListExtra(Constant.SELECTED_MUSIC_LIST);
				for(MusicEntity music:musicList){
					if(!musicSelectList.contains(music)){
						musicSelectList.add(music);
					}
				}
				lvShowMusic.setAdapter(showMusicAdapter);
			}
			
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_music:
			UIHelper.gotoActivityForresult(this, MusicSelectorActivity.class, Constant.FOR_RESULT_SELECT_MUSIC);
			break;
			
		case R.id.btn_add_picture:
			UIHelper.gotoActivityForresult(this, PictureSelectorActivity.class,Constant.FOR_RESULT_SELECT_PICTURE);
			break;
			
		case R.id.btn_merger_video:
			Intent intent = new Intent(this,MergerSettingActivity.class);
			intent.putExtra(Constant.SELECTED_IMG_LIST, imgSelectedList);
			intent.putExtra(Constant.SELECTED_MUSIC_LIST, musicSelectList);
			startActivity(intent);
			break;

		default:
			
			break;
		}
		
	}

}

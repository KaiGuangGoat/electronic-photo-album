package com.example.picturereader.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.picturereader.R;
import com.example.picturereader.entity.MusicEntity;
import com.example.picturereader.util.Constant;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

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
	
	private String[] spinnerModeData;
	
	private int sumImgDuration;
	private int sumMusicDuration;
	
	private String imgDurationFormat;
	private String musicDurationFormat;
	
	private String videoFormat = "3gp";
	private String picturePath;
	private String ffmpeg_cmd;
	
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
		
		spinnerModeData = new String[]{"标准MP4格式","3gp格式","mpeg格式","avi格式"};
		
		if(imgList != null){
			sumImgDuration = imgList.size()*ONE_IMG_DURATION;
			imgDurationFormat = showTimeFormat(sumImgDuration) + "(共" + imgList.size() +"张图片)";
		}
		
		if(musicList != null){
			for(MusicEntity music:musicList){
				sumMusicDuration += music.getDuration()/1000;
			}
			musicDurationFormat = showTimeFormat(sumMusicDuration)+"(共" + musicList.size() +"首音乐)";
		}
		
	}
	
	private void initView() {
		tvimgDuration = (TextView) findViewById(R.id.tv_img_duration);
		tvmusicDuration = (TextView) findViewById(R.id.tv_music_duration);
		
		radioButtonImgEnd = (RadioButton) findViewById(R.id.rb_img_end);
		radioButtonMusicEnd = (RadioButton) findViewById(R.id.rb_music_end);
		
		spinnerMode = (Spinner) findViewById(R.id.sp_select_mode);
		
		editTextFileName = (EditText) findViewById(R.id.et_file_name);
		editTextFilePath = (EditText) findViewById(R.id.et_output_path);
		
		tvimgDuration.setText(tvimgDuration.getText().toString() + imgDurationFormat);
		tvmusicDuration.setText(tvmusicDuration.getText().toString() + musicDurationFormat);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerModeData);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMode.setAdapter(adapter);
		spinnerMode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					videoFormat = "mp4";
					break;
				case 1:
					videoFormat = "3gp";
					break;
				case 2:
					videoFormat = "mpeg";
					break;
				case 3:
					videoFormat = "avi";
					break;

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void cancle(View view){
		finish();
	}
	
	public void begin(View view){
		if(TextUtils.isEmpty(editTextFileName.getText())){
			app.toast("文件名不能为空");
			return;
		}
		splitFfmpegCmd();
		FFmpeg ffmpeg = FFmpeg.getInstance(this);
		try {
			ffmpeg.loadBinary(new LoadBinaryResponseHandler(){
				@Override
                public void onFailure() {
                    app.toast("load binary response fail!");
                }
			});
			ffmpeg.execute(ffmpeg_cmd, new FFmpegExecuteResponseHandler() {
				
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onSuccess(String success) {
					app.toast("合成成功!");
					Log.e("MergerSettingActivity", "success msg:"+success);
				}
				
				@Override
				public void onProgress(String progress) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFailure(String fail) {
					// TODO Auto-generated method stub
					app.toast("合成失败");
					Log.e("MergerSettingActivity", "failue msg:"+fail);
				}
			});
		} catch (FFmpegCommandAlreadyRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FFmpegNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// -f image2pipe -r 10 -vcodec mjpeg -i /tmp/my_fifo -vcodecmpeg4 video.avi
	private void splitFfmpegCmd(){
		String filePath = "";
		if(!TextUtils.isEmpty(editTextFilePath.getText())){
			filePath = editTextFilePath.getText() + File.separator;
		}
		String videoPath = app.getSDCardDir() +File.separator+filePath+editTextFileName.getText().toString() +"."+videoFormat;
		ffmpeg_cmd = "-f image2pipe -r 0.5"+" -vcodec mjpeg -i "+app.getTempDir()+"/img-.jpg -vcodec mpeg4 "+ videoPath;
	}
	
	private String showTimeFormat(int time){
		if(time<60){
			return time+"秒";
		}else{
			int minu = time/60;
			if(minu<60){
				return minu +"分"+time%60+"秒";
			}else{
				int hour = minu/60;
				return hour+"小时"+ minu +"分" + time/3600 +"秒";
			}
		}
	}

}

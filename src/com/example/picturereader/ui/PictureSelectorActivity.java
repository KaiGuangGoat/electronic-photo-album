package com.example.picturereader.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.adapter.PictureSelectorAdapter;
import com.example.picturereader.adapter.PictureSelectorAdapter.CheckBoxListener;
import com.example.picturereader.adapter.PictureSelectorMoreAdapter;
import com.example.picturereader.adapter.PictureSelectorMoreAdapter.PictureSelectorMoreListener;
import com.example.picturereader.entity.FolderPath;
import com.example.picturereader.entity.ImageFolder;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.FileUtil;
import com.example.picturereader.util.ImageLoader;
import com.example.picturereader.util.MediaResolve;
import com.example.picturereader.util.Singleton;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PictureSelectorActivity extends BaseActivity implements OnClickListener, CheckBoxListener,PictureSelectorMoreListener{
	static final String TAG = "PictureSelectorActivity";
	private Handler mHanHandler = null;
	private LinearLayout mainView;
	private GridView gridView;
	private Button sureButton;
	private Button allImgButton;
	private PopupWindow selectMorePopWin;
	private int select_img_max_size;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mHanHandler = new Handler(getMainLooper()){
			public void handleMessage(android.os.Message msg) {
				if(msg.what == Constant.EMPTY_MSG_0){
					adaptImgSelector(Singleton.getInstance().getmImageFolder().getMaxOfImgFolderPath());
				}
				if(msg.what == Constant.EMPTY_MSG_1){
					PictureSelectorAdapter adapter = new PictureSelectorAdapter(fileList, PictureSelectorActivity.this);
					if(select_img_max_size != 0){
						adapter.MAX_SELECTED_NUM = select_img_max_size;
					}
					adapter.setCheckBoxListener(PictureSelectorActivity.this);
					adapter.setIsLimitMax(false);//不限制选择图片的张数
					gridView.setAdapter(adapter);
					gridView.setOnScrollListener(new OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							// TODO Auto-generated method stub
							switch(scrollState){
							case OnScrollListener.SCROLL_STATE_FLING:
								ImageLoader.getInstance().lock();
								break;
							case OnScrollListener.SCROLL_STATE_IDLE:
								ImageLoader.getInstance().unLock();
								break;
							case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
								ImageLoader.getInstance().lock();
								break;
							}
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							
						}
						
						
					});
				}
			}
		};
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void init() {
		setContentView(R.layout.picture_selector_layout);
		select_img_max_size = getIntent().getIntExtra(Constant.SELECT_IMGS_MAX_SIZE, 0);
		mainView = (LinearLayout) findViewById(R.id.ly_select_picture_main_layout);
		gridView = (GridView) findViewById(R.id.gv_show_picture);
		sureButton = (Button) findViewById(R.id.bt_select_sure);
		allImgButton = (Button) findViewById(R.id.bt_all_image);
		ImageLoader.getInstance().init(this);
		
		sureButton.setOnClickListener(this);
		allImgButton.setOnClickListener(this);
		
		runGetAllImgFolderPath();
	}
	
	private void runGetAllImgFolderPath(){
		new Thread(){
			public void run() {
				MediaResolve.getAllImgFolderPath(getApplicationContext());
				mHanHandler.sendEmptyMessage(Constant.EMPTY_MSG_0);
			}
		}.start();
	}
	
	private List<File> fileList;
	private void adaptImgSelector(final String parentFolderPath){
		new Thread(){
			@Override
			public void run() {
				fileList = FileUtil.fileList(parentFolderPath,true, new String[]{".jpg",".jpeg",".png"});
				mHanHandler.sendEmptyMessage(Constant.EMPTY_MSG_1);
			}
		}.start();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_select_sure:
			forResult();
			finish();
			break;
		case R.id.bt_all_image:
			if(selectMorePopWin == null){
				createPopWindow();
			}
			showPopWindow();
			break;
		default:
			break;
		}
		
	}
	
	private void forResult(){
		Intent intent = new Intent();
		intent.putStringArrayListExtra(Constant.SELECTED_IMG_LIST, selectedImgList);
		setResult(RESULT_OK, intent);
	}
	
	private void createPopWindow(){
		View view = LayoutInflater.from(this).inflate(R.layout.selector_more_pic, null);
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		ListView listView = (ListView) view.findViewById(R.id.lv_picture_folder);
		PictureSelectorMoreAdapter adapter = new PictureSelectorMoreAdapter(
				Singleton.getInstance().getmImageFolder().getFolderPaths(), this);
		adapter.setListener(this);
		listView.setAdapter(adapter);
		selectMorePopWin = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		selectMorePopWin.setTouchable(true);
		selectMorePopWin.setOutsideTouchable(true);
		view.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_BACK){
					selectMorePopWin.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	private void showPopWindow(){
		selectMorePopWin.showAtLocation(mainView, Gravity.BOTTOM, 0, 0);
	}

	private ArrayList<String> selectedImgList = new ArrayList<String>();
	@Override
	public void onChecked(String imgFilePath,int selectedNum) {
		// TODO Auto-generated method stub
//		Log.e(TAG, "imgSelectPath:"+imgFilePath);
		selectedImgList.add(imgFilePath);
		sureButton.setText("确定("+selectedNum+")");
	}

	@Override
	public void onCheckedCancle(String imgFilePath,int selectedNum) {
		// TODO Auto-generated method stub
		if(selectedImgList.contains(imgFilePath)){
			selectedImgList.remove(imgFilePath);
			String sure = "确定("+selectedNum+")";
			if(selectedNum == 0){
				sure = "确定";
			}
			sureButton.setText(sure);
		}
	}

	@Override
	public void onSelected(String folderPath) {
		if(selectMorePopWin != null)
			selectMorePopWin.dismiss();
//		app.toast(folderPath);
		adaptImgSelector(folderPath);
	}

}

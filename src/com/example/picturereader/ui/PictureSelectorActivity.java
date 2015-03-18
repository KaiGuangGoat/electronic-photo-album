package com.example.picturereader.ui;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.adapter.PictureSelectorAdapter;
import com.example.picturereader.adapter.PictureSelectorAdapter.ImageClickListener;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.FileUtil;
import com.example.picturereader.util.ImageLoader;

import android.content.Intent;
import android.os.Handler;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

public class PictureSelectorActivity extends BaseActivity implements ImageClickListener{
	
	private Handler handler = null;
	
	private String folderPath;
	
	private List<File> fileList;
	
	private GridView gridView;
	@Override
	protected void init() {
		setContentView(R.layout.picture_selector_layout);
		initHandler();
		Intent intent = getIntent();
		folderPath = intent.getStringExtra(Constant.FOLDER_FILES);
		
		gridView = (GridView) findViewById(R.id.gv_show_picture);
		new Thread(){
			@Override
			public void run() {
				fileList = FileUtil.fileList(folderPath, true, new String[]{".jpg",".png",".bmp",".jpeg"});
				handler.sendEmptyMessage(Constant.EMPTY_MSG_1);
			}
		}.start();
	}
	
	private void initHandler() {
		handler = new Handler(getMainLooper()) {
			public void handleMessage(android.os.Message msg) {
				
				if (msg.what == Constant.EMPTY_MSG_1) {
					PictureSelectorAdapter adapter = new PictureSelectorAdapter(
							fileList, PictureSelectorActivity.this);
					adapter.setImageClickListener(PictureSelectorActivity.this);
					gridView.setAdapter(adapter);
					gridView.setOnScrollListener(new OnScrollListener() {

						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// TODO Auto-generated method stub
							switch (scrollState) {
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
						public void onScroll(AbsListView view,
								int firstVisibleItem, int visibleItemCount,
								int totalItemCount) {
							// TODO Auto-generated method stub

						}

					});
				}
			}
		};
	}

	@Override
	public void onImageClick(int position) {
		Intent intent = new Intent(this,GalleryViewActivity.class);
		intent.putExtra(Constant.GALLERY_SHOW_POSITION, position);
		intent.putExtra(Constant.FOLDER_FILES, folderPath);
		startActivity(intent);
	}

}

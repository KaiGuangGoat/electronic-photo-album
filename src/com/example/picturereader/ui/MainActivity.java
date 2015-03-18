package com.example.picturereader.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.R.layout;
import com.example.picturereader.adapter.MainPictureSelectorAdapter;
import com.example.picturereader.adapter.MainPictureSelectorAdapter.MainPictureSelectorListener;
import com.example.picturereader.entity.FolderPath;
import com.example.picturereader.entity.ImageFolder;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.ImageLoader;
import com.example.picturereader.util.Singleton;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;

public class MainActivity extends BaseActivity implements MainPictureSelectorListener{
	private ListView listView;
	private MainPictureSelectorAdapter adapter;
	private Handler handler;
	private ProgressBar pb_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		handler = new Handler(getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == Constant.EMPTY_MSG_0) {
					pb_loading.setVisibility(View.INVISIBLE);
					adapter = new MainPictureSelectorAdapter(Singleton
							.getInstance().getmImageFolder().getFolderPaths(),
							MainActivity.this);
					adapter.setListener(MainActivity.this);
					listView.setAdapter(adapter);
				}
			}
		};
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void init() {
		setContentView(R.layout.activity_main);
		
		getAllImgFolderPath();
		ImageLoader.getInstance().init(this);
		
		listView = (ListView) findViewById(R.id.lv_picture_folder);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		pb_loading.setVisibility(View.VISIBLE);
		
		new Thread() {
			@Override
			public void run() {
				getAllImgFolderPath();
				handler.sendEmptyMessage(Constant.EMPTY_MSG_0);
			}
		}.start();

	}

	private void getAllImgFolderPath() {

		if (Singleton.getInstance().getmImageFolder() != null
				&& Singleton.getInstance().getmImageFolder().hasLoad()) {
			return;
		}

		int totalImgs = 0;
		int maxOfFolderImgsCount = 0;
		String maxOfImgFolderPath = null;

		ContentResolver resolver = getContentResolver();
		Uri imgUri = Media.EXTERNAL_CONTENT_URI;
		Cursor mCursor = resolver.query(imgUri, null, Media.MIME_TYPE
				+ "= ? or " + Media.MIME_TYPE + "= ? ", new String[] {
				"image/jpeg", "image/png" }, null);

		ImageFolder imageFolder = new ImageFolder();
		List<FolderPath> folderPaths = new ArrayList<FolderPath>();
		List<String> flags = new ArrayList<String>();
		while (mCursor.moveToNext()) {
			String imgPath = mCursor.getString(mCursor
					.getColumnIndex(Media.DATA));
			String folderPath = new File(imgPath).getParent();
			if (flags == null || flags.contains(folderPath)) {
				continue;
			}
			File parentFile = new File(folderPath);
			flags.add(folderPath);
			String[] fileList = parentFile.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String filename) {
					if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")
							|| filename.endsWith(".png")) {
						return true;
					}
					return false;
				}
			});
			FolderPath folderPathEntity = new FolderPath(folderPath, folderPath
					+ File.separator + fileList[0]);
			folderPaths.add(folderPathEntity);
			int size = fileList.length;
			totalImgs += size;
			if (size > maxOfFolderImgsCount) {
				maxOfFolderImgsCount = size;
				maxOfImgFolderPath = parentFile.getAbsolutePath();
			}
		}
		mCursor.close();
		imageFolder.setMaxOfFolderImgsCount(maxOfFolderImgsCount);
		imageFolder.setMaxOfImgFolderPath(maxOfImgFolderPath);
		imageFolder.setTotalImgs(totalImgs);
		imageFolder.setFolderPaths(folderPaths);
		imageFolder.setHasLoad(true);
		Singleton.getInstance().setmImageFolder(imageFolder);
	}

	@Override
	public void onSelected(String folderPath) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,PictureSelectorActivity.class);
		intent.putExtra(Constant.FOLDER_FILES, folderPath);
		startActivity(intent);
	}

}

package com.example.picturereader.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.picturereader.R;
import com.example.picturereader.service.MusicPlayerService;
import com.example.picturereader.thread.RoundTurnThread;
import com.example.picturereader.util.BitmapUtil;
import com.example.picturereader.util.Constant;
import com.example.picturereader.util.FileUtil;
import com.example.picturereader.util.ImageLoader;

public class GalleryViewActivity extends BaseActivity {

	private String folderFile;// 展示的图片的父目录

	private ViewPager viewPage;// 展示图片的控件

	private LinearLayout linearLayout_detail;

	private ImageView imageView_detail;// 展示对图片的缩放

	private int position;// 点击到的第几张图片开始进行展示

	private List<File> dataList;// 当前目录中所有图片的路径

	private RoundTurnThread roundTurnThread;// 负责轮转播放的线程

	private Handler handler;
	
	private Intent musicServiceIntent;//跳转到服务发Intent

	@Override
	protected void init() {
		startBackMusic();
		
		initHandler();
		
		setContentView(R.layout.gallery_view_layout);
		viewPage = (ViewPager) findViewById(R.id.vp_image_show);
		linearLayout_detail = (LinearLayout) findViewById(R.id.ly_img_show_detail);

		folderFile = getIntent().getStringExtra(Constant.FOLDER_FILES);
		position = getIntent().getIntExtra(Constant.GALLERY_SHOW_POSITION, 0);

		setViewPage();
		setImageViewDetail();
		roundTurnThread = new RoundTurnThread(handler);
		roundTurnThread.start();
	}
	
	private void initHandler(){
		handler = new Handler(getMainLooper()) {
			public void handleMessage(android.os.Message msg) {
				int currentPage = viewPage.getCurrentItem();
				if (currentPage >= dataList.size() - 1) {
					currentPage = 0;
				} else {
					currentPage++;
				}
				viewPage.setCurrentItem(currentPage);
			}
		};
	}

	private long lastDownTimestamp;// 用于双击时
	private float lastY;
	private float lastX;
	// 上一次移动偏移的量
	private int scrollXLast;
	private int scrollYLast;

	@SuppressLint("NewApi")
	/**
	 * 对展示图片进行设置：缩放，双击，滑动
	 */
	private void setImageViewDetail() {
		imageView_detail = (ImageView) findViewById(R.id.iv_image_show_detail);
		imageView_detail.setImageBitmap(BitmapUtil.getBitmapByOptScale(
				app.getScreenWidth(), app.getScreenWidth(),
				dataList.get(position)));
		imageView_detail.setOnTouchListener(new OnTouchListener() {
			final ScaleGestureDetector scaleGesture = new ScaleGestureDetector(
					GalleryViewActivity.this, new ScaleListener());

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getPointerCount() == 2) {// 图片进行缩放
					scaleImageView(scaleGesture, event);
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					doubleClick();
					lastX = event.getX();
					lastY = event.getY();
					scrollXLast = imageView_detail.getScrollX();
					scrollYLast = imageView_detail.getScrollY();
					break;
				case MotionEvent.ACTION_MOVE:// 拖动图片
					int deltaX = (int) (lastX - event.getX()) + scrollXLast;
					int deltaY = (int) (lastY - event.getY()) + scrollYLast;
					if (imageView_detail.getWidth() * scaleFactor <= app
							.getScreenWidth()) {
						deltaX = 0;
					}
					if (imageView_detail.getHeight() * scaleFactor <= app
							.getScreenHeight()) {
						deltaY = 0;
					}
					imageView_detail.scrollTo(deltaX, deltaY);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	// 处理双击回复原状
	private void doubleClick() {
		if (System.currentTimeMillis() - lastDownTimestamp < 300) {
			recoveryRoundTurn();
		} else
			lastDownTimestamp = System.currentTimeMillis();
	}
	
	private void recoveryRoundTurn(){
		viewPage.setVisibility(View.VISIBLE);
		scaleFactor = 1.0f;
		linearLayout_detail.setVisibility(View.GONE);
		lastDownTimestamp = 0;
		// 还原图片scroll偏移量
		scrollXLast = linearLayout_detail.getScrollX();
		scrollYLast = linearLayout_detail.getScrollY();
		//设置可以进行自动轮转
		roundTurnThread.setRoundTurn(true);
	}

	private void scaleImageView(ScaleGestureDetector scaleGesture,
			MotionEvent event) {
		mode = IMAGEVIEW_SCALE_MODE;
		scaleGesture.onTouchEvent(event);
		scaleView();
	}

	private void setViewPage() {
		getDataList();
		GalleryShowAdapter adapter = new GalleryShowAdapter();
		viewPage.setAdapter(adapter);
		viewPage.setCurrentItem(position);
		viewPage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int currentPage) {
				imageView_detail.setImageBitmap(BitmapUtil.getBitmapByOptScale(
						app.getScreenWidth(), app.getScreenWidth(),
						dataList.get(currentPage)));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void getDataList() {
		dataList = FileUtil.fileList(folderFile, true, new String[] { ".jpg",
				".png", ".bmp", ".jpeg" });
	}

	class GalleryShowAdapter extends PagerAdapter {
		Map<String, ImageView> imageViewMap;

		public GalleryShowAdapter() {
			imageViewMap = new HashMap<String, ImageView>();
		}

		@Override
		public int getCount() {
			if (dataList == null) {
				return 0;
			}
			return dataList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = null;
			File file = dataList.get(position);
			String key = file.getAbsolutePath();
			if (imageViewMap.containsKey(key)) {
				view = imageViewMap.get(key);
			} else {
				view = new ImageView(app);
				view.setImageBitmap(BitmapUtil.getBitmapByOptScale(
						app.getScreenWidth(), app.getScreenWidth(), file));
				imageViewMap.put(key, view);
			}
			setView(view);
			viewPage.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			File file = dataList.get(position);
			String key = file.getAbsolutePath();
			viewPage.removeView(imageViewMap.get(key));
			imageViewMap.remove(key);
		}

	}

	/**
	 * 对图片进行手势缩放
	 * 
	 * @param view
	 */
	private void setView(ImageView view) {
		final ScaleGestureDetector scaleGesture = new ScaleGestureDetector(
				this, new ScaleListener());
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mode = PAGERVIEW_VIEW_MODE;
				scaleGesture.onTouchEvent(event);
				scaleView();
				return true;
			}
		});
	}

	private float scaleFactor = 1.0f;// 图片缩放因子
	private static final int PAGERVIEW_VIEW_MODE = 0;// 浏览图片模式
	private static final int IMAGEVIEW_SCALE_MODE = 1;// 缩放图片模式
	private int mode; // 图片的浏览模式

	/**
	 * 缩放手势监听类
	 * 
	 * @author Administrator
	 * 
	 */
	class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			scaleFactor *= detector.getScaleFactor();
			scaleFactor = Math.max(0.1f, Math.min(5.0f, scaleFactor));
			return false;
		}
	}

	/**
	 * 对图片进行放大和缩小
	 */
	@SuppressLint("NewApi")
	private void scaleView() {

		if (mode == PAGERVIEW_VIEW_MODE && scaleFactor != 1.0f) {
			viewPage.setVisibility(View.INVISIBLE);
			linearLayout_detail.setVisibility(View.VISIBLE);
			//设置不可自动轮转
			roundTurnThread.setRoundTurn(false);
			mode = IMAGEVIEW_SCALE_MODE;
		}
		if (mode == IMAGEVIEW_SCALE_MODE) {
			if (scaleFactor == 1.0f) {
				linearLayout_detail.setVisibility(View.GONE);
				viewPage.setVisibility(View.VISIBLE);
				mode = PAGERVIEW_VIEW_MODE;
			}
			imageView_detail.setScaleX(scaleFactor);
			imageView_detail.setScaleY(scaleFactor);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(scaleFactor != 1.0f){
				recoveryRoundTurn();
				return false;
			}else{
				roundTurnThread.setFinish(true);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void startBackMusic(){
		musicServiceIntent = new Intent(this,MusicPlayerService.class);
		startService(musicServiceIntent);
	}
	
	private void stopBackMusic(){
		stopService(musicServiceIntent);
	}
	
	@Override
	protected void onDestroy() {
		stopBackMusic();
		super.onDestroy();
		
	}
}

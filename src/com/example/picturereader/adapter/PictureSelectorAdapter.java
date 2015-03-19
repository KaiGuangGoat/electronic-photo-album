package com.example.picturereader.adapter;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.util.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PictureSelectorAdapter extends MyBaseAdapter{
	
	private int selectedNum = 0;
	public int MAX_SELECTED_NUM = 9;
	private boolean isLimitMax = true;
	
	private CheckBoxListener listener;

	public PictureSelectorAdapter(List<File> filePaths, Context context) {
		super(filePaths, context);
	}
	
	public void setCheckBoxListener(CheckBoxListener listener){
		this.listener = listener;
	}
	
	public void setIsLimitMax(boolean isLimitMax){
		this.isLimitMax = isLimitMax;
	}
	
	private void clickImageView(final ImageView imageView,final CheckBox checkBox){
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkBox.setChecked(!checkBox.isChecked());
			}
		});
	}
	
	private void checkBoxListener(final ImageView imageView,final CheckBox checkBox,final String path){
//		checkBox.setBackgroundColor(Color.parseColor("#77000000"));
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(listener == null){
					return ;
				}
				if(isChecked){
					selectedNum ++;
					if(selectedNum > MAX_SELECTED_NUM && isLimitMax){
						selectedNum = MAX_SELECTED_NUM; 
						checkBox.setChecked(false);
						app.toast("最多只能够选"+MAX_SELECTED_NUM+"张");
						return;
					}
					listener.onChecked(path,selectedNum);
					imageView.setColorFilter(Color.parseColor("#77000000"));//点击图片变暗
				}else{
					selectedNum --;
					if(selectedNum <0){
						selectedNum = 0;
					}
					listener.onCheckedCancle(path,selectedNum);
					imageView.setColorFilter(null);//恢复
				}
				
			}
		});
	}
	
	
	
	public static class ViewHolder extends BaseViewHolder{
		ImageView imageView;
		public CheckBox checkBox;
	}
	
	public interface CheckBoxListener{
		public void onChecked(String imgFilePath,int selectedNum);
		public void onCheckedCancle(String imgFilePath,int selectedNum);
	}

	@Override
	protected View getConvertView() {
		return inflater.inflate(R.layout.picture_selector_layout_item, null,false);
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);
		viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_select_picture_item);
		return viewHolder;
	}

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		clickImageView(viewHolder.imageView, viewHolder.checkBox);
		String path = ((File)dataList.get(position)).getAbsolutePath();
		checkBoxListener(viewHolder.imageView,viewHolder.checkBox,path);
		ImageLoader.getInstance().addTask(path, viewHolder.imageView);
	}
}

package com.example.picturereader.adapter;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.util.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PictureSelectorAdapter extends MyBaseAdapter{
	
	public PictureSelectorAdapter(List<File> filePaths, Context context) {
		super(filePaths, context);
	}
	
	@Override
	protected View getConvertView() {
		// TODO Auto-generated method stub
		View convertView = 
				inflater.inflate(R.layout.picture_selector_layout_item, null,false);
		return convertView;
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_img_item);
		return viewHolder;
	}

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		clickImageView(viewHolder.imageView,position);
		String path = ((File)dataList.get(position)).getAbsolutePath();
		ImageLoader.getInstance().addTask(path, viewHolder.imageView);
	}
	
	private void clickImageView(final ImageView imageView,final int position){
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onImageClick(position);
				}
			}
		});
	}
	
	public static class ViewHolder extends BaseViewHolder{
		ImageView imageView;
	}
	
	public interface ImageClickListener{
		public void onImageClick(int position);
	}
	private ImageClickListener listener;
	public void setImageClickListener(ImageClickListener listener){
		this.listener = listener;
	}

}

package com.example.picturereader.adapter;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.entity.FolderPath;
import com.example.picturereader.util.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainPictureSelectorAdapter extends MyBaseAdapter {
	
	private MainPictureSelectorListener listener;

	public MainPictureSelectorAdapter(List<FolderPath> filePaths, Context context) {
		super(filePaths, context);
	}

	@Override
	protected View getConvertView() {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.activity_main_selector_item, null);
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_picture_select_more_item);
		viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_picture_select_more_item);
		return viewHolder;
	} 

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		FolderPath folder = (FolderPath) dataList.get(position);
		viewHolder.textView.setText(new File(folder.getParentPath()).getAbsolutePath());
		ImageLoader.getInstance().addTask(folder.getFirstPath(), viewHolder.imageView);
		final String folderPath = folder.getParentPath();
		viewHolder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onSelected(folderPath);
				}
			}
		});
		viewHolder.textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onSelected(folderPath);
				}
			}
		});
	}
	
	public  class ViewHolder extends BaseViewHolder{
		ImageView imageView;
		TextView textView;
	}
	
	public void setListener(MainPictureSelectorListener listener){
		this.listener = listener;
	}

	public interface MainPictureSelectorListener{
		public void onSelected(String folderPath);
	}

}

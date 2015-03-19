package com.example.picturereader.adapter;

import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.util.ImageLoader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class GridViewShowPicAdapter extends MyBaseAdapter{

	public GridViewShowPicAdapter(List<String> dataList, Context context) {
		super(dataList, context);
	}

	@Override
	protected View getConvertView() {
		
		return inflater.inflate(R.layout.gallery_view_layout_item, null);
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_gallery_item);
		return viewHolder;
	}

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		ImageLoader.getInstance().addTask(dataList.get(position).toString(), viewHolder.imageView);
	}
	
	protected class ViewHolder extends BaseViewHolder{
		ImageView imageView;
	}

}

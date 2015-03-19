package com.example.picturereader.adapter;

import java.util.List;

import com.example.picturereader.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ListViewShowMusicAdapter extends MyBaseAdapter{

	public ListViewShowMusicAdapter(List dataList, Context context) {
		super(dataList, context);
	}

	@Override
	protected View getConvertView() {
		return inflater.inflate(R.layout.listview_show_selected_music_item, null);
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		ViewHolder holderView = new ViewHolder();
		holderView.textView = (TextView) convertView.findViewById(R.id.tv_show_music);
		return holderView;
	}

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		viewHolder.textView.setText(dataList.get(position).toString());
	}
	
	static class ViewHolder extends BaseViewHolder{
		TextView textView;
	}

}

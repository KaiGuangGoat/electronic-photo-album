package com.example.picturereader.adapter;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.entity.MusicEntity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ListViewShowMusicAdapter extends MyBaseAdapter{

	public ListViewShowMusicAdapter(List<MusicEntity> dataList, Context context) {
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
		MusicEntity musicEntity = (MusicEntity) dataList.get(position);
		File file = new File(musicEntity.getMusicPath());
		viewHolder.textView.setText(file.getName());
	}
	
	static class ViewHolder extends BaseViewHolder{
		TextView textView;
	}

}

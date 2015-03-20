package com.example.picturereader.adapter;

import java.io.File;
import java.util.List;

import com.example.picturereader.R;
import com.example.picturereader.entity.MusicEntity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MusicSelectorAdapter extends MyBaseAdapter{
	
	private MusicCheckBoxListener listener;
	private int selectedNum = 0;

	public MusicSelectorAdapter(List<MusicEntity> dataList, Context context) {
		super(dataList, context);
	}
	
	public void setCheckBoxListener(MusicCheckBoxListener listener){
		this.listener = listener;
	}

	@Override
	protected View getConvertView() {
		return inflater.inflate(R.layout.music_selector_item, null);
	}

	@Override
	protected BaseViewHolder initViewHolder(View convertView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_music_name);
		viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_music_select);
		return viewHolder;
	}

	@Override
	protected void setContentView(int position, BaseViewHolder baseViewHolder) {
		MusicEntity music = (MusicEntity) dataList.get(position);
		ViewHolder viewHolder = (ViewHolder) baseViewHolder;
		viewHolder.textView.setText(new File(music.getMusicPath()).getName());
		setTextView(viewHolder.textView, viewHolder.checkBox);
		setCheckBox(viewHolder.checkBox,music);
		
	}
	
	private void setTextView(TextView textView,final CheckBox checkBox){
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkBox.setChecked(!checkBox.isChecked());
			}
		});
	}
	
	private void setCheckBox(CheckBox checkBox,final MusicEntity musicEntity){
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(listener == null)
					return;
				if(isChecked){
					selectedNum++;
					listener.onChecked(musicEntity, selectedNum);
				}else{
					selectedNum--;
					if(selectedNum < 0){
						selectedNum = 0;
					}
					listener.onCheckedCancle(musicEntity, selectedNum);
				}
				
			}
		});
	}
	
	static class ViewHolder extends BaseViewHolder{
		TextView textView;
		CheckBox checkBox;
	}
	
	public interface MusicCheckBoxListener{
		public void onChecked(MusicEntity musicEntity,int selectedNum);
		public void onCheckedCancle(MusicEntity musicEntity,int selectedNum);
	}

}

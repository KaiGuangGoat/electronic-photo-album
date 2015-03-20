package com.example.picturereader.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicEntity implements Parcelable{
	
	private String musicPath;
	private long duration;
	
	public MusicEntity(){
	}

	public String getMusicPath() {
		return musicPath;
	}

	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(musicPath);
		dest.writeLong(duration);
	}
	
	public static final Parcelable.Creator<MusicEntity> CREATOR = new Creator<MusicEntity>() {
		
		@Override
		public MusicEntity[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MusicEntity[size];
		}
		
		@Override
		public MusicEntity createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MusicEntity(source);
		}
	};
	
	public MusicEntity(Parcel source){
		this.musicPath = source.readString();
		this.duration = source.readLong();
	}

}

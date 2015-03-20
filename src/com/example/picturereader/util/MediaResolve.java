package com.example.picturereader.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.picturereader.entity.FolderPath;
import com.example.picturereader.entity.ImageFolder;
import com.example.picturereader.entity.MusicEntity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class MediaResolve {
	
	public static void getAllImgFolderPath(Context context) {

		if (Singleton.getInstance().getmImageFolder() != null
				&& Singleton.getInstance().getmImageFolder().hasLoad()) {
			return;
		}

		int totalImgs = 0;
		int maxOfFolderImgsCount = 0;
		String maxOfImgFolderPath = null;

		ContentResolver resolver = context.getContentResolver();
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
	
	public static void getMusic(Context context){
		if(Singleton.getInstance().getMusicList() != null){
			return;
		}
		List<MusicEntity> musicList = new ArrayList<MusicEntity> ();
		 Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                 null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		  cursor.moveToFirst();
          int counter = cursor.getCount();
          MusicEntity musicEntity = new MusicEntity();
          String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
          long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
          musicEntity.setMusicPath(title);
          musicEntity.setDuration(duration);
          musicList.add(musicEntity);
          for(int j = 0 ; j < counter; j++){
        	  MusicEntity muscEntity = new MusicEntity();
             String musicTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
             long duratio = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
             muscEntity.setMusicPath(musicTitle);
             muscEntity.setDuration(duratio);
             musicList.add(muscEntity);
             cursor.moveToNext();
          }
          Singleton.getInstance().setMusicList(musicList);
          cursor.close();
	}
	
	
	public static void getVideo(Context context){
		
	}
	
}

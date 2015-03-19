package com.example.picturereader.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.example.picturereader.entity.FolderPath;
import com.example.picturereader.entity.ImageFolder;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
		List<String> musicList = new ArrayList<String> ();
		 Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                 null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		  cursor.moveToFirst();
          int counter = cursor.getCount();
          String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
          Log.d("MediaResolve", "music: "+title);
          musicList.add(title);
          for(int j = 0 ; j < counter; j++){
             String musicTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
             musicList.add(musicTitle);
             cursor.moveToNext();
             Log.d("MediaResolve", "music: "+musicTitle);
          }
          Singleton.getInstance().setMusicList(musicList);
          cursor.close();
	}
	
	public static void getVideo(Context context){
		
	}
	
}

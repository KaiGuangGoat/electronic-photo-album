package com.example.picturereader.util;

import android.app.Activity;
import android.content.Intent;

public class UIHelper {
	
	public static void gotoActivityForresult(Activity activity, Class target,int requestCode){
		Intent intent = new Intent(activity,target);
		activity.startActivityForResult(intent, requestCode);
	}
	

}

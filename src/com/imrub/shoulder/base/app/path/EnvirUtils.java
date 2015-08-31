package com.imrub.shoulder.base.app.path;

import android.os.Environment;

public class EnvirUtils {

	public static boolean ExistSDCard() {  
		return Environment.getExternalStorageState().
				equals(Environment.MEDIA_MOUNTED) ? true : false;
	}
}

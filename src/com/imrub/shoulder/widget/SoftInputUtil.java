package com.imrub.shoulder.widget;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.imrub.shoulder.base.app.AppContext;

public class SoftInputUtil {
	
	public static void hideSoftInput(IBinder windowToken){
		InputMethodManager input = (InputMethodManager)AppContext.getAppContext().
				getSystemService(Context.INPUT_METHOD_SERVICE);
		input.hideSoftInputFromWindow(windowToken,InputMethodManager.HIDE_NOT_ALWAYS);		
	}
	
}

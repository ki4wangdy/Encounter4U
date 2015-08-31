package com.imrub.shoulder.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActivityController;
import com.imrub.shoulder.R;

public class Title {

	private View mView;
	
	private ImageView mBackButton;
	private Action<View> mBackListener;
	
	private TextView mTitleContent;
	private TextView mPhotoPickerButton;
	
	public Title(View v){
		this.mView = v;
		initView();
	}
	
	private void initView(){
		mBackButton = (ImageView)mView.findViewById(R.id.title_bg_image);
		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mBackListener != null){
					mBackListener.onExecute(v);
				} else {
					final Context context = ActivityController.getInstance().getCurrentActivity();
					if(context != null && context instanceof Activity){
						((Activity)context).finish();
					}
				}
			}
		});
		mTitleContent = (TextView)mView.findViewById(R.id.title_content);
	}
	
	public void setOnBackListener(final Action<View> action){
		mBackListener = action;
	}
	
	public void setContent(final String content){
		mTitleContent.setText(content);
	}
	
	public void setPhotoPickerButtonVisiable(boolean isVisiable){
		if(mPhotoPickerButton == null){
			mPhotoPickerButton = (TextView)mView.findViewById(R.id.photopicker_use);
		}
		mPhotoPickerButton.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
	}
	
	public void setPhotoPickerButtonName(int resid){
		if(mPhotoPickerButton == null){
			mPhotoPickerButton = (TextView)mView.findViewById(R.id.photopicker_use);
		}
		mPhotoPickerButton.setText(resid);
	}
	
	public void setPhotoPickerButtonListener(final Action<View> listener){
		if(mPhotoPickerButton == null){
			mPhotoPickerButton = (TextView)mView.findViewById(R.id.photopicker_use);
		}
		mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onExecute(v);
			}
		});
	}
	
	public void setRightButton(String name, final Action<View> action){
		TextView button = (TextView)mView.findViewById(R.id.qiuknow_button);
		button.setText(name);
		button.setVisibility(View.VISIBLE);
		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(action != null){
					action.onExecute(v);
				}
			}
		});
	}
	
}

package com.imrub.shoulder.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.ActivityController;
import com.imrub.shoulder.R;

public class DialogFactory {

	private DialogFactory(){
	}

	public static Dialog showLoadingDialog(final String msg , final ActionNull action){
		Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		
		final View currentView = WidgetFactory.createLoadingView();
		dialog.setContentView(currentView);
		
		TextView content = (TextView)currentView.findViewById(R.id.loading_content);
		content.setText(msg);
		
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				dialogInterface.dismiss();
				ImageView image = (ImageView)currentView.findViewById(R.id.loading_image);
				((AnimationDrawable)image.getDrawable()).stop();
				if(action != null){
					action.onExecute();
				}
			}
		});
		dialog.show();
		return dialog;
	}
	
	public static Dialog showLoadingDialog(final String msg){
		return showLoadingDialog(msg, null);
	}
	
	public static void showConfirmDialog(final String title, final String msg1, 
			final String msg2, final ActionNull action){

		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		final View commonDialog = WidgetFactory.createCommonDialog(
				title,
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
					}
				},
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
		);
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createConfirmPhoneView(msg1,msg2);
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
	}
	
	public static void showTwoListviewDialog(final String title, final String msg1, 
			final String msg2, final ActionNull action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		final View commonDialog = WidgetFactory.createTwoListviewCommonDialog(
				title,
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
					}
				},
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
		);
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createTwoListView();
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
		
	}
	
	public static void showNoCancelConfirmDialog(final String title, 
			final String msg, final ActionNull action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		final View commonDialog = WidgetFactory.createNoCancelCommonDialog(
				title,
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
		);
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createOneContentView(msg);
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
	}
	
	public static void showNoTitleConfirmDialog(final String msg, 
			final ActionNull action, final String cancelMsg, final String okMsg){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		final View commonDialog = WidgetFactory.createNoTitleCommonDialog(
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
					}
				},
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
				,cancelMsg, okMsg);
		
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createOneContentView(msg);
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
	}
	
	public static void showNoTitleConfirmDialog(final String msg, 
			final ActionNull action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		final View commonDialog = WidgetFactory.createNoTitleCommonDialog(
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
					}
				},
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
		);
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createOneContentView(msg);
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
	}
	
	public static void showNoTitleNoCancelConfirmDialog(final String msg, 
			final ActionNull action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		final View commonDialog = WidgetFactory.createNoTitleNoCancelCommonDialog(
				new ActionNull() {
					@Override
					public void onExecute() {
						dialog.dismiss();
						if(action != null){
							action.onExecute();
						}
					}
				}
		);
		dialog.setContentView(commonDialog);
		
		View contentView = WidgetFactory.createOneContentView(msg);
		FrameLayout viewGroup = (FrameLayout)commonDialog.findViewById(R.id.dialog_common_view);
		viewGroup.addView(contentView);
		
		dialog.show();
	}
	
	public static void showReplyDialog(final Action<String> action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		final View commonDialog = WidgetFactory.createReplyContentView(new Action<String>() {
			@Override
			public void onExecute(String arg) {
				dialog.dismiss();
				if(action != null){
					action.onExecute(arg);
				}
			}
		}, new ActionNull() {
			@Override
			public void onExecute() {
				dialog.dismiss();
			}
		});
		dialog.setContentView(commonDialog);
		dialog.show();
	}
	
	public static void showModifyPasswordDialog(final Action<String> action){
		final Dialog dialog = new Dialog(ActivityController.getInstance().getCurrentActivity(), 
				R.style.Theme_commonDialog);
		WindowManager.LayoutParams localLayoutParams = dialog.getWindow().getAttributes();
		
//		localLayoutParams.x = context.getResources().getDimensionPixelOffset(R.dimen.x);
//		localLayoutParams.y = -context.getResources().getDimensionPixelOffset(R.dimen.y);
//		dialog.getWindow().setAttributes(localLayoutParams);
			
		dialog.onWindowAttributesChanged(localLayoutParams);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);

		final View commonDialog = WidgetFactory.createModifyPasswordContentView(new Action<String>() {
			@Override
			public void onExecute(String arg) {
				dialog.dismiss();
				if(action != null){
					action.onExecute(arg);
				}
			}
		}, new ActionNull() {
			@Override
			public void onExecute() {
				dialog.dismiss();
			}
		});
		dialog.setContentView(commonDialog);
		dialog.show();
	}
	
}

package com.imrub.shoulder.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.ActionNull;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;

public class WidgetFactory {

	public static View createLoadingView(){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		View v = inflater.inflate(R.layout.loading_dialog, null);
		ImageView image = (ImageView)v.findViewById(R.id.loading_image);
		((AnimationDrawable)image.getDrawable()).start();
		return v;
	}
	
	public static View createCommonDialog(final String title, final ActionNull cancel, final ActionNull ok){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.common_dialog, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setText(title);
		
		view.findViewById(R.id.dialog_common_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});
		
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	public static View createConfirmPhoneView(final String content, final String phoneNumber){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.confirm_phone_dialog_view, null);
		
		TextView contentView = (TextView)view.findViewById(R.id.dialog_phone_content);
		contentView.setText(content);
		
		TextView phone = (TextView)view.findViewById(R.id.dialog_phone);
		phone.setText(phoneNumber);
		return view;
	}

	public static View createTwoListviewCommonDialog(final String title, final ActionNull cancel, final ActionNull ok){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.dialog_two_list_blockview, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setText(title);
		
		view.findViewById(R.id.dialog_common_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});
		
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	public static View createTwoListView(){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.dialog_two_list_view, null);

		ListView left = (ListView)view.findViewById(R.id.dialog_listview_left);
		List<Integer> listLeft = new ArrayList<Integer>();
		for(int i=1; i<13; i++){
			listLeft.add(i);
		}
		left.setAdapter(new DialogListviewAdapter(listLeft));
		
		ListView right = (ListView)view.findViewById(R.id.dialog_listview_right);
		List<Integer> listRight = new ArrayList<Integer>();
		for(int i=1; i<32; i++){
			listRight.add(i);
		}
		right.setAdapter(new DialogListviewAdapter(listRight));
		
		return view;
	}
	
	public static View createOneContentView(final String msg){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.one_content_view, null);
		TextView contentView = (TextView)view.findViewById(R.id.dialog_content);
		contentView.setText(msg);
		return view;
	}
	
	public static View createNoCancelCommonDialog(final String title, final ActionNull ok){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.common_dialog, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setText(title);
		
		view.findViewById(R.id.dialog_common_cancel).setVisibility(View.GONE);
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	public static View createNoTitleCommonDialog(final ActionNull cancel, 
			final ActionNull ok, final String cancelMsg,
			final String okMsg){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.common_dialog, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setVisibility(View.GONE);
		
		view.findViewById(R.id.dialog_line).setVisibility(View.GONE);
		
		Button cancelButton = (Button)view.findViewById(R.id.dialog_common_cancel);
		cancelButton.setText(cancelMsg);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});

		Button okButton = (Button)view.findViewById(R.id.dialog_common_ok);
		okButton.setText(okMsg);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	public static View createNoTitleCommonDialog(final ActionNull cancel, final ActionNull ok){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.common_dialog, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setVisibility(View.GONE);
		
		view.findViewById(R.id.dialog_line).setVisibility(View.GONE);
		view.findViewById(R.id.dialog_common_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});
		
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	public static View createNoTitleNoCancelCommonDialog(final ActionNull ok){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.common_dialog, null);
		
		TextView titleView = (TextView)view.findViewById(R.id.dialog_title);
		titleView.setVisibility(View.GONE);
		
		view.findViewById(R.id.dialog_line).setVisibility(View.GONE);
		view.findViewById(R.id.dialog_common_cancel).setVisibility(View.GONE);
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ok != null){
					ok.onExecute();
				}
			}
		});
		return view;
	}
	
	
	public static View createReplyContentView(final Action<String> action, final ActionNull cancel){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.reply_dialog, null);
		final EditText contentView = (EditText)view.findViewById(R.id.reply_edittext);

		contentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			   @Override
			   public void onFocusChange(View v, boolean hasFocus) {
			       if (hasFocus) {
			    	   ThreadFacade.runOnUiThread(new Runnable() {
						public void run() {
							InputMethodManager manager = (InputMethodManager)AppContext.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
						}
					}, 100);
			       }
			   }
		});
		contentView.requestFocus();

		TextView numberLimit = (TextView)view.findViewById(R.id.dialog_numberlimit);
		TextWatcherImp imp = new TextWatcherImp(contentView, numberLimit, TextWatcherImp.MaxNumber);
		contentView.addTextChangedListener(imp);
		
		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(action != null){
					action.onExecute(contentView.getText().toString());
				}
			}
		});
		
		view.findViewById(R.id.dialog_common_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});
		
		return view;
	}
	
	public static View createModifyPasswordContentView(final Action<String> action, final ActionNull cancel){
		LayoutInflater inflater = LayoutInflater.from(AppContext.getAppContext());
		final View view = inflater.inflate(R.layout.setting_user_accountpassword, null);
		final EditText contentView = (EditText)view.findViewById(R.id.reply_edittext);

		contentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			   @Override
			   public void onFocusChange(View v, boolean hasFocus) {
			       if (hasFocus) {
			    	   ThreadFacade.runOnUiThread(new Runnable() {
						public void run() {
							InputMethodManager manager = (InputMethodManager)AppContext.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
						}
					}, 100);
			       }
			   }
		});
		contentView.requestFocus();

		view.findViewById(R.id.dialog_common_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(action != null){
					action.onExecute(contentView.getText().toString());
				}
			}
		});
		
		view.findViewById(R.id.dialog_common_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cancel != null){
					cancel.onExecute();
				}
			}
		});
		
		return view;
	}
	
}

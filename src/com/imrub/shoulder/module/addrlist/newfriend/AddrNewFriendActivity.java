package com.imrub.shoulder.module.addrlist.newfriend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.db.facade.UserDetailFacade;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.detail.UserDetailInfoActivity;
import com.imrub.shoulder.module.model.detail.UserDetailInfo;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.userDetail.UserDetailRequest;
import com.imrub.shoulder.widget.DialogFactory;

public class AddrNewFriendActivity extends BaseActivity implements IRequestResult<UserDetailInfo>{

	private View mSearchResult;
	private TextView mSearchResultText;
	
	private EditText mSearch;
	private ImageView mDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrlist_add_newfriend);
		initView();
	}
	
	private void initView(){
		mSearchResult = findViewById(R.id.addnewfriend_search_result);
		mSearch = (EditText)findViewById(R.id.addnewfriend_edittext);
		mDelete = (ImageView)findViewById(R.id.addnewfriend_delete);
		mSearchResultText = (TextView)findViewById(R.id.addnewfriend_text2);
		
		ImageView back = (ImageView)findViewById(R.id.addnewfriend_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearch.setText("");
				mSearchResult.setVisibility(View.GONE);
				mDelete.setVisibility(View.GONE);
			}
		});

		mSearchResult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickSearch(mSearchResultText.getText().toString());
			}
		});
		
		mSearch.addTextChangedListener(new TextWatcherImp());
	}

	private void onClickSearch(String content){
		if(content.length() > 9){
			String error = AppContext.getString(R.string.addrlist_new_friend_notexist);
			DialogFactory.showNoTitleNoCancelConfirmDialog(error, null);
			return ;
		}
		showLoadingView(R.string.addrlist_new_search);
		UserDetailRequest.userDetailRequest(this, Integer.parseInt(content));
	}
	
	@Override
	public void onError(int code, String msg) {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		String error = AppContext.getString(R.string.addrlist_new_friend_notexist);
		DialogFactory.showNoTitleNoCancelConfirmDialog(error, null);
	}

	@Override
	public void onSuccess(UserDetailInfo t) {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		
		AddrNewFriendData.getInstance().setUserDetailDataFromAddFriend(t);
		updateDataToDatabaseAsyn(t);
		
		Intent intent = new Intent(this, UserDetailInfoActivity.class);
		intent.putExtra("uid", t.getUser_info().getUid()+"");
		intent.putExtra("fromType", AddrNewFriendData.TypeFromAddNewFriend);
		startActivity(intent);
	}
	
	private void updateDataToDatabaseAsyn(final UserDetailInfo t){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				UserDetailFacade.updateUserDetainInfo(t.toUserDetailInfoData());
			}
		});
	}
	
	private class TextWatcherImp implements TextWatcher{
		@Override
		public void afterTextChanged(Editable s) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int c = s.length();
			if(c != 0){
				mSearchResultText.setText(s);
				mSearchResult.setVisibility(View.VISIBLE);
				mDelete.setVisibility(View.VISIBLE);
			} else {
				mSearchResult.setVisibility(View.GONE);
				mDelete.setVisibility(View.GONE);
			}
		}
	}
	
}

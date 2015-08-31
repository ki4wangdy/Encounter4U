package com.imrub.shoulder.module.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imrub.shoulder.Action;
import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.app.store.UserInfo;
import com.imrub.shoulder.base.db.facade.FriendFacade;
import com.imrub.shoulder.base.db.facade.QiuKnowUserFacade;
import com.imrub.shoulder.base.db.facade.UserDetailFacade;
import com.imrub.shoulder.base.db.table.addr.AddrlistNewData;
import com.imrub.shoulder.base.db.table.cajian.UserDetailInfoData;
import com.imrub.shoulder.base.db.table.im.ReplyMessage;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.ImageSizeUtils;
import com.imrub.shoulder.base.util.bitmaploader.BitmapLoaderManager;
import com.imrub.shoulder.module.addrlist.more.AddrlistNewConstant;
import com.imrub.shoulder.module.addrlist.newfriend.AddrNewFriendData;
import com.imrub.shoulder.module.addrlist.newfriend.IMReplyMsgManager;
import com.imrub.shoulder.module.im.MessageIdUtils;
import com.imrub.shoulder.module.main.MainUIActivity;
import com.imrub.shoulder.module.model.detail.MeetInfo;
import com.imrub.shoulder.module.model.detail.RequestInfo;
import com.imrub.shoulder.module.model.detail.UserDetailInfo;
import com.imrub.shoulder.module.request.IRequestResult;
import com.imrub.shoulder.module.request.addrlist.CreateKnowFacade;
import com.imrub.shoulder.module.request.addrlist.ICreateKnow;
import com.imrub.shoulder.module.request.userDetail.UserDetailRequest;
import com.imrub.shoulder.widget.DialogFactory;
import com.imrub.shoulder.widget.NotificationFactory;
import com.imrub.shoulder.widget.Title;
import com.imrub.shoulder.widget.ToastFactory;

public class UserDetailInfoActivity extends BaseActivity implements 
			IRequestResult<UserDetailInfo>, ICreateKnow{

	public static final int TypeAgree 	= 	1;
	public static final int TypeTo		=	2;
	public static final int TypeChat	=	3;
	
	private ViewPagerProxy mViewPagerProxy;
	
	private GridView mReplyGridView;
	private ReplyGridViewAdapter mAdapter;
	
	private View mReplyBlock;
	private View mRelplyButtonBlock;
	private Button mReplyButton;
	
	private Title mTitle;
	private TextView mNickName;
	private TextView mCajianId;
	private TextView mSignature;
	private ImageView mUserLogo;
	
	private TextView mText1;
	private TextView mText2;
	
	private ImageView mQiuKnow;
	private ImageView mMeet;
	private ImageView mShare;
	
	private MoveableView mMoveableView;
	
	private String mUid;
	private String mCurrentComment;
	
	private UserDetailInfo mUserDetailInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_detail);
		initType();
		initView();
		initData();
		registerListener();
		requestAsyn();
	}

	private void initType(){
		int type = getIntent().getIntExtra("fromType", -1);
		mUid = getIntent().getExtras().getString("uid", "0");
		switch (type) {
			case AddrNewFriendData.TypeFromAddNewFriend:
				mUserDetailInfo = AddrNewFriendData.getInstance().getUserDetailDataFromAddFriend(mUid);
				break;
		}
	}
	
	private void initView(){
		View titleContainer = findViewById(R.id.userinfo_detail_title);
		mTitle = new Title(titleContainer);
		mTitle.setContent(getString(R.string.detail_detail_info));
		mTitle.setOnBackListener(new Action<View>() {
			@Override
			public void onExecute(View arg) {
				finish();
			}
		});
		
		initBasicUserInfo();
		initReplyBlock();
		initPagerView();
		initBottomView();

		ViewPager pager = (ViewPager)findViewById(R.id.detail_viewpager);
		mViewPagerProxy = new ViewPagerProxy(pager);
		
		mMoveableView = (MoveableView)findViewById(R.id.moveable_view);
		mMoveableView.setonPagerListener(new Action<Integer>() {
			@Override
			public void onExecute(Integer pager) {
				updateTextBasedOnPager(pager);
			}
		});
		mViewPagerProxy.setOnPageChangeListener(mMoveableView);
	}

	private void initData(){
		UserDetailInfo data = mUserDetailInfo;
		if(data == null){
			data = loadDbFromDatabase();
			if(data == null){
				return ;
			}
		}
		mUserDetailInfo = data;
		initBasicUserInfo(data);
		initMeetInfo(data);
		initRequestInfo(data);
		iniBottomBlockImage(mQiuKnow);
	}
	
	private UserDetailInfo loadDbFromDatabase(){
		UserDetailInfoData info = UserDetailFacade.queryUserDetailInfo(mUid);
		if(info != null){
			return info.toUserDetailInfo();
		}
		return null;
	}
	
	private void initPagerView(){
		mText1 = (TextView)findViewById(R.id.userinfo_text1);
		mText2 = (TextView)findViewById(R.id.userinfo_text2);
	}
	
	private void initBottomView(){
		mQiuKnow = (ImageView)findViewById(R.id.detail_bottom_left);
		mQiuKnow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Integer type = (Integer)v.getTag();
				if(type != null){
					switch (type) {
					case TypeTo:
						onClickQiuknow();
						break;
					case TypeAgree:
						break;
					case TypeChat:
						onClickChat();
						break;
					}
				}
			}
		});
		
		mMeet = (ImageView)findViewById(R.id.detail_bottom_middle);
		mMeet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		mShare = (ImageView)findViewById(R.id.detail_bottom_right);
		mShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	private boolean hasUserExist(int uid){
		AddrlistNewData item = QiuKnowUserFacade.queryQiukonwUserInfo(
				mUserDetailInfo.getUser_info().getUid()+"");
		if(item != null){
			return AddrlistNewConstant.StatusType.Status_ToAgree 
					== item.getStatus() ? true : false;
		}
		return false;
	}
	
	private void iniBottomBlockImage(ImageView imageView){
		if(mUserDetailInfo == null){
			return ;
		}
		// judge the type 
		boolean isFriend = mUserDetailInfo.isIs_friend() == 1 ? true : false;
		if(!isFriend){
			if(hasUserExist(mUserDetailInfo.getUser_info().getUid())){
				imageView.setImageResource(R.drawable.detail_qiuknow_agree_bg);
				imageView.setTag(TypeAgree);
			} else {
				imageView.setImageResource(R.drawable.detail_qiuknow_image_bg);
				imageView.setTag(TypeTo);
			}
		} else {
			imageView.setImageResource(R.drawable.detail_qiuknow_chat_bg);
			imageView.setTag(TypeChat);
		}
	}
	
	private void initBasicUserInfo(){
		mNickName = (TextView)findViewById(R.id.detail_user_name);
		mCajianId = (TextView)findViewById(R.id.detail_user_id);
		mSignature = (TextView)findViewById(R.id.detail_user_nick);
		mUserLogo = (ImageView)findViewById(R.id.detail_user_logo);
	}
	
	private void initReplyBlock(){
		mReplyBlock = findViewById(R.id.userinfo_reply_block);
		mRelplyButtonBlock = findViewById(R.id.reply_button_block);
		mReplyGridView = (GridView)findViewById(R.id.userinfo_reply_gridview);
		mReplyButton = (Button)findViewById(R.id.reply_button);
		mReplyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickReplyButton();
			}
		});
	}
	
	private void onClickReplyButton(){
		DialogFactory.showReplyDialog(new Action<String>() {
			@Override
			public void onExecute(String arg) {
				onClickReplyInSubThread(arg);
			}
		});
	}
	
	private void onClickReplyInSubThread(final String msg){
		if(!isNetworkConnected()){
			return ;
		}
		showLoadingView(R.string.detail_reply_requesting);
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				replyMessage(Integer.parseInt(mUid), msg);
				mCurrentComment = msg;
			}
		});
	}
	
	private void sendMsg(String msg){
		String userName = UserInfo.getInstance().getUserName();
		String userIcon = "";
		String userSignature = mCurrentComment;
		ReplyMessage replyMsg = new ReplyMessage(userSignature,userName,userIcon);
		replyMsg.setSignature(msg);
		IMReplyMsgManager.getInstance().sendMsg(mUid+"@cajian.cc", replyMsg.getMessageJsonString());
	}
	
	private static void replyMessage(int toUid, String comment){
		CreateKnowFacade.getInstance().replyCreateKnowRequest(toUid, comment);
	}
	
	@SuppressWarnings("unused")
	private void setReplyVisiable(boolean isVisiable){
		mRelplyButtonBlock.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
	}
	
	private void requestAsyn(){
		if(!"0".equalsIgnoreCase(mUid)){
			UserDetailRequest.userDetailRequest(this, Integer.parseInt(mUid));
		}
	}
	
	@Override
	public void onError() {
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsg(this, R.string.detail_reply_error);
	}

	@Override
	public void onSuccess() {
		if(mCurrentComment != null){
			sendMsg(mCurrentComment);
		}
		if(isLoadingViewShow()){
			dismissLoadingView();
		}
		ToastFactory.showMsg(this, R.string.detail_reply_success);
		mAdapter.addRequestInfo(new RequestInfo(RequestInfo.TypeReply, mCurrentComment));
	}
	
	private void updateTextBasedOnPager(int pager){
		if(pager == 1){
			mText2.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.main_ui_name_color));
			mText1.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.detail_middle_textcolor));
		} else {
			mText1.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.main_ui_name_color));
			mText2.setTextColor(AppContext.getAppContext().getResources().getColor(R.color.detail_middle_textcolor));
		}
	}
	
	private void initBasicUserInfo(UserDetailInfo t){
		if(t.getUser_info() == null){
			return ;
		}
		
		if(t.isIs_friend() == 1){
			mRelplyButtonBlock.setVisibility(View.GONE);
		}
		
		mNickName.setText(t.getUser_info().getNick_name());
		String signature = t.getUser_info().getDescription();
		if(signature == null || signature.equalsIgnoreCase("")){
			mSignature.setVisibility(View.GONE);
		} else {
			mSignature.setText(getString(R.string.detail_user_nickname, signature));
		}
		mCajianId.setText(getString(R.string.detail_user_uid,t.getUser_info().getUid()));
		updateImage(t.getUser_info().getHeader_logo());
	}
	

	private void updateImage(String userLogo){
		int width = AppContext.getDimensionPixelSize(R.dimen.image_width);
		BitmapLoaderManager.getInstance().loadBitmap(
				ImageSizeUtils.get140x140(userLogo), 
				mUserLogo, 
				width, width, 
				AppContext.getBitmap(R.drawable.icon_66x66));
	}
	
	private void initMeetInfo(UserDetailInfo t){
		if(t.getMeet_info() == null){
			return ;
		}
		List<Integer> list = new ArrayList<Integer>();
		for(MeetInfo info : t.getMeet_info()){
			list.add(info.getMeet_time());
		}
		mViewPagerProxy.setData(list);
	}
	
	private void initRequestInfo(UserDetailInfo t){
		List<RequestInfo> infos = t.getRequest_info();
		if(infos == null){
			return ;
		}
		Collections.reverse(infos);
		if(infos != null && !infos.isEmpty()){
			if(mAdapter == null){
				mAdapter = new ReplyGridViewAdapter(infos, t.getUser_info().getNick_name());
				mReplyGridView.setAdapter(mAdapter);
			} else {
				mAdapter.addRequestInfos(infos);
			}
			mReplyBlock.setVisibility(View.VISIBLE);
		} else {
			mReplyBlock.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onSuccess(UserDetailInfo t) {
		mUserDetailInfo = t;
		initData();
		updateDataToDatabase();
	}
	
	private void updateDataToDatabase(){
		ThreadFacade.runOnSingleThread(new Runnable() {
			@Override
			public void run() {
				UserDetailFacade.updateUserDetainInfo(mUserDetailInfo.toUserDetailInfoData());
			}
		});
	}
	
	@Override
	public void onError(int code, String msg) {
	}
	
	private void onClickChat(){
		String userName = FriendFacade.getInstance().getFriendName(mUid);
		if(userName == null){
			return ;
		}
		Intent i = new Intent(AppContext.getAppContext(), MainUIActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		i.putExtra(NotificationFactory.CHAT_ROOM_ID, MessageIdUtils.createRoomId(mUid));
		i.putExtra(NotificationFactory.CHAT_ROOM_NAME,  userName != null ? userName : mUid);
		startActivity(i);
	}
	
	private void onClickQiuknow(){
		CreateKnowFacade.getInstance().unRegisterListener(this);
		// check whether or not the user is the same as me
		String uid = UserInfo.getInstance().getUid();
		if(uid.equalsIgnoreCase(mUid)){
			ToastFactory.showMsgForShortTime(this, R.string.addrlist_new_same);
			return ;
		}
		Intent intent = new Intent(this, QiuKnowMessageActivity.class);
		intent.putExtra("uid", mUid);
		intent.putExtra("name", UserInfo.getInstance().getUserName());
		intent.putExtra("targetName", mUserDetailInfo.getUser_info().getNick_name());
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CreateKnowFacade.getInstance().registerListener(this);
		if(resultCode == RESULT_OK && requestCode == 1){
			String msg = data.getExtras().getString("msg");
			RequestInfo info = new RequestInfo(RequestInfo.TypeReply, msg);
			if(mAdapter == null){
				List<RequestInfo> datas = new ArrayList<RequestInfo>();
				datas.add(info);
				mAdapter = new ReplyGridViewAdapter(datas,mUid);
				mReplyGridView.setAdapter(mAdapter);
			} else {
				mAdapter.addRequestInfo(info);
			}
			mReplyBlock.setVisibility(View.VISIBLE);
		}
	}
	
	private void registerListener(){
		CreateKnowFacade.getInstance().registerListener(this);
	}
	
	private void unRegisterListener(){
		CreateKnowFacade.getInstance().unRegisterListener(this);
	}
	
	@Override
	protected void onDestroy() {
		unRegisterListener();
		super.onDestroy();
	}
	
}


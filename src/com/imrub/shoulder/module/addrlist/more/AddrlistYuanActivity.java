package com.imrub.shoulder.module.addrlist.more;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.imrub.shoulder.BaseActivity;
import com.imrub.shoulder.R;
import com.imrub.shoulder.module.model.AddrlistYuanData;

public class AddrlistYuanActivity extends BaseActivity{

	private TextView mTitle;
	private ListView mListView;
	private AddrlistYuanAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrlist_yuan_view);
		initView();
	}
	
	private void initView(){
		View titleContainer = findViewById(R.id.addrlist_yuan_title);
		mTitle = (TextView)titleContainer.findViewById(R.id.title_content);
		mTitle.setText(R.string.addrlist_header_icon_text_2);
		
		View v = titleContainer.findViewById(R.id.title_bg_image);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mListView = (ListView)findViewById(R.id.addrlist_yuan_listview);
		List<AddrlistYuanData> data = getData();
		if(data.isEmpty()){
			mListView.setVisibility(View.GONE);
			initEmptyView();
		}
		
		mAdapter = new AddrlistYuanAdapter(getData());
		mListView.setAdapter(mAdapter);
		
	}
	
	private List<AddrlistYuanData> getData(){
		List<AddrlistYuanData> data = new ArrayList<AddrlistYuanData>();
		data.add(new AddrlistYuanData("月海瑶", "月海上一叶舟，飘飘衣袖", "322332", "已经相遇18次"));
		data.add(new AddrlistYuanData("月下清泉", "门前有清泉如许，身旁有佳人如厮", "322332", "已经相遇43次"));
		data.add(new AddrlistYuanData("速度与激情", "喜欢跑车，喜欢速度，追求激情", "322332", "已经相遇2次"));
		data.add(new AddrlistYuanData("风清扬", "我不是马云，请自重", "322332", "已经相遇1次"));
		data.add(new AddrlistYuanData("爱飘柔之少林金刚寺", "奇柔软妹子，签名好二", "322332", "已经相遇8次"));
		data.add(new AddrlistYuanData("云山初日", "风吹云破，渐展红日，世间多美景！岂不是找找木木的说嘛，这个要长", "322332", "已经相遇58次"));
		return data;
	}
	
	private void initEmptyView(){
		View view = findViewById(R.id.addrlist_empty_data);
		view.setVisibility(View.VISIBLE);
		
		View titleContainer = view.findViewById(R.id.addrlist_yuan_title);
		TextView title = (TextView)titleContainer.findViewById(R.id.title_content);
		title.setText(R.string.addrlist_header_icon_text_2);
		
		View v = titleContainer.findViewById(R.id.title_bg_image);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		TextView text1 = (TextView)view.findViewById(R.id.addrlist_empty_text_first);
		text1.setText(R.string.addrlist_empty_first_text_0);
		
		TextView text2 = (TextView)view.findViewById(R.id.addrlist_empty_text_second);
		text2.setText(R.string.addrlist_empty_second_text_0);
		
	}
	
}

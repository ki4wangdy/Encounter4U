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
		data.add(new AddrlistYuanData("�º���", "�º���һҶ�ۣ�ƮƮ����", "322332", "�Ѿ�����18��"));
		data.add(new AddrlistYuanData("������Ȫ", "��ǰ����Ȫ���������м�������", "322332", "�Ѿ�����43��"));
		data.add(new AddrlistYuanData("�ٶ��뼤��", "ϲ���ܳ���ϲ���ٶȣ�׷����", "322332", "�Ѿ�����2��"));
		data.add(new AddrlistYuanData("������", "�Ҳ������ƣ�������", "322332", "�Ѿ�����1��"));
		data.add(new AddrlistYuanData("��Ʈ��֮���ֽ����", "���������ӣ�ǩ���ö�", "322332", "�Ѿ�����8��"));
		data.add(new AddrlistYuanData("��ɽ����", "�紵���ƣ���չ���գ��������������������ľľ��˵����Ҫ��", "322332", "�Ѿ�����58��"));
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

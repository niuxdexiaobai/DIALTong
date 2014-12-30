package com.zhy.dialtong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.zhy.dialtong.R;

public class MoreFragment extends Activity{
	
	private ScrollView ScrollView;
	private RelativeLayout setting_wifi1;
	
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		View view = inflater.inflate(com.zhy.dialtong.R.layout.more_page, container, false);
//		return view;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_page);
		init();
	}

	private void init() {
		setting_wifi1 = (RelativeLayout) findViewById(R.id.setting_wifi1);
		setting_wifi1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent=new Intent(MoreFragment.this,MoreWifiSettingPage.class);
				startActivity(intent);
//				Intent intent=new Intent(MoreFragment.this,MoreWifiManagerActivity.class);
//				startActivity(intent);
				
//				Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);//打开系统WiFi配置页面
//				startActivity(intent);
			}
		});
	}	
}

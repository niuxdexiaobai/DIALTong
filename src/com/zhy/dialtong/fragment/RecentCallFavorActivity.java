package com.zhy.dialtong.fragment;

import com.zhy.dialtong.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

public class RecentCallFavorActivity extends Activity{
	
	private LayoutInflater inflater;
	private ListView favorcallrecordlist;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.recent_call_select_item_page, null));
		
		favorcallrecordlist = (ListView) findViewById(R.id.callrecordlist);
	}

}

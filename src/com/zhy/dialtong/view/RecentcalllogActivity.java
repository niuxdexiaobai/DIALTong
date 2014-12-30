package com.zhy.dialtong.view;


import com.zhy.dialtong.fragment.CallLogFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class RecentcalllogActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		createFragment();
	}
	
	protected CallLogFragment createFragment() {
		return new CallLogFragment();
	}

}

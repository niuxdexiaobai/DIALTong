package com.zhy.dialtong.fragment;

import com.zhy.dialtong.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class Item_Sort_Listview extends FragmentActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts_item_sort_listview);
	}

}

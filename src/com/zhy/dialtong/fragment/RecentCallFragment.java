package com.zhy.dialtong.fragment;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhy.dialtong.R;
import com.zhy.dialtong.fragment.recentcall.CallLogBean;
import com.zhy.dialtong.fragment.recentcall.CallLogManager;
import com.zhy.dialtong.fragment.recentcall.CalllogAdapter;
import com.zhy.dialtong.fragment.recentcall.HomeDialAdapter;

public class RecentCallFragment extends Activity{
	
	private Context context;
	private AsyncQueryHandler asyncQuery;
	
	private HomeDialAdapter adapter;
	private List<CallLogBean> list;
	
	private ListView callrecordlist;
	
	private CalllogAdapter adapter1;
	
//	public View onCreateView(LayoutInflater inflater,
//			 ViewGroup container,  Bundle savedInstanceState) {
//		View view = inflater.inflate(com.zhy.dialtong.R.layout.recent_call_record_page, container, false);
//		return view;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recent_call_select_item_page);
		
		callrecordlist = (ListView) findViewById(R.id.callrecordlist);
		
//		CallLogManager reader = new CallLogManager(this);
//		adapter = new HomeDialAdapter(this, reader.getAllCallRecords());
//		callrecordlist.setAdapter(adapter);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		
		
		init();
		
//		Cursor mCallLogCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, 
//				new String[]{"max(date) as max_date",CallLog.Calls.NUMBER,CallLog.Calls.DURATION,CallLog.Calls.TYPE},
//				"1=1) group by(number", 
//				null, 
//				CallLog.Calls.DEFAULT_SORT_ORDER);
		
//		Cursor mCallLogCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, 
//				new String[]{CallLog.Calls.NUMBER,},
//				"1=1) group by(CallLog.Calls.NUMBER", 
//				null, 
//				CallLog.Calls.DEFAULT_SORT_ORDER);
		
//		CallLogManager reader = new CallLogManager(this);
//		adapter1 = new CalllogAdapter(this, reader.getAllCallRecords());//
//		callrecordlist.setAdapter(adapter1);
	}
	
	private void init(){
		Uri uri = CallLog.Calls.CONTENT_URI;//��ò��ŵ�����ϵͳͳһ��ʶ��URI
		
		String[] projection = { 
				CallLog.Calls.DATE,
				CallLog.Calls.NUMBER,
				CallLog.Calls.TYPE,
				CallLog.Calls.CACHED_NAME,
//				CallLog.Calls.CACHED_MATCHED_NUMBER,
				CallLog.Calls._ID,
//				ContactsContract.Contacts.TIMES_CONTACTED
		}; // 
//		String selection = CallLog.Calls.NUMBER;
//		 resolver.query(CallLog.Calls.CONTENT_URI, null, "number=?", new String[]{"15101689022"}, null);
		
//		Uri uri1 = ContactsContract.Contacts.CONTENT_URI;
		String[] projection1 = {
				CallLog.Calls.NUMBER,
		};
		String viaOrder = android.provider.CallLog.Calls.DATE + " DESC," + android.provider.CallLog.Calls.NUMBER + " DESC";
//		String[] projection1 = {
//				"max(date) as max_date",CallLog.Calls.NUMBER,CallLog.Calls.DURATION,CallLog.Calls.TYPE
//		};CallLog.Calls.DEFAULT_SORT_ORDER CallLog.Calls.NUMBER+"=?", new String[]{"number"},
		asyncQuery.startQuery(0, null, uri, projection, null, null, Calls.DEFAULT_SORT_ORDER);  
//		asyncQuery.startQuery(0, null, uri, projection1 COUNT(*) AS num_calls, null, null, null);
	}
	
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				list = new ArrayList<CallLogBean>();
				SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");// HH��24Сʱ�ƣ�hh��12Сʱ��
				Date date;
				cursor.moveToFirst();// �α��ƶ�����һ��
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					date = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
//					String date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
					String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
					int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
					String cachedName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));//�����������绰���룬������Ĵ���
//					String count = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL));
					int id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
//					String timeContact = cursor.getString(7);

					CallLogBean clb = new CallLogBean();
					clb.setId(id);
					clb.setNumber(number);
					clb.setName(cachedName);
					if(null == cachedName || "".equals(cachedName)){
						clb.setName(number);
					}
					clb.setType(type);
//					clb.setCount(count);
//					clb.setCount(timeContact);
					clb.setDate(sfd.format(date));
					
					list.add(clb);
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}
		}

	}
	
	private void setAdapter(List<CallLogBean> list) {
		adapter = new HomeDialAdapter(this, list);
//		TextView tv = new TextView(this);
//		tv.setBackgroundResource(R.drawable.dial_input_bg2);
//		callLogList.addFooterView(tv);
		callrecordlist.setAdapter(adapter);
//		callLogList.setOnScrollListener(new OnScrollListener() {
//
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//					if(bohaopan.getVisibility() == View.VISIBLE){
//						bohaopan.setVisibility(View.GONE);
//						keyboard_show_ll.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//			}
//		});
		callrecordlist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Uri uri = Uri.parse("tel:" + list.getNumber());
//				Intent it = new Intent(Intent.ACTION_CALL, uri);
//				ctx.startActivity(it);
//				Intent phoneIntent = new Intent("android.intent.action.CALL",
//						Uri.parse("tel:" + adapter.getNumber()));
//				context.startActivity(phoneIntent);
//				context.getContentResolver().delete(CallLog.Calls.CONTENT_URI,null,null);
			}
		});
	}


}

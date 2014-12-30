package com.zhy.dialtong.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.dialtong.ClearEditText;
import com.zhy.dialtong.ContactBean;
import com.zhy.dialtong.R;
import com.zhy.dialtong.fragment.contacts.CharacterParser;
import com.zhy.dialtong.fragment.contacts.ContactHomeAdapter;
import com.zhy.dialtong.fragment.contacts.PinyinComparator;
import com.zhy.dialtong.fragment.contacts.SortModel;
import com.zhy.dialtong.view.QuickAlphabeticBar;

public class ContactsActivity extends Activity{
	
	private ClearEditText mClearEditText;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	private QuickAlphabeticBar alpha;
	private TextView dialog;
	private ListView sortListView;
	private TextView contacts_add;
	private static AsyncQueryHandler asyncQuery;
	
	private List<ContactBean> list;
	
	private Map<Integer, ContactBean> contactIdMap = null;//
	
	private ContactHomeAdapter adapter;
	
	private LayoutInflater inflater;
	
//	private Map<String,String> callRecords;
	
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			 ViewGroup container,  Bundle savedInstanceState) {
//		View view = inflater.inflate(com.zhy.dialtong.R.layout.contacts_page, container, false);
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		inflater = LayoutInflater.from(this);
		setContentView(inflater.inflate(R.layout.contact_select_item_page, null));
		
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
//		contacts_add = (TextView) findViewById(R.id.contacts_add);
//		contacts_add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Uri insertUri = android.provider.ContactsContract.Contacts.CONTENT_URI;
//				Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
//				startActivityForResult(intent, 1008);
//			}
//		});
		
		dialog = (TextView) findViewById(R.id.fast_position);
		
//		callRecords=getAllCallRecords();
		sortListView = (ListView) findViewById(R.id.acbuwa_list);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//			Toast.makeText(getApplication(),((SortModel) adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
//				String number=callRecords.get(((ContactBean)adapter.getItem(position)).getPhoneNum());//获取到联系人的号码
//				Intent intent=new Intent(ContactsActivity.this,ConstactsDetailActivity.class);//设置传入目标类
//				intent.putExtra("number", number);
//				intent.putExtra("name", ((ContactBean)adapter.getItem(position)).getDisplayName());
//				startActivity(intent);
			}
		});
		
		alpha = (QuickAlphabeticBar)this.findViewById(R.id.fast_scroller);
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();
		
//		return view;
	}

	public static void init() {
		// TODO Auto-generated method stub
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { 
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1,
				"sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
				ContactsContract.CommonDataKinds.Phone.STARRED,
//				ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
		};// 查询的列
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
		
	}
	

	
	
	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}
		
		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				
				contactIdMap = new HashMap<Integer, ContactBean>();//新建一个hashMap表来存放联系人数据
				
				list = new ArrayList<ContactBean>();//新建一个只支持ContactBean的数组列表
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);
					int starred = cursor.getInt(7);
//					String times = cursor.getString(8);

					if (contactIdMap.containsKey(contactId)) {
						
					}else{
						
						ContactBean cb = new ContactBean();
						cb.setDisplayName(name);
//					if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
//						cb.setPhoneNum(number.substring(3));
//					} else {
						cb.setPhoneNum(number);
//					}
						cb.setSortKey(sortKey);
						cb.setContactId(contactId);
						cb.setPhotoId(photoId);
						cb.setLookUpKey(lookUpKey);
						cb.setStarred(starred);
//						cb.setDialcount(times);
						list.add(cb);
						
						contactIdMap.put(contactId, cb);
						
					}
				}
				if (list.size() > 0) {
					setAdapter(list);
				}
			}
		}

	}
	
	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactHomeAdapter(this, list, alpha);
		sortListView.setAdapter(adapter);
		alpha.init(ContactsActivity.this);
		alpha.setListView(sortListView);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				ContactBean cb = (ContactBean) adapter.getItem(position);
//				showContactDialog(lianxiren1, cb, position);
//				String number=callRecords.get(((ContactBean)adapter.getItem(position)).getPhoneNum());
				Intent intent=new Intent(ContactsActivity.this,ConstactsDetailActivity.class);
				intent.putExtra("number", ((ContactBean)adapter.getItem(position)).getPhoneNum());
				intent.putExtra("name", ((ContactBean)adapter.getItem(position)).getDisplayName());
//				intent.putExtra("photo",((ContactBean)adapter.getItem(position)).getPhoto());
				intent.putExtra("ID",((ContactBean)adapter.getItem(position)).getContactId()); 
				startActivity(intent);
			}
		});
//		sortListView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				ContactBean cb = (ContactBean) adapter.getItem(position);
//				showContactDialog(lianxiren1, cb, position);
//				return true;
//			}
//		});
	}
//	
//	private String[] lianxiren1 = new String[] { "拨打电话", /*"发送短信",*/ "查看详细",/*"移动分组","移出群组",*/"删除" };
//	
//	//群组联系人弹出页
//		private void showContactDialog(final String[] arg ,final ContactBean cb, final int position){
//			new AlertDialog.Builder(this).setTitle(cb.getDisplayName()).setItems(arg,
//					new DialogInterface.OnClickListener(){
//				public void onClick(DialogInterface dialog, int which){
//
//					Uri uri = null;
//
//					switch(which){
//
//					case 0://打电话
//						String toPhone = cb.getPhoneNum();
//						uri = Uri.parse("tel:" + toPhone);
//						Intent it = new Intent(Intent.ACTION_CALL, uri);
//						startActivity(it);
//						break;
//
////					case 1://发短息
////
////						String threadId = getSMSThreadId(cb.getPhoneNum());
////						
////						Map<String, String> map = new HashMap<String, String>();
////						map.put("phoneNumber", cb.getPhoneNum());
////						map.put("threadId", threadId);
////						BaseIntentUtil.intentSysDefault(HomeContactActivity.this, MessageBoxList.class, map);
////						break;
//
//					case 1:// 查看详细       修改联系人资料
//
//						uri = ContactsContract.Contacts.CONTENT_URI;
//						Uri personUri = ContentUris.withAppendedId(uri, cb.getContactId());
//						Intent intent2 = new Intent();
//						intent2.setAction(Intent.ACTION_VIEW);
//						intent2.setData(personUri);
//						startActivity(intent2);
//						break;
//
//					//case 3: 移动分组
//
//						//					Intent intent3 = null;
//						//					intent3 = new Intent();
//						//					intent3.setClass(ContactHome.this, GroupChoose.class);
//						//					intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//						//					intent3.putExtra("联系人", contactsID);
//						//					Log.e("contactsID", "contactsID---"+contactsID);
//						//					ContactHome.this.startActivity(intent3);
////						break;
//
//					//case 4: 移出群组
//
//						//					moveOutGroup(getRaw_contact_id(contactsID),Integer.parseInt(qzID));
////						break;
//
//					case 2:// 删除
//
//						showDelete(cb.getContactId(), position);
//						break;
//					}
//				}
//			}).show();
//		}
//
//		// 删除联系人方法
//		private void showDelete(final int contactsID, final int position) {
//			new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("是否删除此联系人")
//			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//					//源码删除
//					Uri deleteUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactsID);
//					Uri lookupUri = ContactsContract.Contacts.getLookupUri(ContactsActivity.this.getContentResolver(), deleteUri);
//					if(lookupUri != Uri.EMPTY){
//						ContactsActivity.this.getContentResolver().delete(deleteUri, null, null);
//					}
//					adapter.remove(position);
//					adapter.notifyDataSetChanged();
//					Toast.makeText(ContactsActivity.this, "该联系人已经被删除.", Toast.LENGTH_SHORT).show();
//				}
//			})
//			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//
//				}
//			}).show();
//		}

	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<ContactBean> filterDateList = new ArrayList<ContactBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = list;
		} else {
			filterDateList.clear();
			for (ContactBean ContactBean : list) {
				String name = ContactBean.getDisplayName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(ContactBean);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
	/*private Map<String,String> getAllCallRecords() {
		Map<String,String> temp = new HashMap<String, String>();
		Cursor c = getContentResolver().query(	//查找信息，联系人
				ContactsContract.Contacts.CONTENT_URI,
				null,
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		if (c.moveToFirst()) {
			do {
				// 获取联系人ID
				String contactId = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获取联系人姓名
				String name = c
						.getString(c
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 获取联系人电话号码
				int phoneCount = c
						.getInt(c
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				String number=null;
				if (phoneCount > 0) {
					// 
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						number = phones
								.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						}
					phones.close();
				}
				temp.put(name, number);
			} while (c.moveToNext());
		}
		c.close();
		return temp;
	}*/

}
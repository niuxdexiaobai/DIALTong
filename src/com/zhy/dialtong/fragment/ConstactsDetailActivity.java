package com.zhy.dialtong.fragment;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.dialtong.ContactBean;
import com.zhy.dialtong.R;
import com.zhy.dialtong.fragment.contacts.ActionSheet;
import com.zhy.dialtong.fragment.contacts.ActionSheet.ActionSheetListener;
import com.zhy.dialtong.view.HomeTabHostAcitivity;

public class ConstactsDetailActivity extends FragmentActivity

 implements OnClickListener,ActionSheetListener{
	
	private TextView contacts_back,contacts_edit,contact_detail_name_show,contact_detail_num_show;
	private ImageView contacts_imageView;
	private ContactBean contactBean;
	private Context ctx;
	private int id;                  // id for contact reference
	private ImageButton star;
	
	private static final String ADD_CONTACTS = "增加联系人";
	private static final String DEL_CONTACTS = "删除联系人";
	private static final String EDIT_CONTACTS = "编辑联系人";
	
//	String[] EDIT_ITEM_1 = new String[] {ADD_CONTACTS,DEL_CONTACTS,EDIT_CONTACTS};
//	enum EDIT_ITEM {ADD_CONTACTS,DEL_CONTACTS,EDIT_CONTACTS};
	
	private static final int REQUEST_PHOTO = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contacts_detail);
		
		init();
	}
	
	private void init() {
		// TODO Auto-generated method stub
		
//		// Intent from past Activity
//		Intent intent = getIntent();
//				
//		// Get the contact from the intent 
//		id = (int) intent.getIntExtra("id",0);
//		
//		// Instantiate the DatabaseHandler
//		db = new DatabaseHandler(this);
//		contactBean = db.getContact(id);
		
		contacts_back = (TextView) findViewById(R.id.contacts_back);
		contacts_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent=new Intent(ConstactsDetailActivity.this,HomeTabHostAcitivity.class);
				startActivityForResult(intent, 1008);
//				finish();
			}
		});
		
		contacts_edit = (TextView) findViewById(R.id.contacts_edit);
		contacts_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//下面的2行代码是显示仿ios的item出现的，但是不知道怎么设置点击事件
//				setTheme(R.style.ActionSheetStyleIOS7);
//				showActionSheet();
				Intent edit=new Intent(ConstactsDetailActivity.this,ConstactsEditActivity.class);
//				if(tv_add_edit.getText().toString().trim().equals("鏂板缓")){	
//					edit.putExtra("status", false);
//				}else{
//					edit.putExtra("status", true);
//				}
				edit.putExtra("phoneNumber", getIntent().getStringExtra("number"));
				edit.putExtra("name", getIntent().getStringExtra("name"));
//				edit.putExtra("name", getIntent().getStringExtra("name"));
				startActivity(edit);
			}
		});
		
		
		contact_detail_name_show = (TextView) findViewById(R.id.contact_detail_name_show);
		String name=getIntent().getStringExtra("name");
		contact_detail_name_show.setText(name);
		
		contact_detail_num_show = (TextView) findViewById(R.id.contact_detail_num_show);
		String number=getIntent().getStringExtra("number");
		contact_detail_num_show.setText(number);
		contact_detail_num_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("tel:" + getIntent().getStringExtra("number"));
				Intent it = new Intent(Intent.ACTION_CALL, uri);
				startActivity(it);
			}
		});
		
//		contactBean = new ContactBean();
//		
//		star = (ImageButton) findViewById(R.id.star);
//		if (contactBean.getStarred() == 1){
//			star.setImageResource((R.drawable.star_clicked));
//		}else
//		{
//			star.setImageResource((R.drawable.star));
//		}
		// Getting a reference to star image view and setting the correct
		// version represent if contact is a favorite or not
//		ImageView star = (ImageView) findViewById(R.id.star);
//		star.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (contactBean.getFavourite() == 0) {
//					star.setImageResource((R.drawable.star));
//					addKeepedContacts(getIntent().getStringExtra("ID"));
//				} else {
//					star.setImageResource((R.drawable.star_clicked));
//					removeKeepedContacts(getIntent().getStringExtra("ID"));
//				}
//			}
//		});
//		contactBean = new ContactBean();
		
//		contacts_imageView = (ImageView) findViewById(R.id.contacts_imageView);
		
		// getting a reference to the imageView and setting it. 
//		Intent intent = getIntent();
//		id = (int) intent.getIntExtra("id",0);
//		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);//位置
//		//获得照片returns the photo as a byte stream.
//		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uri);
//		if (null == input){
//			contacts_imageView = (ImageView) findViewById(R.id.contacts_imageView);
//		}else {
//		Bitmap contactPhoto = BitmapFactory.decodeStream(input);
//		byte[] photo = getIntent().getByteArrayExtra("photo");
//		Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
		contacts_imageView = (ImageView) findViewById(R.id.contacts_imageView);
//		contacts_imageView.setImageBitmap(contactPhoto);
		}
//		ContactBean contactbean = (ContactBean) getIntent().getSerializableExtra("photo");
		
//		contacts_imageView.setImageBitmap(contactbean.);
//		contacts_imageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Photo p = contactBean.getPhoto();
//				if (p == null)
//					return;
//				
//				FragmentManager fm = getSupportFragmentManager();
//				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
//				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
//			}
//		});
//		contacts_imageView.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(ConstactsDetailActivity.this, ConstactsImageCameraActivity.class);
//				startActivityForResult(i, REQUEST_PHOTO);
//				return true;
//			}
//		});

//	}
	
	/**
	* 添加到收藏夹
	* @param _id
	*/
	/*@SuppressWarnings("deprecation")
	public void addKeepedContacts(long _id){
	ContentResolver contentResolver = getContentResolver();
	Cursor cusor = null;
	@SuppressWarnings("deprecation")
	String[] projection = new String[] { Contacts.People._ID, Contacts.People.NAME, Contacts.People.NUMBER };
	cusor = contentResolver.query(Contacts.People.CONTENT_URI, projection, Contacts.People._ID + "= ", new String[] { _id + "" }, Contacts.People.NAME + " ASC");
	    cusor.moveToFirst();
	ContentValues values = new ContentValues();
	Uri uri = Uri.withAppendedPath(Contacts.People.CONTENT_URI, cusor.getString(cusor.getColumnIndex(Contacts.People._ID)));
	// values.put(Contacts.People.NAME, newName);
	values.put(Contacts.People.STARRED, 1);
	// values.put(Contacts.Phones.NUMBER, newPhoneNum);
	contentResolver.update(uri, values, null, null);
	Toast.makeText(this, this.getResources().getString(R.string.add_succeed), Toast.LENGTH_SHORT).show();
	}
	
	*//**
	* 从收藏夹中移出
	* @param _id
	*/
	/*@SuppressWarnings("deprecation")
	public void removeKeepedContacts(long _id){
	ContentResolver contentResolver = getContentResolver();
	Cursor cusor = null;
	@SuppressWarnings("deprecation")
	String[] projection = new String[] { Contacts.People._ID, Contacts.People.NAME, Contacts.People.NUMBER };
	    cusor = contentResolver.query(Contacts.People.CONTENT_URI, projection, Contacts.People._ID + "= ", new String[] { _id + "" }, Contacts.People.NAME + " ASC");
	cusor.moveToFirst();
	ContentValues values = new ContentValues();
	Uri uri = Uri.withAppendedPath(Contacts.People.CONTENT_URI, cusor.getString(cusor.getColumnIndex(Contacts.People._ID)));
	// values.put(Contacts.People.NAME, newName);
	values.put(Contacts.People.STARRED, 0);
	// values.put(Contacts.Phones.NUMBER, newPhoneNum);
	contentResolver.update(uri, values, null, null);
	new getKeepedContactsTask().execute((Void)null);
	Toast.makeText(ContactActivity.this, ContactActivity.this.getResources().getString(R.string.remove_succeed), Toast.LENGTH_SHORT).show();
	}*/
	
	public void ClickedImage(View view) {
		ImageView imageView = (ImageView) findViewById(R.id.star);
		if (ContactsContract.Contacts.STARRED +" =  1 " != null) {//,contactBean.getStarred() == 1
//			contactBean.setStarred(0);
//			contactBean.setFavourite(0);
//			ContentValues v = new ContentValues();
//			v.put(ContactsContract.Contacts.STARRED,0);
//			getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, 
//					v, ContactsContract.Contacts.Data._ID+"=?", 
//					new String[]{getIntent().getStringExtra("number")+""});
			imageView.setImageResource((R.drawable.star));
		} else {
			Log.v("star", "isclick");
//			contactBean.setFavourite(1);
//			contactBean.setStarred(1);
//			ContentValues v = new ContentValues();
//			v.put(ContactsContract.Contacts.STARRED,0);
//			getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, 
//					v, ContactsContract.Contacts.Data.DATA1+"=?", 
//					new String[]{getIntent().getStringExtra("number")+""});
			imageView.setImageResource((R.drawable.star_clicked));
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(1008 == requestCode){
			ContactsActivity.init();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles(ADD_CONTACTS, DEL_CONTACTS, EDIT_CONTACTS)//, "Item4"
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "click item index = " + index,
//				0).show();
		
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, 0).show();
//		 if (EDIT_ITEM_1)
	}

}

package com.zhy.dialtong.fragment.contacts;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.zhy.dialtong.ContactBean;
import com.zhy.dialtong.R;
import com.zhy.dialtong.view.QuickAlphabeticBar;

public class ContactHomeAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private List<ContactBean> list;
	private HashMap<String, Integer> alphaIndexer;//
	private String[] sections;//
	private Context ctx;
	
	public ContactHomeAdapter(Context context, List<ContactBean> list, QuickAlphabeticBar alpha) {
		
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list; 
		this.alphaIndexer = new HashMap<String, Integer>();//建一个新的hashMap表Constructs a new empty HashMap instance.
		this.sections = new String[list.size()];//List<ContactBean>,ContactBean有9个私有变量，list.size()=9
		//遍历表
		for (int i =0; i <list.size(); i++) {
			//get(i):Returns the element at the specified location in this List.
			String name = getAlpha(list.get(i).getSortKey());
			//getSortKey()：获得SortKey，String类型
			if(!alphaIndexer.containsKey(name)){ //false,代表map表不包含特殊键。
				//containsKey(name)：Returns whether this map contains the specified key.
				//true if this map contains the specified key, false otherwise.
				alphaIndexer.put(name, i);//Maps the specified key to the specified value.
				//put(name, i):Returns:
				//the value of any previous mapping with the specified key or null if there was no such mapping.
			}
		}
		//Set<String>:A Set is a data structure which does not allow duplicate elements.
		Set<String> sectionLetters = alphaIndexer.keySet();
		//keySet():Returns a set of the keys contained in this map. 
		//The set is backed by this map so changes to one are reflected by the other. The set does not support adding.

		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
		
		if(alpha != null){//设置右边sidebar导航联系
		alpha.setAlphaIndexer(alphaIndexer);
		//setAlphaIndexer:this.alphaIndexer = alphaIndexer;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void remove(int position){
		list.remove(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contacts_item_sort_listview, null);
			holder = new ViewHolder();
			holder.userImg = (QuickContactBadge) convertView.findViewById(R.id.userImg);
			holder.catalog = (TextView) convertView.findViewById(R.id.catalog);
			holder.nametitle = (TextView) convertView.findViewById(R.id.nametitle);
			holder.numbershow = (TextView) convertView
					.findViewById(R.id.numbershow);
//			holder.dialcount = (TextView) convertView.findViewById(R.id.dialcount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ContactBean cb = list.get(position);//获得列表的位置
		String name = cb.getDisplayName();//获得联系人姓名
		String dialcount = cb.getDialcount();
		String number = cb.getPhoneNum();//获得联系人号码
		holder.nametitle.setText(name);//设置名字显示
		holder.numbershow.setText(number);//设置号码显示
//		holder.dialcount.setText(dialcount);
		//assignContactUri():contact uri that this QuickContactBadge should be associated with.
		holder.userImg.assignContactUri(Contacts.getLookupUri(cb.getContactId(), cb.getLookUpKey()));//设置头像显示
		if(0 == cb.getPhotoId()){
			holder.userImg.setImageResource(R.drawable.user);
		}else{
			//withAppendedId():a new URI with the given ID appended(附加) to the end of the path
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cb.getContactId());//位置
			//获得照片returns the photo as a byte stream.
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uri); 
			//把byte stream类型的照片解码成Bitmap的形式
			Bitmap contactPhoto = BitmapFactory.decodeStream(input);//Decode(解码) an input stream into a bitmap
			//把Bitmap形式的照片传给userImg
			holder.userImg.setImageBitmap(contactPhoto);//设置头像显示
		}
		
//		if (cb.getFavourite() == 0) {
//			holder.star.setImageResource((R.drawable.star));
//		} else {
//			holder.star.setImageResource((R.drawable.star_clicked));
//		}
		
		// 
		String currentStr = getAlpha(cb.getSortKey());
//		// 
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(position - 1).getSortKey()) : " ";
		/**
		 * 
		 */
		if (!previewStr.equals(currentStr)) { // 显示设置联系人的排头，编成一组
			holder.catalog.setVisibility(View.VISIBLE);
			holder.catalog.setText(currentStr);
		} else {
			holder.catalog.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public static class ViewHolder {
		QuickContactBadge userImg;
		TextView catalog;
		TextView nametitle;
		TextView numbershow;
		TextView dialcount;
	}
	
//	public void ClickedImage(View view) {
//		ImageView imageView = (ImageView) findViewById(R.id.Star);
//		ContactBean cb = list.get(position);
//		
//		if (cb.getFavourite() == 1) {
//			imageView.setImageResource((R.drawable.star));
//			cb.setFavourite(0);
//		} else {
//			imageView.setImageResource((R.drawable.star_clicked));
//			cb.setFavourite(1);
//		}
//		
//	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<ContactBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	
	/**
	 * 获取到通讯录的排头，进行判断排序
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 
		} else {
			return "#";
		}
	}

}

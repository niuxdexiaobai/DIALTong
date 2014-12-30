/**
 * @encoding UTF-8
 */
package com.zhy.dialtong.fragment.recentcall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.dialtong.R;

/**
 * 提供通话记录ListView的视图适配。
 * <p>
 * 注意：由于当前的UI设计和类归档位置，ListView进入删除模式的方法在Adapter内部，
 * 而控制ListView退出删除模式的方法则在CallFragment里，两个类之间没有直接的属性继承关系，
 * 需要使用接口和额外的方法来实现通信。<br/>
 * 	当CallFragment需要通知Adapter切换到正常模式的时候，要先调用{@link onListViewModeChangedListener}的方法
 * 通知适配器，<strong>这时候不要切换界面显示。</strong>ListView更新视图后会再次通知Fragment，此时Fragment才应该
 * 执行UI的恢复工作。
 * </p>
 * @author Muyangmin
 * @create 2014-9-5
 * @version 1.0
 */
public final class CalllogAdapter extends BaseAdapter {
//		implements ListItemMultiCheckable, ListItemDeletable {
	private static final String LOG_TAG = "CalllogAdapter";
	
	/*
	 * 原有的、全部通话记录数据。
	 * list在执行refreshDistinctRecords()方法后会被置为null以减少内存占用。
	 */
	private ArrayList<CallRecord> list;				//
	private LayoutInflater inflater; 				//加载布局用
	/*
	 * 根据一定的规则过滤的单条数据，用于显示和控制。
	 * 当执行对某个联系人的删除操作后，distinctList中的数据也会删除，并调用notifyDataSetChanged()方法刷新UI。
	 * 也就是说，在刷新的时候list仍然为null，而新的列表数据是根据distinctList的数据来进行显示的。
	 * 如果UI要求刷新，则必须提供一个非空的list。
	 */
	private ArrayList<CallRecord> distinctList;		//
	
	private Context context;						//用于启动新的Activity
	private boolean isDeleteMode;
	//这里，由于每个Item都需要保存状态，故使用ArrayList并不会浪费空间，但要注意List的大小。
	private ArrayList<Boolean> itemSelectedList;		//用于保存已选择的项
//	private ListViewModeListener modeListener;	//用于通知全局视图
//	private OnCheckedItemChangedListener checkListener;
	private int currentCheckItemCount = 0;

	/**
	 * 尝试提高删除模式的性能。
	 */
	private SparseArray<View> views;
	/**
	 * 创建适配器。
	 * @param list 通话记录列表
	 * @param modeListener 接收ListView模式改变的接口
	 */
	public CalllogAdapter(Context context, ArrayList<CallRecord> list) {
		this.list = list;
		this.context = context;
		distinctList = new ArrayList<CallRecord>();
		refreshDistinctRecords();//按每个号码筛选记录，调用该方法之前须确保list和distinctList不为null。
		inflater = LayoutInflater.from(context);
//		views = new SparseArray<View>();
//		itemSelectedList = new ArrayList<Boolean>();
//		// 初始化所有checked box选中状态为false 
//		for (int i=0; i<getCount(); i++){
//			itemSelectedList.add(false);
//		}
	}
	
	/**
	 * 刷新列表的数据。
	 * @param list
	 */
	public void setDataList(ArrayList<CallRecord> list){
		if (list!=null){
			this.list = list;
			distinctList.clear();
			refreshDistinctRecords();
		}
		views.clear();
		itemSelectedList.clear();
		// 初始化所有checked box选中状态为false
		for (int i = 0; i < getCount(); i++) {
			itemSelectedList.add(false);
		}
		notifyDataSetChanged();
	}
	
//	public void setListViewModeListener(ListViewModeListener modeListener){
//		this.modeListener = modeListener;
//	}
//	public void setOnCheckedItemChangedListener(OnCheckedItemChangedListener listener){
//		this.checkListener = listener;
//	}
	//-----------------------实现ListItemMultiCheckable BEGIN----------------
//	@Override
//	public final void selectAll() {
//		for (int i=0; i<getCount(); i++){
//			requestChangeItemCheckState(i, true, false);//修改指定位置的Item的选中状态，负责处理程序请求的选定操作。
//
//		}
//	}
//	@Override
//	public final void deselectAll() {
//		for (int i=0; i<getCount(); i++){
//			requestChangeItemCheckState(i, false, false);//修改指定位置的Item的选中状态，负责处理程序请求的选定操作。
//
//		}
//	}
	//-----------------------实现ListItemMultiCheckable END----------------
	//-----------------------实现ListItemDeletable    BEGIN--------------	

//	@Override
//	public boolean isDeleteMode(){//判断ListView当前是否处于删除模式下（即供用户选择哪些数据进行删除）。
//		return isDeleteMode;
//	}
//	@Override
//	public void deleteAllCheckedItems(){//子类应当在该方法中实现对数据的删除，具体要删除的数据由子类决定。
//		CallLogManager manager = new CallLogManager(context);
//		//获取所有被选择的项并删除
//		for (int i=getCount()-1; i>0; i--){
//			if (itemSelectedList.get(i)){
//				CallRecord record = (CallRecord)getItem(i);
//				manager.deleteCallRecord(record.getNumber());
//				distinctList.remove(i);
//			}
//		}
//		//这里，list的值应该为null,但为了直观，将值显示传入，要求刷新数据。
//		setDataList(null);
//	}
//	@Override
//	public void requestExitDeleteMode(){//请求ListView退出删除模式。
//		for (int i=0; i<getCount(); i++){
//			Log.d(LOG_TAG, "i'm trying get"+i);
//			View view = views.get(i);
//			if (view!=null){
//				CheckBox checkBox = (CheckBox) view.findViewById(R.id.calllog_item_checkbox);
//				checkBox.setVisibility(View.INVISIBLE);
//				/* Note：这里无需立即清除删除列表，因为用户可能不会再点开。 */
//				ImageButton img_btndetail = (ImageButton)view.findViewById(R.id.calllog_item_detail);
//				img_btndetail.setVisibility(View.VISIBLE);
//			}
//		}
//		isDeleteMode = false;
//		if (modeListener!=null){
//			modeListener.onModeChanged(ListViewModeListener.MODE_VIEW);	
//		}
//	}
//
//	//进入删除界面
//	@Override
//	public void requestEnterDeleteMode(){
//		if (!isDeleteMode){	//如果当前不在删除模式，则进入
//			//TODO 1.隐藏拨号键盘
//			for (int i=0; i<getCount(); i++){
//				//初始化列表，全部为未选中状态。
//				requestChangeItemCheckState(i, false, false);
//				View view = views.get(i);
//				if (view!=null){
//					CheckBox checkBox = (CheckBox) view.findViewById(R.id.calllog_item_checkbox);
//					checkBox.setVisibility(View.VISIBLE);
//					ImageButton img_btndetail = (ImageButton)view.findViewById(R.id.calllog_item_detail);
//					img_btndetail.setVisibility(View.INVISIBLE);
//				}
//			}
//			isDeleteMode = true;
//			currentCheckItemCount = 0;	//重置选中的项数
//			if (modeListener!=null){
//				modeListener.onModeChanged(ListViewModeListener.MODE_DELETE);	
//			}
//		}
//	}
	//--------------------实现ListItemDeletable END-----------------
	@Override
	public View getView(final int position, View view, ViewGroup parent){
		view = views.get(position);
		if (view==null){
			view = createListItemView(position, view);
			Log.i(LOG_TAG, "i'm putting into sparsearray:"+position);
			views.put(position, view);
		}
		//因为数据全部被缓存了，所以无需创建和刷新。
		//数据的选择交由其他方法实现。
		//当数据集改变的时候，只需要把views清空，即可全部重绘。
		return view;
	}
	
	/**
	 * 创建ListView的每个Item项。
	 * @param position 位置
	 * @param view 视图的引用
	 * @return
	 */
	private View createListItemView(final int position, View view){
//		view = inflater.inflate(R.layout.common_slidelistview_item, null);//滑动ListView的每个子项添加左右滑动快速拨号和短信的效果
//		ListItemViewPager viewPager = (ListItemViewPager) view.findViewById(R.id.slidelist_viewpager);
//		ListItemMainView mainView = new ListItemMainView() {
//			private View mView = createCalllogView(position);
//			@Override
//			public void performMessageAction() {
//				if (!isDeleteMode){
//					Log.i(LOG_TAG, "performing msg.");	
//				}
//			}
//			
//			@Override
//			public void performCallAction() {
//				if (!isDeleteMode){
//					performCall(position);
//				}
//			}
//			
//			@Override
//			public View getView() {
//				return mView;
//			}
//		};
//		viewPager.setAdapter(new ListItemViewPagerAdapter(AppEnvironment.getApplicationContext(),
//				viewPager, mainView));
//		//总是显示主显视图
//		viewPager.setCurrentItem(1);
		return view;
	}
	/**
	 * 创建通话记录主视图。
	 * @param position
	 * @return
	 */
	private View createCalllogView(final int position){
		View view = inflater.inflate(R.layout.recent_call_list_item, null);
//		holder = new ViewHolder();
		ImageView call_type = (ImageView) view.findViewById(R.id.call_type);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView number = (TextView) view.findViewById(R.id.number);
		TextView time = (TextView) view.findViewById(R.id.time);
		TextView call_btn = (TextView) view.findViewById(R.id.call_btn);
		TextView count = (TextView) view.findViewById(R.id.count);
//		ImageView img_type = (ImageView)view.findViewById(R.id.calllog_item_type);
//		TextView tv_name = (TextView)view.findViewById(R.id.calllog_item_name);
//		TextView tv_phonenumber = (TextView)view.findViewById(R.id.calllog_item_phonenumber);
//		TextView tv_time = (TextView)view.findViewById(R.id.calllog_item_time);
//		ImageButton btn_detail = (ImageButton)view.findViewById(R.id.calllog_item_detail);
//		final CheckBox checkBox = (CheckBox)view.findViewById(R.id.calllog_item_checkbox);
		//初始化监听器，当其不可见的时候不会触发
//		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				requestChangeItemCheckState(position, isChecked, true);
//			}
//		});
//		//当前是否为删除模式
//		if (isDeleteMode){
//			checkBox.setVisibility(View.VISIBLE);
//			
//			btn_detail.setVisibility(View.INVISIBLE);
//			checkBox.setChecked(itemSelectedList.get(position));
//			
//		}
//		else{
//			btn_detail.setVisibility(View.VISIBLE);
//			checkBox.setVisibility(View.INVISIBLE);
//		}
		
		CallRecord record = distinctList.get(position);
		switch (record.getType()) {		
			case CALL_INCOMING:call_type.setImageResource(R.drawable.ic_calllog_incomming_normal);
				break;
			case CALL_MISSED:call_type.setImageResource(R.drawable.ic_calllog_missed_normal);
				break;
				//outgoing
			default:call_type.setImageResource(R.drawable.ic_calllog_outgoing_nomal);
				break;
		}
		
		String name1 = record.getCacheName();
		if (name1==null || name1.equals("")) {
			name.setText("陌生人");
		}
		else{
			name.setText(name1);	
		}
		number.setText(record.getNumber());
		time.setText(getTimeString(record.getDate()));
		//点击按钮启动详情界面
//		btn_detail.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (!isDeleteMode){
//					Intent intent = new Intent(context.getResources()
//							.getString(R.string.ACTION_VIEW_CALLLOGS));
//					CallRecord record = (CallRecord) getItem(position);
//					intent.putExtra(CalllogDetailActivity.INTENT_EXTRA_NUMBER, record.getNumber());
//					context.startActivity(intent);	
//				}
//			}
//		});
//		view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (!isDeleteMode){
//					performCall(position);	
//				}
//				else {
//					checkBox.setChecked(!checkBox.isChecked());
//				}
//			}
//		});
//		view.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				requestEnterDeleteMode();
//				return true;
//			}
//		});
		return view;
	}
	
	/**
	 * 修改指定位置的Item的选中状态，负责处理程序请求的选定操作。
	 * @param i 位置
	 * @param checked 是否选中
	 */
//	private void requestChangeItemCheckState(int i, boolean checked, boolean requestFromUI){
//		//忽略重复的请求[重复是万恶之源- -]
//		if (itemSelectedList.get(i)==checked){
//			return ;
//		}
//		itemSelectedList.set(i, checked);
//		currentCheckItemCount = checked? currentCheckItemCount+1 : currentCheckItemCount-1;
//		if (checkListener != null){
//			checkListener.onCheckedItemChanged(currentCheckItemCount);
//		}
//		//仅当程序请求事件时才发生UI变更，否则将造成Circle调用。
//		if (!requestFromUI){
//			View view = views.get(i);
//			//非缓存中的视图无需重绘
//			if (view != null){
//				CheckBox checkBox = (CheckBox) view.findViewById(R.id.calllog_item_checkbox);
//				checkBox.setChecked(checked);
//			}
//		}
//	}

	/**
	 * 执行实际的呼出电话的操作。
	 * @param position 列表中的位置
	 */
//	private synchronized void performCall(int position){
//		Log.i(LOG_TAG, "orz calling");
//		CallRecord record = distinctList.get(position);
//		PhoneCaller.makePhoneCall(context, record.getNumber());
//	}
	/**
	 * 负责计算并返回每条通话记录的时间。
	 * @param date 要计算的时间
	 * @return 返回计算后的时间字符串。
	 */
	private String getTimeString(long date){
		long now = System.currentTimeMillis();
		long period = now -date;
		if (period<0){
			return "来自Future";
		}
		if (period < 300000){//5分钟内
			return "刚刚";
		}
		if (period < 3600000){//1小时内
			return (period/60000)+"分钟前";
		}
		if (period < 86400000){//1天内
			return (period/3600000)+"小时前";
		}
		if (period < 172800000){
			return "昨天";
		}
		if (period < 259200000){
			return "前天";
		}
		return new SimpleDateFormat("M月d日", Locale.CHINA).format(new Date(date));
	}
	
	/**
	 * 按每个号码筛选记录，调用该方法之前须确保list和distinctList不为null。
	 */
	private void refreshDistinctRecords(){
		HashSet<String> numberset = new HashSet<String>();
		distinctList.clear();
		for (int i=0; i<list.size(); i++){
			CallRecord record = list.get(i);
			String number = record.getNumber();
			if (!numberset.contains(number)){
				numberset.add(number);
				distinctList.add(record);
			}
		}
		//将list置为空，减少内存消耗
		list.clear();
		list = null;
	}
	
	@Override
	public int getCount() {
		return distinctList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return distinctList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}

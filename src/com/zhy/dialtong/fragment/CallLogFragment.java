/**
 * @encoding UTF-8
 */
package com.zhy.dialtong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zhy.dialtong.R;
import com.zhy.dialtong.fragment.recentcall.CallLogManager;
import com.zhy.dialtong.fragment.recentcall.CalllogAdapter;

/**
 * @author Muyangmin
 * @create 2014-8-31
 * @version 1.0
 */
public class CallLogFragment extends Fragment {
	
	private static final String LOG_TAG = "CallLogFragment";
	private RelativeLayout rl_keyboard;
	private ListView listview;
	private boolean isKeyboardHidden = false;
	private Animation anim_fadein;
	private Animation anim_fadeout;
	private CalllogAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		anim_fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_keyboard_fadein);//动画键盘出现
//		anim_fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_keyboard_fadeout);//动画键盘淡出
		View view = inflater.inflate(R.layout.fragment_calllog, null);//实例化通话记录页面
		//默认展开拨号盘
//		getFragmentManager().beginTransaction().replace(R.id.calllog_keyboard_area, new DialFragment())
//		.commit();
		retrieveWidgets(view);//实例化通话记录的部件
		CallLogManager reader = new CallLogManager(getActivity());//getActivity()
		adapter = new CalllogAdapter(getActivity(), //getActivity()
				reader.getAllCallRecords());
//		adapter.setListViewModeListener(new ListViewModeListener() {
//			
//			@Override
//			public void onModeChanged(boolean mode) {
//				if (mode==MODE_VIEW){
//					//自动弹出键盘
//					if (isKeyboardHidden){
//						showKeyboard();
//					}
//					exitDeleteMode();
//				}
//				else{
//					//自动隐藏键盘
//					if (!isKeyboardHidden) {
//						hideKeyboard();
//					}
//					enterDeleteMode();
//				}
//			}
//		});
//		adapter.setOnCheckedItemChangedListener(new OnCheckedItemChangedListener() {
//			
//			@Override
//			public void onCheckedItemChanged(int checked) {
//				if (adapter.isDeleteMode()){
//					View view = getView();
//					TextView tv_title = (TextView) view.findViewById(R.id.calllog_tv_title);
//					tv_title.setText("删除已选("+checked+")");
//					if (checked <= 0){
//						tv_title.setEnabled(false);
//					}
//					else{
//						tv_title.setEnabled(true);
//					}
//				}
//			}
//		});
		listview.setAdapter(adapter);
//		listview.setOnScrollListener(new OnScrollListener() {
//			
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				if (!isKeyboardHidden){
//					hideKeyboard();
//				}
//			}
			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				//因为该方法在ListView初始化的时候会被调用若干次，
//				//故无法使用该方法达到首次滚动ListView隐藏拨号盘的效果
//			}
//		});
		return view;
	}
	
//	private void enterDeleteMode(){
//		View view = getView();
//		TextView tv_title_delete = (TextView) view.findViewById(R.id.calllog_tv_title);
//		TextView tv_exit = (TextView) view.findViewById(R.id.calllog_exitdelmode);
//		final TextView tv_selectAll = (TextView) view.findViewById(R.id.calllog_selectall);
//		tv_title_delete.setBackgroundResource(R.drawable.sel_global_titlebar_deletebtn_bg);
//		tv_title_delete.setText("删除已选(0)");
//		tv_title_delete.setEnabled(false);
//		tv_exit.setText(getResources().getString(R.string.global_ui_cancel));
//		tv_selectAll.setText(getResources().getString(R.string.global_ui_selectall));
//		tv_title_delete.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				adapter.deleteAllCheckedItems();
//				adapter.requestExitDeleteMode();
//			}
//		});
//		tv_exit.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				adapter.requestExitDeleteMode();
//			}
//		});
//		tv_selectAll.setOnClickListener(new OnClickListener() {
//			private boolean selected = false;
//			@Override
//			public void onClick(View v) {
//				if (selected){
//					adapter.deselectAll();
//					tv_selectAll.setText(getResources().getString(R.string.global_ui_selectall));
//				}
//				else{
//					adapter.selectAll();
//					tv_selectAll.setText(getResources().getString(R.string.global_ui_deselectall));
//				}
//				selected = !selected;
//			}
//		});
//		tv_exit.setVisibility(View.VISIBLE);
//		tv_selectAll.setVisibility(View.VISIBLE);
//	}
//	private void exitDeleteMode(){
//		View view = getView();
//		TextView tv_title = (TextView) view.findViewById(R.id.calllog_tv_title);
//		TextView tv_exit = (TextView) view.findViewById(R.id.calllog_exitdelmode);
//		TextView tv_selectAll = (TextView) view.findViewById(R.id.calllog_selectall);
//		tv_title.setText(getResources().getString(R.string.calllog_titlebar));
//		tv_title.setBackgroundResource(android.R.color.transparent);
//		tv_exit.setVisibility(View.INVISIBLE);
//		tv_selectAll.setVisibility(View.INVISIBLE);
//	}
	
	@Override
	public void onResume() {
		super.onResume();
		//修复从其他Fragment切到该界面时滑动ListView不隐藏Keyboard的问题
//		isKeyboardHidden = rl_keyboard.getVisibility()==View.INVISIBLE;
	}
	
	private void retrieveWidgets(View view){
//		rl_keyboard = (RelativeLayout)view.findViewById(R.id.calllog_keyboard_area);
		listview = (ListView)view.findViewById(R.id.calllog_listview);
	}
	
	//隐藏拨号键盘
//	private void hideKeyboard(){
//		rl_keyboard.startAnimation(anim_fadeout);
//		rl_keyboard.setVisibility(View.INVISIBLE);
//		isKeyboardHidden = true;
//	}
	//显示拨号键盘
//	public void showKeyboard(){
//		rl_keyboard.startAnimation(anim_fadein);
//		rl_keyboard.setVisibility(View.VISIBLE);
//		isKeyboardHidden = false;
//	}
	/**
	 * 反转键盘的显示状态。该方法仅供MainActivity使用。
	 */
//	protected void reverseKeyBoardVisibility(){
//		if (isKeyboardHidden){
//			showKeyboard();
//		}
//		else{
//			hideKeyboard();
//		}
//	}
}

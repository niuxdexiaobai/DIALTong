package com.zhy.dialtong.fragment.dial;

import java.util.List;

import android.content.Intent;

import com.zhy.dialtong.ContactBean;

public class MyApplication extends android.app.Application{
	
private List<ContactBean> contactBeanList;//添加一个变量
	
	public List<ContactBean> getContactBeanList() {//自动生成获取这个变量方法
		return contactBeanList;
	}
	public void setContactBeanList(List<ContactBean> contactBeanList) {//自动生成修改这个变量方法
		this.contactBeanList = contactBeanList;
	}

	public void onCreate() {
		
		System.out.println("项目启动");
		
		Intent startService = new Intent(MyApplication.this, T9Service.class);
		startService(startService);
	}

}

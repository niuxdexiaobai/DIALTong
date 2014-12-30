package com.zhy.dialtong.fragment.dial;

import java.util.List;

import android.content.Intent;

import com.zhy.dialtong.ContactBean;

public class MyApplication extends android.app.Application{
	
private List<ContactBean> contactBeanList;//���һ������
	
	public List<ContactBean> getContactBeanList() {//�Զ����ɻ�ȡ�����������
		return contactBeanList;
	}
	public void setContactBeanList(List<ContactBean> contactBeanList) {//�Զ������޸������������
		this.contactBeanList = contactBeanList;
	}

	public void onCreate() {
		
		System.out.println("��Ŀ����");
		
		Intent startService = new Intent(MyApplication.this, T9Service.class);
		startService(startService);
	}

}

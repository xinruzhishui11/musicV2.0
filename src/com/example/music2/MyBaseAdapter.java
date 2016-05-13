package com.example.music2;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	private List<T> data;
	private Context context;
	private LayoutInflater inflater;
	
	public MyBaseAdapter(Context context, List<T> data){
		super();
		setData(data);
		setContext(context);
		setLayoutInflater(context);
		
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public final List<T> getData() {
		return data;
	}

	public final void setData(List<T> data) {
		if(data == null){
			data = new ArrayList<T>();
			Log.w("edu", "数据集合为null");
		}
		this.data = data;
	}

	public final Context getContext() {
		return context;
	}

	public final void setContext(Context context) {
		if(context == null){
			throw new IllegalArgumentException("不能为空");
		}
		this.context = context;
	}

	public LayoutInflater getLayoutInflater() {
		return inflater;
	}

	private void setLayoutInflater(Context context) {
		inflater=LayoutInflater.from(context);
	}
	
	
	
	
	
	@Override
	public Object getItem(int position) {
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
}

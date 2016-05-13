package com.example.music2;

import java.util.List;

import android.app.Application;

public class MusicPlayerApplication extends Application {
	private List<Music> data;
	
	
	@Override
	public void onCreate() {
		IDAO<Music> dao=MusicPlayerFarctory.getInstance();
		dao.getData();
	}
	
	public List<Music> getData(){
		return data;
	}
}

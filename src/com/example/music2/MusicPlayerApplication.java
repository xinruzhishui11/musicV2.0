package com.example.music2;

import java.util.List;

import android.app.Application;

public class MusicPlayerApplication extends Application {
	private List<Music> musics;
	
	
	@Override
	public void onCreate() {
		IDAO<Music> dao=MusicPlayerFarctory.getInstance();
		musics=dao.getData();
	}
	
	public List<Music> getMusics(){
		return musics;
	}
}

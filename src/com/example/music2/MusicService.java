package com.example.music2;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service{
	private MediaPlayer player;
	private List<Music> musics;
	private MusicPlayerApplication app;
	
	@Override
	public void onCreate() {
		player=new MediaPlayer();
		app=(MusicPlayerApplication) getApplication();
		musics=app.getData();
	}

	@Override
	public IBinder onBind(Intent intent) {
		InnerBinder binder=new InnerBinder();
		
		return binder;
	}
	
	private class InnerBinder extends Binder {
		
	}
}

package com.example.music2;

import java.io.IOException;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service{
	private MediaPlayer player;
	private List<Music> musics;
	private MusicPlayerApplication app;
	//��ǰ���ֲ�������
	private int currentMusicIndex;
	//��������ͣ��λ��
	private int pauseposition;
	
	@Override
	public void onCreate() {
		//��service������ʱ��ͳ�ʼ��������
		player=new MediaPlayer();
		//��ø������ݼ���
		app=(MusicPlayerApplication) getApplication();
		musics=app.getMusics();
		
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				isEnding=true;
				pauseposition=0;
				next();
			}
		});
		
		Log.i("edu", "MusicService.onCreate");
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		//����binder����
		InnerBinder binder=new InnerBinder();
		
		return binder;
	}
	
	//�̳�binder����ʵ��IMusicPlayer�ӿ�
	private class InnerBinder extends Binder implements IMusicPlayer{

		@Override
		public void pause() {
			MusicService.this.pause();
		}

		@Override
		public void play() {
			MusicService.this.play();
		}

		@Override
		public void previous() {
			MusicService.this.previous();
		}

		@Override
		public void next() {
			MusicService.this.next();
		}

		@Override
		public int getCurrentPosition() {
			return player.getCurrentPosition();
		}

		@Override
		public int getDuration() {
			return player.getDuration();
		}

		@Override
		public boolean isPlaying() {
			return player.isPlaying();
		}

		@Override
		public void play(int position) {
			MusicService.this.play(position);
		}

		@Override
		public int getCurrentMusicIndex() {
			// TODO Auto-generated method stub
			return currentMusicIndex;
		}

		@Override
		public boolean autoPlayNext() {
			return MusicService.this.autoPlayNext();
		}

		@Override
		public void playFromCurrentPosition(int position) {
			MusicService.this.playFromCurrentPosition(position);
		}
		
	}
	private void pause(){
		player.pause();
		pauseposition=player.getCurrentPosition();
		
	}
	private void play(){
		try {
			//����
			player.reset();
			//���ò�����Դ
			player.setDataSource(musics.get(currentMusicIndex).getPath());
			//����
			player.prepare();
			//�������ͣ��λ��
			player.seekTo(pauseposition);
			//����
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void previous(){
		currentMusicIndex--;
		if(currentMusicIndex<0){
			currentMusicIndex=musics.size()-1;
			pauseposition=0;
			
		}
		play();
	}
	private void next(){
		currentMusicIndex++;
		if(currentMusicIndex>=musics.size()){
			currentMusicIndex=0;
			pauseposition=0;
		}
		play();
	}
	private void play(int position){
		currentMusicIndex=position;
		pauseposition=0;
		play();
	}
	
	private boolean isEnding;
	private boolean autoPlayNext(){
		 return isEnding;
	}
	
	private void playFromCurrentPosition(int position){
		pauseposition=position;
		play();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

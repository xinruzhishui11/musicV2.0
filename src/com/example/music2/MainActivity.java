package com.example.music2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	private ListView lvMusic;
	private List<Music> musics;
	private MusicAdapter musicAdapter;
	private MusicPlayerApplication app;
	private InnerServiceConnection conn;

	private IMusicPlayer player;

	// ��ͣ���߲���
	private ImageButton ibPauseOrPlay;
	// ��һ��
	private ImageButton ibPrevious;
	// ��һ��
	private ImageButton ibNext;
	
	//��ʾ��ǰ�������ű���
	private TextView tvMusicTitle;
	//��ʾ��ǰ��������ʱ��
	private TextView tvDuration;
	//���²���ʱ��
	private TextView tvCurrentPosition;
	
	
	//������
	private SeekBar sbProgress;
	
	//�߳�
	private UpdataProgressThread updataProgressThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ��service
		Intent service = new Intent(MainActivity.this, MusicService.class);
		conn = new InnerServiceConnection();
		bindService(service, conn, BIND_AUTO_CREATE);

		// ��ȡ���ݼ���
		app = (MusicPlayerApplication) getApplication();
		musics = app.getMusics();

		// ��ʼ���ؼ�
		setView();

		// Ϊ�ؼ����ü�����
		setlistener();
		// ��ListView���������
		musicAdapter = new MusicAdapter(MainActivity.this, musics);
		lvMusic.setAdapter(musicAdapter);
	}

	private class InnerServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// �Ѿ���������ʱ���ø÷���
			player = (IMusicPlayer) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// �Ѿ��Ͽ�����ʱ���ø÷���
		}

	}

	private void setView() {
		lvMusic = (ListView) findViewById(R.id.lv_musics);

		ibPauseOrPlay = (ImageButton) findViewById(R.id.ib_music_play_or_pause);
		ibPrevious = (ImageButton) findViewById(R.id.ib_music_previous);
		ibNext = (ImageButton) findViewById(R.id.ib_music_next);
		
		tvMusicTitle=(TextView) findViewById(R.id.tv_music_title);
		tvDuration=(TextView) findViewById(R.id.tv_music_duration);
		
		sbProgress=(SeekBar) findViewById(R.id.sb_progress);
		tvCurrentPosition=(TextView) findViewById(R.id.tv_music_current_position);
		
	}
	
	
	
	private void setlistener() {
		ibPauseOrPlay.setOnClickListener(this);
		ibPrevious.setOnClickListener(this);
		ibNext.setOnClickListener(this);
		
		OnItemClickListener listener = new InnerOnItemClickListener();
		lvMusic.setOnItemClickListener(listener);
	}
	
	private class InnerOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			player.play(position);
			showMusicInfo(player.getDuration());
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_music_play_or_pause:
			if(player.isPlaying()){
				player.pause();
				ibPauseOrPlay.setImageResource(android.R.drawable.ic_media_play);
				if(updataProgressThread!=null){
					isRunning=false;
					updataProgressThread=null;
				}
			}else{
				player.play();
				showMusicInfo(player.getDuration());
				
				updataProgressThread=new UpdataProgressThread();
				updataProgressThread.start();
			}
			break;
		case R.id.ib_music_previous:
			player.previous();
			showMusicInfo(player.getDuration());
			break;
		case R.id.ib_music_next:
			player.next();
			showMusicInfo(player.getDuration());
			break;
		}
	}
	
	
	private void showMusicInfo(int millis){
		tvMusicTitle.setText(musics.get(player.getCurrentMusicIndex()).getTitle());
		tvDuration.setText(dataFormatedTimer(millis));
		ibPauseOrPlay.setImageResource(android.R.drawable.ic_media_pause);
		openThread();
	}
	
	private String dataFormatedTimer(int millis){
		SimpleDateFormat sdf=new SimpleDateFormat("mm:ss",Locale.CHINA);
		Date date=new Date();
		date.setTime(millis);
		return sdf.format(date);
	}

	@Override
	protected void onDestroy() {
		// �����
		unbindService(conn);
		super.onDestroy();
	}
	
	//״̬��ʶ�̵߳�����״̬
	private boolean isRunning;
	private class UpdataProgressThread extends Thread{
		int currentPosition;
		int percent;
		@Override
		public void run() {
			Runnable runnable=new Runnable() {
				
				@Override
				public void run() {
					//���½�����
					sbProgress.setProgress(percent);
					//����ʱ��
					tvCurrentPosition.setText(dataFormatedTimer(currentPosition));
				}
			};
			while(isRunning){
				try {
					currentPosition=player.getCurrentMusicIndex();
					int duration=player.getDuration();
					percent=duration==0 ? 0:currentPosition*100/duration;
					runOnUiThread(runnable);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void openThread(){
		if(updataProgressThread==null){
			updataProgressThread=new UpdataProgressThread();
			isRunning=true;
			updataProgressThread.start();
		}
	}
	
}

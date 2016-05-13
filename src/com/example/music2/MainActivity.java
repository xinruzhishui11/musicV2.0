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

	// 暂停或者播放
	private ImageButton ibPauseOrPlay;
	// 上一首
	private ImageButton ibPrevious;
	// 下一首
	private ImageButton ibNext;
	
	//显示当前歌曲播放标题
	private TextView tvMusicTitle;
	//显示当前歌曲的总时长
	private TextView tvDuration;
	//更新播放时间
	private TextView tvCurrentPosition;
	
	
	//进度条
	private SeekBar sbProgress;
	
	//线程
	private UpdataProgressThread updataProgressThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 绑定service
		Intent service = new Intent(MainActivity.this, MusicService.class);
		conn = new InnerServiceConnection();
		bindService(service, conn, BIND_AUTO_CREATE);

		// 获取数据集合
		app = (MusicPlayerApplication) getApplication();
		musics = app.getMusics();

		// 初始化控件
		setView();

		// 为控件设置监听器
		setlistener();
		// 给ListView添加适配器
		musicAdapter = new MusicAdapter(MainActivity.this, musics);
		lvMusic.setAdapter(musicAdapter);
	}

	private class InnerServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 已经建立连接时调用该方法
			player = (IMusicPlayer) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 已经断开连接时调用该方法
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
		// 解除绑定
		unbindService(conn);
		super.onDestroy();
	}
	
	//状态标识线程的运行状态
	private boolean isRunning;
	private class UpdataProgressThread extends Thread{
		int currentPosition;
		int percent;
		@Override
		public void run() {
			Runnable runnable=new Runnable() {
				
				@Override
				public void run() {
					//更新进度条
					sbProgress.setProgress(percent);
					//更新时间
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

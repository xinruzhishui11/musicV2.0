package com.example.music2;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends Activity {
	private ListView lvMusic;
	private List<Music> musics;
	private MusicAdapter musicAdapter;
	private MusicPlayerApplication app;
	private InnerServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //绑定service
        Intent service = new Intent(MainActivity.this, MusicService.class);
		conn =new  InnerServiceConnection();
		bindService(service, conn, BIND_AUTO_CREATE);
        
        
        //获取数据集合
        app=(MusicPlayerApplication) getApplication();
        musics=app.getData();
        
        //初始化控件
        setView();
        //给ListView添加适配器
        musicAdapter=new MusicAdapter(MainActivity.this, musics);
        lvMusic.setAdapter(musicAdapter);
    }
    
    private class InnerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
    	
    }

	private void setView() {
		lvMusic=(ListView) findViewById(R.id.lv_musics);
	}
}









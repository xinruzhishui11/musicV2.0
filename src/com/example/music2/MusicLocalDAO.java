package com.example.music2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

public class MusicLocalDAO implements IDAO<Music> {
	private List<Music> musics;
	public List<Music> getData(){
		musics=new ArrayList<Music>();
		String s=Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(s)){
			File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			if(musicDir.exists()){
				File[] files = musicDir.listFiles();
				if(files.length>0 && files!= null){
					for (File file : files) {
						if(file.isFile()){
							String musicName = file.getName();
							if(musicName.toLowerCase(Locale.CHINA).endsWith(".mp3")){
								Music music=new Music();
								music.setTitle(musicName.substring(0, musicName.length()-4));
								music.setPath(file.getAbsolutePath());
								musics.add(music);
							}
						}
					}
				}
			}
		}
		Log.d("edu", "length"+musics.size());
		return musics;
	}

}

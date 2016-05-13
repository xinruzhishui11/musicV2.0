package com.example.music2;


public class MusicPlayerFarctory {
	private MusicPlayerFarctory(){
		
	}
	
	public static IDAO<Music> getInstance(){
		return new MusicLocalDAO();
	}
}

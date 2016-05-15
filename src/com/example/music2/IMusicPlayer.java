package com.example.music2;

public interface IMusicPlayer {
	void pause();
	void play();
	void previous();
	void next();
	int getCurrentPosition();
	int getDuration();
	boolean isPlaying();
	void play(int position);
	int getCurrentMusicIndex();
	boolean autoPlayNext();
	void playFromCurrentPosition(int position);
	
	
}

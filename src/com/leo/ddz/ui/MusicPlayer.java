package com.leo.ddz.ui;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * @author Leo
 *
 */
public class MusicPlayer {

	public MusicPlayer(Context context) {
		this.context = context;
	}

	public void play(int streamResourceId) {

		stop();

		player = MediaPlayer.create(context, streamResourceId);

		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				player.release();
			}
		});

		player.start();
	}

	public void loopPlay(int streamResourceId) {

		stop();

		player = MediaPlayer.create(context, streamResourceId);

		player.setLooping(true);

		player.start();
	}

	public void stop() {

		if (player == null)
			return;

		try {
			player.stop();
		} catch (IllegalStateException ignored) {
		}

		try {
			player.release();
		} catch (Exception ignored) {
		}
	}

	public boolean isPlaying() {
		if (player != null) {
			return player.isPlaying();
		} else {
			return false;
		}
	}

	public void pause(){
		if (player!=null && player.isPlaying())
			player.pause();
	}
	public void resume(){
		if (player!=null)
			player.start();
	}
	private MediaPlayer player;
	private Context context;
}

package com.leo.ddz;

import com.leo.ddz.R;
import com.leo.ddz.data.Config;
import com.leo.ddz.ui.MessagePane;
import com.leo.ddz.ui.MusicPlayer;
import com.leo.ddz.ui.SoundPoolManager;
import com.leo.ddz.ui.skin.Skin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

/**
 * 启动主界面
 * @author Leo
 * 
 */
public class StartupActivity extends Activity {

	private MusicPlayer bgMusic;
	private Config config;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);

		findViewById(R.id.start_game).setOnClickListener(onStartGame);
		findViewById(R.id.game_about).setOnClickListener(onAbout);

		Skin.getInstance(this);
		SoundPoolManager.getInstance(this);
		bgMusic = new MusicPlayer(this);
	}

	protected void onResume() {

		super.onResume();

		config = new Config(this);
		if (config.isPlayBgSound())
			bgMusic.loopPlay(R.raw.home_backgroud);

	}


	protected void onPause() {

		if (config.isPlayBgSound()) {
			bgMusic.stop();
		}
		super.onPause();
	}

	private void startGame() {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}


	private View.OnClickListener onStartGame = new View.OnClickListener() {
		public void onClick(View v) {
			SoundPoolManager.getInstance(StartupActivity.this).play(
					SoundPoolManager.ANJIAN);
			startGame();
		}
	};
	private View.OnClickListener onAbout = new View.OnClickListener() {
		public void onClick(View v) {
			SoundPoolManager.getInstance(StartupActivity.this).play(
					SoundPoolManager.ANJIAN);
			MessagePane.show(StartupActivity.this, MessagePane.ABOUT_FILE);
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}

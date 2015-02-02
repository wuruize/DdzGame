package com.leo.ddz;


import com.leo.ddz.R;
import com.leo.ddz.data.Config;
import com.leo.ddz.ui.MessagePane;
import com.leo.ddz.ui.MusicPlayer;
import com.leo.ddz.ui.SoundPoolManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * πÿ”⁄”Œœ∑
 * @author Leo
 *
 */
public class GameAboutAcitvity extends Activity {

    private MusicPlayer bgMusic;
    Config config;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_about);

        View view = findViewById(R.id.back);
        view.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                    	SoundPoolManager.getInstance(GameAboutAcitvity.this).play(SoundPoolManager.ANJIAN);
                        finish();
                    }
                }
        );
        setTextContent();
        bgMusic = new MusicPlayer(this);
    }
    protected void onResume() {
    	 super.onResume();
        config = new Config(this);
        if(config.isPlayBgSound()) {
            bgMusic.loopPlay(R.raw.home_backgroud);
        }
    }
	protected void onPause() {

		if (config.isPlayBgSound()) {
			bgMusic.stop();
		}
		super.onPause();
	}
    private void setTextContent() {

        String content = getIntent().getStringExtra(MessagePane.KEY_CONTENT);
        TextView tvContent = (TextView) findViewById(R.id.content);
        tvContent.setText(content);
    }
}

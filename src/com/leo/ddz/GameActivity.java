package com.leo.ddz;

import java.util.HashMap;
import java.util.Map;

import com.leo.ddz.R;
import com.leo.ddz.data.Config;
import com.leo.ddz.model.DdzView;
import com.leo.ddz.model.GameMsg;
import com.leo.ddz.model.Player;
import com.leo.ddz.ui.MusicPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
/**
 * 斗地主主面板
 * @author Leo
 *
 */
public class GameActivity extends Activity {
	Config config;
	GameMsg gameMsg;
	private MusicPlayer bgMusic;
	DdzView ddz ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(layoutResID)context
        config = new Config(this);
        config.load(this);
        gameMsg = new GameMsg();
        initUserInfo(gameMsg);
        
        ddz = new DdzView(this,gameMsg);
        LinearLayout.LayoutParams  layout = new LinearLayout.LayoutParams(800, 454);
        layout.setMargins(0, 26, 0, 0);
        ddz.setLayoutParams(layout);
        setContentView(ddz);
        
        bgMusic = new MusicPlayer(this);
        ddz.setConfig(config);
        ddz.setBgMusic(bgMusic);
        if(!config.isPlayBgSound() && !config.isPlaySound()){
        	ddz.setPlaySounds(false);
        }
        ddz.setPokeBack(BitmapFactory.decodeResource(getResources(), R.drawable.back));
        ddz.setBackgroundColor(Color.GRAY);
        ddz.startGame();
    }
	@Override
    protected void onStop() {
    	Player[] allPlayer = gameMsg.getAllPlayer();
    	Map<String, Integer> userInfo = new HashMap<String, Integer>();
    	userInfo.put(allPlayer[1].getUserName(), allPlayer[1].getIntegral());
    	userInfo.put(allPlayer[2].getUserName(), allPlayer[2].getIntegral());
    	userInfo.put(Config.HOST_INTEGRAL, allPlayer[0].getIntegral());
    	config.saveUserItegral(userInfo, this);
    	super.onStop();
    }
    
	protected void onResume() {
        super.onResume();
        config = new Config(this);
        if(config.isPlayBgSound()) {
            bgMusic.loopPlay(R.raw.game_backgroud);
        }
    }
	protected void onPause() {

		config = new Config(this);
		if (config.isPlayBgSound()) {
			bgMusic.stop();
		}
		ddz.setPlaySounds(false);
		super.onPause();
	}

    public void back(){
    	Intent intent = new Intent(this, StartupActivity.class);
		startActivity(intent);
    }
    private void initUserInfo(GameMsg gameMsg){
    	Context context = GameActivity.this;
    	String[] playerNames = {
    			 context.getString(R.string.userName1),
    			 context.getString(R.string.userName2),
    			 context.getString(R.string.userName3),
    			 context.getString(R.string.userName4),
    			 context.getString(R.string.userName5),
    			 context.getString(R.string.userName6),
    	}; 
    	gameMsg.initMsg(config);
    	gameMsg.setDifficulty(config.getDifficulty());
    	Map<String, Integer> userInfo = config.loadUserItegral(this,playerNames);
    	
    	Player[] allPlayer = new Player[3];
    	Player p0 = new Player();
		p0.setIntegral(userInfo.get(Config.HOST_INTEGRAL));
		p0.setUserName(getString(R.string.wanjia));
		allPlayer[0] = p0;
		
		int random = ((int)(Math.random()*10))%playerNames.length;
		Player p1 = new Player();
		p1.setIntegral(userInfo.get(playerNames[random]));
		p1.setUserName(playerNames[random]);
		allPlayer[1] = p1;
		
		random++;
		if(random>=playerNames.length){
			random = 2;
		}
		Player p2 = new Player();
		p2.setIntegral(userInfo.get(playerNames[random]));
		p2.setUserName(playerNames[random]);
		allPlayer[2] = p2;
		gameMsg.setAllPlayer(allPlayer);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
        
}
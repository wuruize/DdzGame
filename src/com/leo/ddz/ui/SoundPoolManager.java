package com.leo.ddz.ui;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

import com.leo.ddz.R;
import com.leo.ddz.data.Config;

/**
 * 声音管理类
 * @author Leo
 *
 */
public class SoundPoolManager {

    /**炸弹 */
    public static final int BOOM = 1;
    /**叫分 */
    public static final int CALLSCORE = 2;
    /**计分 */
    public static final int CALSCORE = 3;
    /** 选牌*/
    public static final int CHOSE = 4;
    /**出牌 */
    public static final int CHUPAI = 5;
    /**倒 */
    public static final int DGF = 6;
    /**发牌 */
    public static final int FAPAI = 7;
    /**亮牌  */
//    public static final int LIANGPAI = 8;
    /**输*/
    public static final int LOSE = 8;
    /**不要 */
    public static final int PASS = 9;
    /**火箭 */
    public static final int ROCKET = 10;
    /**亮底牌  */
    public static final int SHOWBOTTOM = 11;
    /**赢*/
    public static final int WIN = 12;
    /**按键*/
    public static final int ANJIAN = 13;
    /**按键*/
    public static final int ANJIAN_2 = 14;
    /**排序*/
    public static final int SORT = 15;
    /**点击声音开关按钮*/
    public static final int CLICK_MUSIC_BTN = 16;
    private MediaPlayer mp ;

    private static SoundPoolManager instance;

    public static SoundPoolManager getInstance(Context context) {
        if (instance == null)
            instance = new SoundPoolManager(context);
        return instance;
    }

    private SoundPoolManager(Context context) {
        this.context = context;
        soundMap = new HashMap<Integer, Integer>();
        pool = new SoundPool(16, AudioManager.STREAM_RING, 0);
     
        soundMap.put(BOOM, pool.load(context, R.raw.boom, 1));
        soundMap.put(CALLSCORE, pool.load(context, R.raw.callscore, 1));
        soundMap.put(CALSCORE, pool.load(context, R.raw.calscore, 1));
        soundMap.put(CHOSE, pool.load(context, R.raw.chose, 1));
        soundMap.put(CHUPAI, pool.load(context, R.raw.chupai, 1));
        soundMap.put(DGF, pool.load(context, R.raw.dgf, 1));
        soundMap.put(FAPAI, pool.load(context, R.raw.fapai, 1));
        soundMap.put(LOSE, pool.load(context, R.raw.lose, 1));
        soundMap.put(PASS, pool.load(context, R.raw.pass, 1));
        soundMap.put(ROCKET, pool.load(context, R.raw.rocket, 1));
        soundMap.put(SHOWBOTTOM, pool.load(context, R.raw.showbottom, 1));
        soundMap.put(WIN, pool.load(context, R.raw.win, 1));
        soundMap.put(ANJIAN, pool.load(context, R.raw.anjian, 1));
        soundMap.put(ANJIAN_2, pool.load(context, R.raw.anjian_2, 1));
        soundMap.put(SORT, pool.load(context, R.raw.sort, 1));
        soundMap.put(CLICK_MUSIC_BTN, pool.load(context, R.raw.click_music_btn, 1));
    }

    public void play(int sound) {
    	 float volume = getVolume();
    	Config config = new Config(context);
    	if(!config.isPlaySound()) return ;
        Integer streamId = soundMap.get(sound);
        pool.play(streamId, volume, volume, 1, 0, 1f);
    }

    public void playBackground() {
	   if(mp==null){
       		mp =  MediaPlayer.create(context, R.raw.fapai);
       }
    	mp.setLooping(true);
    	mp.start();
    }

    public void stopBackground() {
    	if(mp!=null){
    		mp.stop();
    		mp = null;
    	}
    	
    }
    public void stop(int sound) {
        Integer streamId = soundMap.get(sound);
        pool.stop(streamId);
    }
    
	private float getVolume() {
		AudioManager mgr = (AudioManager)
		context.getSystemService(Context.AUDIO_SERVICE);
	
		int volume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		int maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	
		return (float) volume / maxVolume;
	}

    private SoundPool pool;
    private Map<Integer, Integer> soundMap;
    private Context context;
}

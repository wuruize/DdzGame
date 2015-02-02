package com.leo.ddz.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

	public static final String PLAY_SOUND = "play_sound";
	public static final String PLAY_BG_SOUND = "play_bg_sound";
	public static final String BACKGROUD = "head_image_index";
	public static final String HOST_INTEGRAL = "host_integral";
	// public static final String HOST_USERNAME = "host_username";
	public static final String DIFFICULTY = "difficulty";
	public static final String POKE_TYPE = "poke_type";
	private boolean playSound;
	private boolean playBgSound;
	private int backgroud;
	private int pokeType;
	private int integral;
	// private String username;
	private int difficulty;


	public Config(Context context) {
		load(context);
	}

	public void load(Context context) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		setPlaySound(setting.getBoolean(PLAY_SOUND, true));
		setPlayBgSound(setting.getBoolean(PLAY_BG_SOUND, true));
		setBackgroud(setting.getInt(BACKGROUD, 0));
		setPokeType(setting.getInt(POKE_TYPE, 1));
		setDifficulty(setting.getInt(DIFFICULTY, 10));
	}

	@SuppressWarnings("unchecked")
	public void saveUserItegral(Map<String, Integer> userMap, Context context) {
		if (userMap.size() == 0)
			return;
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = setting.edit();
		Iterator it = userMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String userName = (String) entry.getKey();
			Integer itegral = (Integer) entry.getValue();
			editor.putInt(userName, itegral);
		}
		editor.commit();
	}

	public Map<String, Integer> loadUserItegral(Context context,String[] playerNames) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		Map<String, Integer> userMap = new HashMap<String, Integer>();
		userMap.put(HOST_INTEGRAL, setting.getInt(HOST_INTEGRAL, 1000));
		for (int i = 0; i < playerNames.length; i++) {
			String userName = playerNames[i];
			userMap.put(userName, setting.getInt(userName, 1000));
		}
		return userMap;
	}

	public void save(Context context) {

		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);

		SharedPreferences.Editor editor = setting.edit();
		editor.putBoolean(PLAY_SOUND, playSound);
		editor.putBoolean(PLAY_BG_SOUND, playBgSound);
		editor.putInt(BACKGROUD, backgroud);
		editor.putInt(POKE_TYPE, pokeType);
		editor.commit();
	}

	public void savePlaySound(boolean isPlaySound,Context context) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = setting.edit();
		editor.putBoolean(PLAY_SOUND, isPlaySound);
		editor.commit();
	}

	public boolean isPlaySound() {
		return playSound;
	}

	public void setPlaySound(boolean playSound) {
		this.playSound = playSound;
	}

	public boolean isPlayBgSound() {
		return playBgSound;
	}

	public void setPlayBgSound(boolean playBgSound) {
		this.playBgSound = playBgSound;
	}

	public int getBackgroud() {
		return backgroud;
	}

	public void setBackgroud(int backgroud) {
		this.backgroud = backgroud;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getPokeType() {
		return pokeType;
	}

	public void setPokeType(int pokeType) {
		this.pokeType = pokeType;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

}
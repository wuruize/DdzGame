package com.leo.ddz.ui.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

public abstract class Skin {

	Context context;
	/**  牌*/
	Bitmap[][] pokeImage;
	/** 排序按钮*/
	Bitmap[] sortImage;
	Bitmap gameOverBitMap;
	Bitmap[] continueBtnImage;
	Bitmap[] exitBtnImage;
	/** "不叫"两字*/
	Bitmap bjzImage;
	/** "不出"两字*/
	Bitmap bczImage;
	/**倒*/
	Bitmap[] daoImage;
	/**跟*/
	Bitmap[] genImage;
	/**反*/
	Bitmap[] fanImage;
	/**农民、地主头像*/
	Bitmap[] headImage;
	/** 音乐设置*/
	Bitmap[] musicBtnImage;
	/**显示叫分值和炸弹数栏*/
	Bitmap calScoreBar;
	/** 不叫按钮*/
	Bitmap[] bjImage;
	/** 不出按钮*/
	Bitmap[] bcImage;
	/** 出牌*/
	Bitmap[] cpImage;
	/** 一分*/
	Bitmap[] oneImage;
	/** 两分*/
	Bitmap[] twoImage;
	/** 三分*/
	Bitmap[] threeImage;
	/**倒、跟、反状态显示*/
	Bitmap daoStatus;
	Bitmap genStatus;
	Bitmap fanStatus;
	/**动画*/
	Bitmap[] passAnim;
	Bitmap[] daoAnim;
	Bitmap[] genAnim;
	Bitmap[] fanAnim;
	Bitmap[] onePointAnim;
	Bitmap[] twoPointAnim;
	Bitmap[] threePointAnim;
	Bitmap[] noCallAnim;		//不叫
	Bitmap nobodyCallScoreImage;
	Bitmap[] rocketAnim;
	Bitmap[] numbers;
	Bitmap[] gradeNameImage;
	
	/**画显示牌张数paint*/
	Paint paiCountPaint = null;
	/**画显示牌张数paint*/
	Paint remainPokeCount = null;
	
	public static Skin getInstance(Context context){
		return DefaultSkin.getInstance(context);
	}
	public abstract void init();
	
	public Bitmap[][] getPokeImage() {
		return pokeImage;
	}
	public Bitmap[] getSortImage() {
		return sortImage;
	}
	public Bitmap getGameOverBitMap() {
		return gameOverBitMap;
	}
	public Bitmap[] getContinueBtnImage() {
		return continueBtnImage;
	}
	public Bitmap[] getExitBtnImage() {
		return exitBtnImage;
	}
	public Bitmap getBjzImage() {
		return bjzImage;
	}
	public Bitmap getBczImage() {
		return bczImage;
	}
	public Bitmap[] getDaoImage() {
		return daoImage;
	}
	public Bitmap[] getGenImage() {
		return genImage;
	}
	public Bitmap[] getFanImage() {
		return fanImage;
	}
	
	public Bitmap[] getheadImage() {
		return headImage;
	}
	public Bitmap getCalScoreBar() {
		return calScoreBar;
	}
	public Bitmap[] getBjImage() {
		return bjImage;
	}
	public Bitmap[] getBcImage() {
		return bcImage;
	}
	public Bitmap[] getCpImage() {
		return cpImage;
	}
	public Bitmap[] getOneImage() {
		return oneImage;
	}
	public Bitmap[] getTwoImage() {
		return twoImage;
	}
	public Bitmap[] getThreeImage() {
		return threeImage;
	}
	public Bitmap getDaoStatus() {
		return daoStatus;
	}
	public Bitmap getGenStatus() {
		return genStatus;
	}
	public Bitmap getFanStatus() {
		return fanStatus;
	}
	public Bitmap[] getPassAnim() {
		return passAnim;
	}
	public Bitmap[] getDaoAnim() {
		return daoAnim;
	}
	public Bitmap[] getGenAnim() {
		return genAnim;
	}
	public Bitmap[] getFanAnim() {
		return fanAnim;
	}
	public Bitmap[] getRocketAnim() {
		return rocketAnim;
	}
	public void setGameOverBitMap(Bitmap gameOverBitMap) {
		this.gameOverBitMap = gameOverBitMap;
	}
	public Paint getPaiCountPaint() {
		return paiCountPaint;
	}
	public Paint getRemainPokeCount() {
		return remainPokeCount;
	}
	public Bitmap[] getNumbers() {
		return numbers;
	}
	public Bitmap[] getGradeNameImage() {
		return gradeNameImage;
	}
	public Bitmap[] getOnePointAnim() {
		return onePointAnim;
	}
	public Bitmap[] getTwoPointAnim() {
		return twoPointAnim;
	}
	public Bitmap[] getThreePointAnim() {
		return threePointAnim;
	}
	public Bitmap[] getNoCallAnim() {
		return noCallAnim;
	}
	public Bitmap[] getMusicBtnImage() {
		return musicBtnImage;
	}
	
}

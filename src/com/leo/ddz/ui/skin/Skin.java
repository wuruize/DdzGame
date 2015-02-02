package com.leo.ddz.ui.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

public abstract class Skin {

	Context context;
	/**  ��*/
	Bitmap[][] pokeImage;
	/** ����ť*/
	Bitmap[] sortImage;
	Bitmap gameOverBitMap;
	Bitmap[] continueBtnImage;
	Bitmap[] exitBtnImage;
	/** "����"����*/
	Bitmap bjzImage;
	/** "����"����*/
	Bitmap bczImage;
	/**��*/
	Bitmap[] daoImage;
	/**��*/
	Bitmap[] genImage;
	/**��*/
	Bitmap[] fanImage;
	/**ũ�񡢵���ͷ��*/
	Bitmap[] headImage;
	/** ��������*/
	Bitmap[] musicBtnImage;
	/**��ʾ�з�ֵ��ը������*/
	Bitmap calScoreBar;
	/** ���а�ť*/
	Bitmap[] bjImage;
	/** ������ť*/
	Bitmap[] bcImage;
	/** ����*/
	Bitmap[] cpImage;
	/** һ��*/
	Bitmap[] oneImage;
	/** ����*/
	Bitmap[] twoImage;
	/** ����*/
	Bitmap[] threeImage;
	/**����������״̬��ʾ*/
	Bitmap daoStatus;
	Bitmap genStatus;
	Bitmap fanStatus;
	/**����*/
	Bitmap[] passAnim;
	Bitmap[] daoAnim;
	Bitmap[] genAnim;
	Bitmap[] fanAnim;
	Bitmap[] onePointAnim;
	Bitmap[] twoPointAnim;
	Bitmap[] threePointAnim;
	Bitmap[] noCallAnim;		//����
	Bitmap nobodyCallScoreImage;
	Bitmap[] rocketAnim;
	Bitmap[] numbers;
	Bitmap[] gradeNameImage;
	
	/**����ʾ������paint*/
	Paint paiCountPaint = null;
	/**����ʾ������paint*/
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

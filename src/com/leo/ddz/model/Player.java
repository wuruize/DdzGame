package com.leo.ddz.model;

/**
 * ���
 * @author Leo
 *
 */
public class Player
{
	/**����*/
	private String userName;
	/**ͷ��*/
	private int headImageIndex;
	/**����*/
	private int integral;
	/**��ǰһ��������ҵĵ÷�*/
	private int currGameScoring;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getHeadImageIndex() {
		return headImageIndex;
	}
	public void setHeadImageIndex(int headImageIndex) {
		this.headImageIndex = headImageIndex;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	
	public int getCurrGameScoring() {
		return currGameScoring;
	}
	public void setCurrGameScoring(int currGameScoring) {
		this.currGameScoring = currGameScoring;
	}
}

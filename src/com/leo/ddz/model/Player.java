package com.leo.ddz.model;

/**
 * 玩家
 * @author Leo
 *
 */
public class Player
{
	/**姓名*/
	private String userName;
	/**头像*/
	private int headImageIndex;
	/**积分*/
	private int integral;
	/**当前一盘牌中玩家的得分*/
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

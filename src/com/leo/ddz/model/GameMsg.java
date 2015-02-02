package com.leo.ddz.model;
import java.util.*;

import com.leo.ddz.DdzConstants;
import com.leo.ddz.data.Config;
import com.leo.ddz.rule.PokeAnalyze;
/**
 * 记录游戏用一些状态信息
 * @author Leo
 *
 */
@SuppressWarnings("unchecked")
public class GameMsg
{
	/**用户名*/
	private String userName;
	/**玩家手上牌的信息*/
	public ArrayList<Poke> zhurenPoke = new ArrayList<Poke>();
	
	/**出牌或叫地主过程中轮到某个玩家*/
	int turnBanker = 0;
	/**轮到出牌的位置(0 ,1,2)是按座位来分的*/
	int order = -1;
	/**叫分的次数（最多叫一圈 为3次）*/
	int callScoreTimes = 0;
	/**记录地主玩家 (用位置标识)*/
	 int diZhuSite = -1;//0表示自己    1 2轮流其他玩家位置
//	/**是否开始*/
//	boolean isStarted = false;
	/**是否结束*/
	boolean isGameOver = false;
	/**当前所叫的分值*/
	int currCallScore = 0 ;
	/**底分*/
	int bottomScore = 0;
	/**底牌*/
	public ArrayList<Poke> bottomPoke = new ArrayList<Poke>(3);
	/**当前轮的牌*/
	public ArrayList<Poke> currentPoke = new ArrayList<Poke>();
	
	/**一副牌的信息*/
	private LinkedList<Poke> initPokes = new LinkedList<Poke>();
	
	/**记录每个玩家手上牌的数目*/
	public  int[] count = new int[3];
	
	/**记录主人玩家的牌是否弹出*/
	public  boolean[] pokePop = new boolean[20];
	
	/**记录每个玩家叫分的情况*/
	public  int callScore[] = new int[3];
	/**记录每局各玩家的牌*/
	public  ArrayList<Poke>[] playersPoke = new ArrayList[3];
	
	/**记录每轮牌轮过玩家数目*/
	public  int hadPut = 0;
	/**是否执行过倒跟反过程*/
	public  boolean hadDFG = false;
	/**保存当前用户的信息*/
	public static Player currUser;
	/**主玩家的牌按大到小排列*/
	public  boolean SORT_BY_BTOS = true;
	
	/**当前分析牌类 */
	private PokeAnalyze currPa;
	
	/**记录每轮各玩家出的牌*/
	public ArrayList<Poke>[] turnPoke = new ArrayList[3];
	
	public enum DGFCourse{DAO,GEN,FAN,OVER}
	/**记录倒反跟过程状态*/
	public DGFCourse dGFCourse = null;  //TODO重发牌 初始数据
	/**记录每个玩家的倒跟反行为*/
	public Map<Integer,DGFCourse> playerDGFMap = new HashMap<Integer,DGFCourse>();

	/** 炸弹数量 */
	public int boomCount = 0;
	/**地主出了几首牌*/		//为计算地主春天
	public int dizhuHadPut = 0;
	public boolean isSpring = false;
	/**所有玩家*/
	public Player[] allPlayer = new Player[3];
	/**游戏难度*/
	public int difficulty ;
	
	public boolean dizhuWin = false;
	
	public void initMsg(Config config)
	{
		//初始化牌
		for (int i = 0;  i <= 14  ; i ++)
		{
			for(int j = 0; j < 4 ; j ++)
			{
				if(i >= 13 && j > 0) break;						
				initPokes.add(new Poke(i,j));
			}
		}
	}
	
	/**
	 * 发牌
	 */
	public void sendOutPai(){
		LinkedList<Poke> allPokes = new LinkedList<Poke>(initPokes); 
		pokePop = new boolean[20];
		//初始化分值
		for (int i=0;i<3 ;i++ )
		{
			callScore[i] = -1;
		}
		//初始化玩家的牌
		for (int i = 0; i<3 ;i++ )
		{
			if(playersPoke[i] == null)
				playersPoke[i] = new ArrayList<Poke>();
			else
				playersPoke[i].clear();
		}
		
		//初始记录每轮玩家所出牌数组
		for (int i = 0; i<3 ;i++ )
		{
			if(turnPoke[i] == null)
				turnPoke[i] = new ArrayList<Poke>();
			else
				turnPoke[i].clear();
		}
		bottomPoke.clear();
		currCallScore = 0;
		callScoreTimes = 0;
		diZhuSite = -1;
		turnBanker = 0;
		dGFCourse = null;
		SORT_BY_BTOS = true;
		isGameOver = false;
		currPa  = new PokeAnalyze();
		hadPut = 0 ;
		order = -1;
		playerDGFMap.clear();
		boomCount = 0;
		isSpring = false;
		dizhuHadPut = 0;
		dizhuWin = false;
		//=============以上为发牌之前做一些初始化动作
		
		Collections.shuffle(allPokes);//洗牌
		//谁拿方块3(控制牌),谁就先叫牌,如果在底牌中,就以此类推
		Poke flagPoke = new Poke(0,0);
		//发三张底牌到各玩家
		for (int i = 0; i < 3; i ++ )
		{
			Poke poke = allPokes.removeLast();
			if(poke.equals(flagPoke)){
				flagPoke = new Poke(0,flagPoke.getPokeColor() + 1);
			}
			this.bottomPoke.add(poke);
		}
		//发牌
		for(int i = 0;i<allPokes.size();i++){
			if(i<DdzConstants.INIT_POKE_COUNT){
				playersPoke[DdzConstants.HOST_SITE].add(allPokes.get(i));
				//设置发到控制牌的玩家首先叫地主
				if(allPokes.get(i).equals(flagPoke)) {
					this.setTurnBanker(DdzConstants.HOST_SITE);
				}
			}else if(i>=DdzConstants.INIT_POKE_COUNT&&i<DdzConstants.INIT_POKE_COUNT*2){
				this.playersPoke[DdzConstants.HOST_NEXT_SITE].add(allPokes.get(i));
				if(allPokes.get(i).equals(flagPoke)) {
					this.setTurnBanker(DdzConstants.HOST_NEXT_SITE);
				}
				//TODO 通过分析玩家的牌好坏决定叫分值
				this.callScore[DdzConstants.HOST_NEXT_SITE] = ((int)(Math.random()*10))%4;;
			}else{
				this.playersPoke[DdzConstants.HOST_LAST_SITE].add(allPokes.get(i));
				if(allPokes.get(i).equals(flagPoke)){
					this.setTurnBanker(DdzConstants.HOST_LAST_SITE);
				}
				//TODO 通过分析玩家的牌好坏决定叫分值
				this.callScore[DdzConstants.HOST_LAST_SITE] = ((int)(Math.random()*10))%4;;
			}
		}	
		//对牌排序
		for (int j = 0; j < this.playersPoke.length; j++) {
			Collections.sort(this.playersPoke[j]);
		}
	}
	public ArrayList<Poke>[] getTurnPoke() {
		return turnPoke;
		
	}

	public void setTurnPoke(ArrayList<Poke>[] turnPoke) {
		this.turnPoke = turnPoke;
	}

	public int getCallScoreTimes() {
		return callScoreTimes;
	}
	public void setCallScoreTimes(int callScoreTimes) {
		this.callScoreTimes = callScoreTimes;
	}
	public int getBottomScore() 
	{
	return bottomScore;
	}
	public void setBottomScore(int bottomScore) 
	{
		this.bottomScore = bottomScore;
	}
	
	public int getCurrCallScore() {
		return currCallScore;
	}


	public void setCurrCallScore(int currCallScore) {
		this.currCallScore = currCallScore;
	}


	public int[] getCount() {
		return count;
	}


	public void setCount(int[] count) {
		this.count = count;
	}


	public boolean[] getPokePop() {
		return pokePop;
	}


	public void setPokePop(boolean[] pokePop) {
		this.pokePop = pokePop;
	}


	public int[] getCallScore() {
		return callScore;
	}


	public void setCallScore(int[] callScore) {
		this.callScore = callScore;
	}


	public ArrayList<Poke>[] getPlayersPoke() {
		return playersPoke;
	}


	public void setPlayersPoke(ArrayList<Poke>[] playersPoke) {
		this.playersPoke = playersPoke;
	}


	public int getHadPut() {
		return hadPut;
	}


	public void setHadPut(int hadPut) {
		this.hadPut = hadPut;
	}


	public static Player getCurrUser() {
		return currUser;
	}


	public static void setCurrUser(Player currUser) {
		GameMsg.currUser = currUser;
	}


	public PokeAnalyze getCurrPa() {
		return currPa;
	}


	public void setCurrPa(PokeAnalyze currPa) {
		this.currPa = currPa;
	}
	/**轮到出牌的位置(1 ,2,3)是按座位来分的*/
	public int getOrder() 
	{
		return order;
	}
	public void setOrder(int order) 
	{
		this.order = order;
	}
	public int getTurnBanker()
	{
		return turnBanker;
	}
	public void setTurnBanker(int turnBanker)
	{
		this.turnBanker = turnBanker;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public ArrayList<Poke> getZhurenPoke() {
		return zhurenPoke;
	}
	public void setZhurenPoke(ArrayList<Poke> zhurenPoke) {
		this.zhurenPoke = zhurenPoke;
	}
	public int getDiZhuSite() {
		return diZhuSite;
	}
	public void setDiZhuSite(int diZhuSite) {
		this.diZhuSite = diZhuSite;
	}
	public ArrayList<Poke> getBottomPoke() {
		return bottomPoke;
	}
	public void setBottomPoke(ArrayList<Poke> bottomPoke) {
		this.bottomPoke = bottomPoke;
	}
	public ArrayList<Poke> getCurrentPoke() {
		return currentPoke;
	}
	public void setCurrentPoke(ArrayList<Poke> currentPoke) {
		this.currentPoke = currentPoke;
	}

	public LinkedList<Poke> getInitPokes() {
		return initPokes;
	}

	public void setInitPokes(LinkedList<Poke> initPokes) {
		this.initPokes = initPokes;
	}
	public Map<Integer, DGFCourse> getPlayerDGFMap() {
		return playerDGFMap;
	}

	public void setPlayerDGFMap(Map<Integer, DGFCourse> playerDGFMap) {
		this.playerDGFMap = playerDGFMap;
	}
	
	public int getBoomCount() {
		return boomCount;
	}

	public void setBoomCount(int boomCount) {
		this.boomCount = boomCount;
	}

	public Player[] getAllPlayer() {
		return allPlayer;
	}

	public void setAllPlayer(Player[] allPlayer) {
		this.allPlayer = allPlayer;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	
}

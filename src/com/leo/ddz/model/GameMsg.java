package com.leo.ddz.model;
import java.util.*;

import com.leo.ddz.DdzConstants;
import com.leo.ddz.data.Config;
import com.leo.ddz.rule.PokeAnalyze;
/**
 * ��¼��Ϸ��һЩ״̬��Ϣ
 * @author Leo
 *
 */
@SuppressWarnings("unchecked")
public class GameMsg
{
	/**�û���*/
	private String userName;
	/**��������Ƶ���Ϣ*/
	public ArrayList<Poke> zhurenPoke = new ArrayList<Poke>();
	
	/**���ƻ�е����������ֵ�ĳ�����*/
	int turnBanker = 0;
	/**�ֵ����Ƶ�λ��(0 ,1,2)�ǰ���λ���ֵ�*/
	int order = -1;
	/**�зֵĴ���������һȦ Ϊ3�Σ�*/
	int callScoreTimes = 0;
	/**��¼������� (��λ�ñ�ʶ)*/
	 int diZhuSite = -1;//0��ʾ�Լ�    1 2�����������λ��
//	/**�Ƿ�ʼ*/
//	boolean isStarted = false;
	/**�Ƿ����*/
	boolean isGameOver = false;
	/**��ǰ���еķ�ֵ*/
	int currCallScore = 0 ;
	/**�׷�*/
	int bottomScore = 0;
	/**����*/
	public ArrayList<Poke> bottomPoke = new ArrayList<Poke>(3);
	/**��ǰ�ֵ���*/
	public ArrayList<Poke> currentPoke = new ArrayList<Poke>();
	
	/**һ���Ƶ���Ϣ*/
	private LinkedList<Poke> initPokes = new LinkedList<Poke>();
	
	/**��¼ÿ����������Ƶ���Ŀ*/
	public  int[] count = new int[3];
	
	/**��¼������ҵ����Ƿ񵯳�*/
	public  boolean[] pokePop = new boolean[20];
	
	/**��¼ÿ����ҽзֵ����*/
	public  int callScore[] = new int[3];
	/**��¼ÿ�ָ���ҵ���*/
	public  ArrayList<Poke>[] playersPoke = new ArrayList[3];
	
	/**��¼ÿ�����ֹ������Ŀ*/
	public  int hadPut = 0;
	/**�Ƿ�ִ�й�����������*/
	public  boolean hadDFG = false;
	/**���浱ǰ�û�����Ϣ*/
	public static Player currUser;
	/**����ҵ��ư���С����*/
	public  boolean SORT_BY_BTOS = true;
	
	/**��ǰ�������� */
	private PokeAnalyze currPa;
	
	/**��¼ÿ�ָ���ҳ�����*/
	public ArrayList<Poke>[] turnPoke = new ArrayList[3];
	
	public enum DGFCourse{DAO,GEN,FAN,OVER}
	/**��¼����������״̬*/
	public DGFCourse dGFCourse = null;  //TODO�ط��� ��ʼ����
	/**��¼ÿ����ҵĵ�������Ϊ*/
	public Map<Integer,DGFCourse> playerDGFMap = new HashMap<Integer,DGFCourse>();

	/** ը������ */
	public int boomCount = 0;
	/**�������˼�����*/		//Ϊ�����������
	public int dizhuHadPut = 0;
	public boolean isSpring = false;
	/**�������*/
	public Player[] allPlayer = new Player[3];
	/**��Ϸ�Ѷ�*/
	public int difficulty ;
	
	public boolean dizhuWin = false;
	
	public void initMsg(Config config)
	{
		//��ʼ����
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
	 * ����
	 */
	public void sendOutPai(){
		LinkedList<Poke> allPokes = new LinkedList<Poke>(initPokes); 
		pokePop = new boolean[20];
		//��ʼ����ֵ
		for (int i=0;i<3 ;i++ )
		{
			callScore[i] = -1;
		}
		//��ʼ����ҵ���
		for (int i = 0; i<3 ;i++ )
		{
			if(playersPoke[i] == null)
				playersPoke[i] = new ArrayList<Poke>();
			else
				playersPoke[i].clear();
		}
		
		//��ʼ��¼ÿ���������������
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
		//=============����Ϊ����֮ǰ��һЩ��ʼ������
		
		Collections.shuffle(allPokes);//ϴ��
		//˭�÷���3(������),˭���Ƚ���,����ڵ�����,���Դ�����
		Poke flagPoke = new Poke(0,0);
		//�����ŵ��Ƶ������
		for (int i = 0; i < 3; i ++ )
		{
			Poke poke = allPokes.removeLast();
			if(poke.equals(flagPoke)){
				flagPoke = new Poke(0,flagPoke.getPokeColor() + 1);
			}
			this.bottomPoke.add(poke);
		}
		//����
		for(int i = 0;i<allPokes.size();i++){
			if(i<DdzConstants.INIT_POKE_COUNT){
				playersPoke[DdzConstants.HOST_SITE].add(allPokes.get(i));
				//���÷��������Ƶ�������Ƚе���
				if(allPokes.get(i).equals(flagPoke)) {
					this.setTurnBanker(DdzConstants.HOST_SITE);
				}
			}else if(i>=DdzConstants.INIT_POKE_COUNT&&i<DdzConstants.INIT_POKE_COUNT*2){
				this.playersPoke[DdzConstants.HOST_NEXT_SITE].add(allPokes.get(i));
				if(allPokes.get(i).equals(flagPoke)) {
					this.setTurnBanker(DdzConstants.HOST_NEXT_SITE);
				}
				//TODO ͨ��������ҵ��ƺû������з�ֵ
				this.callScore[DdzConstants.HOST_NEXT_SITE] = ((int)(Math.random()*10))%4;;
			}else{
				this.playersPoke[DdzConstants.HOST_LAST_SITE].add(allPokes.get(i));
				if(allPokes.get(i).equals(flagPoke)){
					this.setTurnBanker(DdzConstants.HOST_LAST_SITE);
				}
				//TODO ͨ��������ҵ��ƺû������з�ֵ
				this.callScore[DdzConstants.HOST_LAST_SITE] = ((int)(Math.random()*10))%4;;
			}
		}	
		//��������
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
	/**�ֵ����Ƶ�λ��(1 ,2,3)�ǰ���λ���ֵ�*/
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

package com.leo.ddz.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Message;
import com.leo.ddz.DdzConstants;
import com.leo.ddz.ai.AI;
import com.leo.ddz.ai.AI.StructObject;
import com.leo.ddz.model.DdzView;
import com.leo.ddz.model.GameMsg;
import com.leo.ddz.model.MessageHandler;
import com.leo.ddz.model.Player;
import com.leo.ddz.model.Poke;
import com.leo.ddz.model.GameMsg.DGFCourse;
import com.leo.ddz.ui.SoundPoolManager;
import com.leo.ddz.ui.skin.Skin;

public class RuleDefined {

	GameMsg gameMsg;
	MessageHandler msgHandler;
	DdzView ddzView;
	SoundPoolManager soundManager;
	Skin skin;

	public RuleDefined(DdzView ddzView, MessageHandler msgHandler,
			GameMsg gameMsg) {
		this.gameMsg = gameMsg;
		this.msgHandler = msgHandler;
		this.ddzView = ddzView;
		soundManager = SoundPoolManager.getInstance(ddzView.getContext());
		skin = Skin.getInstance(ddzView.getContext());
	}

	/**
	 * ��ҽзֵĴ������
	 * 
	 * @param site
	 */
	@SuppressWarnings("unchecked")
	public void callScore(int site) {
		gameMsg.setCallScoreTimes(gameMsg.getCallScoreTimes() + 1);
		int diZhuSite = DdzConstants.DIZHU_IS_NULL;
		
		if(gameMsg.callScore[site]>gameMsg.getCurrCallScore()){//��׼���еķ�ֵҪ���ڵ�ǰ�еķ�ֵ
			gameMsg.setCurrCallScore(gameMsg.callScore[site]);
		}else{
			gameMsg.callScore[site] = 0;;
		}
		if (gameMsg.getCurrCallScore() == 3) {
			diZhuSite = site;
		} else if (gameMsg.getCallScoreTimes() == 3) {
			if (gameMsg.callScore[site] == 0) {
				diZhuSite = returnDiZhuSite();
				if (diZhuSite == DdzConstants.DIZHU_IS_NULL) {
					// ���·���
					gameMsg.sendOutPai();
					ddzView.hostHadCall = false;
					ddzView.hasSendedPoke = false;
					ddzView.sendPokeIndex = 0;
					msgHandler.handleMessage(Message.obtain(msgHandler,
							MessageHandler.MESSAGE_SENDPOKE));
				}
			} else {
				diZhuSite = site;
			}
		} else {
			gameMsg.setTurnBanker(getNextSite(gameMsg.getTurnBanker()));// �ֵ��¼ҽе���
		}
		ddzView.hasCallScore = diZhuSite == DdzConstants.DIZHU_IS_NULL ? false:true;
		gameMsg.setDiZhuSite(diZhuSite);
		// �������ȷ����ѵ��Ƹ���
		if (diZhuSite != DdzConstants.DIZHU_IS_NULL) {
			gameMsg.playersPoke[diZhuSite].addAll(gameMsg.bottomPoke);
			gameMsg.setTurnBanker(diZhuSite);
			Collections.sort(gameMsg.playersPoke[diZhuSite]);

			if (gameMsg.getCallScoreTimes() < 3) {
				new Thread() {
					public void run() {
						try {
							sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						nextDGF(gameMsg.getDiZhuSite(), false);
					}
				}.start();
			}
		}
		if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.CALLSCORE);
		
		Bitmap[] animImages ;
		if(gameMsg.callScore[site]==1){
			animImages = skin.getOnePointAnim();
		}else if(gameMsg.callScore[site]==2){
			animImages = skin.getTwoPointAnim();
		}else if(gameMsg.callScore[site]==3){
			animImages = skin.getThreePointAnim();
		}else {
			animImages = skin.getNoCallAnim();
		}
		ddzView.showSpeakAnimation(site, animImages[site]);
		msgHandler.sendMessageDelayed(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW, ddzView.hasCallScore),500);
	}

	/**
	 * ����һ�����ƴ���
	 */
	@SuppressWarnings("unchecked")
	public void nextChuPai() {
		if (!ddzView.hadPutPoke)
			ddzView.hadPutPoke = true;
		int lastOrder = gameMsg.getOrder();
		int currOrder = getNextSite(lastOrder);
		gameMsg.setOrder(currOrder);

		if (currOrder != 0) {
			// ��������Ҷ��ǲ��� ���µ�һ�������ȳ��Ƶ�Ϊ��ǰ�������
			if (gameMsg.hadPut > 2 && gameMsg.turnPoke[lastOrder].size() == 0
					&& gameMsg.turnPoke[getLastSite(lastOrder)].size() == 0) {
				gameMsg.setTurnBanker(currOrder);
				gameMsg.hadPut = 0;
			}
			/**
			 * ================================================
			 * �������ܳ���
			 * ================================================
			 */
			if (gameMsg.hadPut == 0) {// ������������5
				StructObject so = AI
						.zhuDongChuPai(gameMsg.playersPoke[currOrder]);
				Collections.sort(so.split);
				gameMsg.currentPoke = new ArrayList<Poke>(so.split);
				gameMsg.playersPoke[currOrder].removeAll(so.split);
				gameMsg.turnPoke[currOrder] = so.split;
			} else {// ���Խ���
				ddzView.currPa = new PokeAnalyze(gameMsg.currentPoke);
				ddzView.currPa.analyze();
				StructObject so = AI.jiePai(ddzView.currPa,
						gameMsg.playersPoke[currOrder]);

				if (so != null && so.split.size() > 0) {// TODO ����
					gameMsg.currentPoke = new ArrayList<Poke>(so.split);
					gameMsg.playersPoke[currOrder].removeAll(so.split);
					gameMsg.turnPoke[currOrder] = so.split;
				} else {
					gameMsg.turnPoke[currOrder] = new ArrayList<Poke>();// Ҫ����
					ddzView.showSpeakAnimation(currOrder, skin.getPassAnim()[currOrder]);
				}
			}

			// �����������
			if (ddzView.hasCallScore &&  gameMsg.dizhuHadPut < 2 && gameMsg.getDiZhuSite() == currOrder
					&& gameMsg.turnPoke[currOrder].size() > 0) {
				gameMsg.dizhuHadPut++;
			}
			boolean dizhuSpring = false;
			if (dizhuSpring) {
				dizhuSpring = false;
			} else if (gameMsg.playersPoke[gameMsg.getDiZhuSite()].size() < 20) {
				dizhuSpring = true;
			}
			gameMsg.hadPut++;
		}

		// ������Ƿ����
		if (ddzView.hadPutPoke
				&& gameMsg.playersPoke[gameMsg.getOrder()].size() == 0) {
			msgHandler.removeMessages(MessageHandler.FALL_NEXT_CHUPAI);
			msgHandler.sendMessageDelayed(Message.obtain(msgHandler,
					MessageHandler.GAMEOVER), 700);
		}
		// ��������
		if (gameMsg.turnPoke[currOrder].size() > 0){
			PokeAnalyze pa = new PokeAnalyze(gameMsg.turnPoke[currOrder]);
			if (pa.analyze() && pa.getType() == PokeAnalyze.BOOM && gameMsg.hadPut!=1) {
				if (pa.getBigPoke().getPokeNum() == DdzConstants.Poke_BG) {
					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.ROCKET);
				}else{
					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.BOOM);
				}
				gameMsg.boomCount++;
			} else {
				if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.CHUPAI);
			}
		} else {
			if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.PASS);
		}
		ddzView.postInvalidate();
	}

	public void nextDGF(int currDGFSite, boolean hostSelectFlag) {
		int times = gameMsg.getCallScoreTimes();
		if (times >= 3)
			return;
		int diZhuSite = gameMsg.getDiZhuSite();

		try {
			/** �龰һ:����ҵ���ֵ��Լ��ĵ�����ѡ��ť �� ��ѡ �ڳ����һ���� ִ��������� */
			if ((currDGFSite == DdzConstants.HOST_SITE && currDGFSite != diZhuSite)
					|| (currDGFSite == diZhuSite && gameMsg.dGFCourse != null)) {
				ddzView.hostChoosingDGF = false;
				msgHandler.handleMessage(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW));
				if (currDGFSite == getNextSite(diZhuSite)) { // ==2 �Ǵ���������жϵ�
					boolean daoFlag = hostSelectFlag;
					if (daoFlag) {
						toShowDGFView(currDGFSite, DGFCourse.DAO);
						boolean genFlag = isToDGF(getNextSite(currDGFSite),
								DGFCourse.GEN);
						if (genFlag) {
							toShowDGFView(getNextSite(currDGFSite),
									DGFCourse.GEN);
						}
					} else if (times == 1) {// times==1��һ�Ҳſɵ�
						daoFlag = isToDGF(getNextSite(currDGFSite),
								DGFCourse.DAO);
						if (daoFlag) {
							toShowDGFView(getNextSite(currDGFSite),
									DGFCourse.DAO);
						}
					}
				} else if (currDGFSite == getLastSite(diZhuSite)) {
					if (gameMsg.dGFCourse == null && times == 1) {// �����û�л���з�
						// ���ϼ�û�е�
						// ��ʱ�ɵ�
						boolean daoFlag = hostSelectFlag;
						if (daoFlag) {
							toShowDGFView(currDGFSite, DGFCourse.DAO);
						}
					} else if (gameMsg.dGFCourse == DGFCourse.DAO) {// �����ǰ���ڵ�
						// �Ϳ�ѡ���
						boolean genFlag;
						if (currDGFSite == DdzConstants.HOST_SITE) {
							genFlag = hostSelectFlag;
						} else {
							genFlag = isToDGF(currDGFSite, DGFCourse.GEN);
						}
						if (genFlag) {
							toShowDGFView(currDGFSite, DGFCourse.GEN);
						}
					}
				}

				if (gameMsg.dGFCourse == DGFCourse.DAO
						|| gameMsg.dGFCourse == DGFCourse.GEN) {
					boolean fanFlag;
					if (diZhuSite == DdzConstants.HOST_SITE) {
						fanFlag = hostSelectFlag;
					} else {
						fanFlag = isToDGF(diZhuSite, DGFCourse.FAN);
					}
					if (fanFlag) {
						toShowDGFView(diZhuSite, DGFCourse.FAN);
					}
				}
				gameMsg.dGFCourse = DGFCourse.OVER; // ���������� ����������
				msgHandler.handleMessage(Message.obtain(msgHandler,
						MessageHandler.REFRESH_VIEW));
				return;
			} else if (DdzConstants.HOST_SITE == diZhuSite
					&& gameMsg.dGFCourse == null) {
				/** �龰��:�����Ϊ����--����֮ǰ ������ҵ���һ�ҿ�ʼ��һֱ next ������ �� ѡ�� ����������� */
				// ���� ����Ӧ if(currDGFSite==diZhuSite &&����
				boolean daoFlag = isToDGF(getNextSite(diZhuSite), DGFCourse.DAO);// ������ҵ���һ�ҿ�ʼ��
				if (daoFlag) {
					toShowDGFView(getNextSite(diZhuSite), DGFCourse.DAO);
					// ��һ�Ҿ��Ǹ��� -- �е��Ϳɸ� û����������
					boolean genFlag = isToDGF(getLastSite(diZhuSite),
							DGFCourse.GEN);
					if (genFlag) {
						toShowDGFView(getLastSite(diZhuSite), DGFCourse.GEN);
					}
				} else if (times == 1) {
					// ��һ�� ��
					daoFlag = isToDGF(getLastSite(diZhuSite), DGFCourse.DAO);
					if (daoFlag) {
						toShowDGFView(getLastSite(diZhuSite), DGFCourse.DAO);
					}
				}

				// ������һ�Ҿ�������ҷ���
				if(gameMsg.dGFCourse!=null) ddzView.hostChoosingDGF = true;
				msgHandler.sendMessageDelayed(Message.obtain(msgHandler,
						MessageHandler.REFRESH_VIEW), 500);
				return;
			} else if (gameMsg.dGFCourse == null) {
				/** �龰��������Ϊ������� **/
				// �����߳� ʱִ�е�DGF����
				int nextSite = getNextSite(diZhuSite);// �жϵ�����һ���Ƿ�Ϊ�����
				if (nextSite == DdzConstants.HOST_SITE) {
					ddzView.hostChoosingDGF = true;
					msgHandler.sendMessageDelayed(Message.obtain(msgHandler,
							MessageHandler.REFRESH_VIEW), 500);
					return;
				} else {
					boolean daoFlag = isToDGF(nextSite, DGFCourse.DAO);
					if (daoFlag) {
						toShowDGFView(getNextSite(diZhuSite), DGFCourse.DAO);
					}
					nextSite = getNextSite(nextSite);
					if (nextSite == DdzConstants.HOST_SITE) {// ��ʱnextSiteһ��Ϊ�����
						ddzView.hostChoosingDGF = true;
						msgHandler.sendMessageDelayed(Message.obtain(
								msgHandler, MessageHandler.REFRESH_VIEW), 500);
						return;
					} else {
						throw new RuntimeException("DGF Logic is error!");
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�������λ��
	 * 
	 * @return
	 */
	public int returnDiZhuSite() {
		int checkTurnBanker = getLastSite(gameMsg.getTurnBanker());
		if (gameMsg.callScore[checkTurnBanker] != 0) {// �ж���һ�� �����ֵ��Ϊ�����ȷ��Ϊ����
			// ������λ��
			return checkTurnBanker;
		} else {
			checkTurnBanker = getLastSite(checkTurnBanker);// �ж���һ�� ����һ��
			if (gameMsg.callScore[checkTurnBanker] != 0) {
				return checkTurnBanker;
			}
		}
		return -1;
	}
	/**
	 * �ó�ָ������Ƿ�ѡ����Ӧ�ĵ�����
	 * 
	 * @param currDGFSite
	 * @param dGFStatus
	 * @return
	 */
	public boolean isToDGF(int currDGFSite, DGFCourse dGFStatus) {
		boolean dgfFlag = ((int)(Math.random() * 10)) % 2 == 0 ? false : true;
		if(dGFStatus !=DGFCourse.DAO){
			dgfFlag = true;
		}else{
			dgfFlag = false;
		}
		return dgfFlag;															//��ʱ����������
	}

	@SuppressWarnings("unchecked")
	public void toCalScoring() {
		int diZhuSite = gameMsg.getDiZhuSite();
		
		int multiple = getMultiple(gameMsg.allPlayer[DdzConstants.HOST_SITE].getIntegral());
		int scoring = gameMsg.getCurrCallScore()
				* (int) Math.pow(2, gameMsg.boomCount)
				* multiple;

		
		if (gameMsg.dizhuHadPut < 2) {
			gameMsg.isSpring = true;
		} else if (gameMsg.dizhuWin
				&& (gameMsg.playersPoke[getNextSite(diZhuSite)].size() == 17 && gameMsg.playersPoke[getLastSite(diZhuSite)]
						.size() == 17)) {
			gameMsg.isSpring = true;
		}
		if (gameMsg.isSpring) {
			scoring = scoring * 2;
		}
		int plusMinus = gameMsg.dizhuWin == true ? -1 : 1;
		Player dizhuNextSite = gameMsg.allPlayer[getNextSite(diZhuSite)];
		dizhuNextSite.setCurrGameScoring(scoring * plusMinus);
		dizhuNextSite.setIntegral(dizhuNextSite.getIntegral() + scoring
				* plusMinus);

		Player dizhuLastSite = gameMsg.allPlayer[getLastSite(diZhuSite)];
		dizhuLastSite.setCurrGameScoring(scoring * plusMinus);
		dizhuLastSite.setIntegral(dizhuLastSite.getIntegral() + scoring
				* plusMinus);
		if (gameMsg.playerDGFMap.size() > 0) {
			for (Iterator iter = gameMsg.playerDGFMap.entrySet().iterator(); iter
					.hasNext();) {
				Map.Entry element = (Map.Entry) iter.next();
				Integer site = (Integer) element.getKey();
				DGFCourse dgf = (DGFCourse) element.getValue();
				int currScoring = gameMsg.allPlayer[site].getCurrGameScoring();
				if (dgf != DGFCourse.FAN) {
					gameMsg.allPlayer[site].setCurrGameScoring(currScoring * 2);
				} else {
					gameMsg.allPlayer[site].setCurrGameScoring(currScoring * 4);
				}
			}
		}
		int diZhuScoring = -1* (dizhuNextSite.getCurrGameScoring() + dizhuLastSite.getCurrGameScoring());
		Player diZhu = gameMsg.allPlayer[diZhuSite];
		diZhu.setCurrGameScoring(diZhuScoring);
		diZhu.setIntegral(diZhu.getIntegral() + diZhuScoring);
		
		for(int i=0;i<=2;i++){
			if(gameMsg.allPlayer[i].getIntegral()<=0){
				gameMsg.allPlayer[i].setIntegral(200);
			}
		}
	}

	public void toShowDGFView(int currDGFSite, DGFCourse dao)
			throws InterruptedException {
		ddzView.toShowDGF(currDGFSite, dao);
	}

	/**
	 * ���ݻ��ֵó��ȼ�
	 * 
	 * @param integral
	 * @return
	 */
	public static int  getGrade(int integral) {
		if (integral > 100000){
			return 7;
		} if (integral > 30000){
			return 6;
		}  else if (integral > 10000) {
			return 5;
		} else if (integral > 5000) {
			return 4;
		}else if (integral > 4000) {
			return 3;
		}  else if (integral > 2000) {
			return 2;
		} else if (integral > 500) {
			return 1;
		}else {
			return 0;
		}
	}

	public static int getMultiple(int integral){
		if (integral > 100000){
			return 20;
		} if (integral > 30000){
			return 20;
		}  else if (integral > 10000) {
			return 10;
		} else if (integral > 5000) {
			return 10;
		}else if (integral > 4000) {
			return 5;
		}  else if (integral > 2000) {
			return 5;
		} else if (integral > 500) {
			return 2;
		}else {
			return 2;
		}
//		switch(grade){
//			case 0:
//				return 2;
//			case 1:
//				return 2;
//			case 2:
//				return 5;
//			case 3:
//				return 5;
//			case 4:
//				return 10;
//			case 5:
//				return 10;
//			case 6:
//				return 20;
//			case 7:
//				return 20;
//		}
//		return 2;
	}
	private int getLastSite(int site) {
		return site == 0 ? 2 : site - 1;
	}

	private int getNextSite(int site) {
		return site == 2 ? 0 : site + 1;
	}
}

package com.leo.ddz.model;

import static com.leo.ddz.DdzConstants.*;
import static com.leo.ddz.ui.Position.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.*;
import java.util.Map.Entry;
 
import com.leo.ddz.R;
import com.leo.ddz.DdzConstants;
import com.leo.ddz.GameActivity;
import com.leo.ddz.data.Config;
import com.leo.ddz.model.GameMsg.DGFCourse;
import com.leo.ddz.rule.PokeAnalyze;
import com.leo.ddz.rule.RuleDefined;
import com.leo.ddz.ui.MatrixAnimation;
import com.leo.ddz.ui.MusicPlayer;
import com.leo.ddz.ui.Position;
import com.leo.ddz.ui.SoundPoolManager;
import com.leo.ddz.ui.skin.Skin;
import com.leo.ddz.util.AlgorithmUtil;

/**
 * ��Ϸ��ͼ��
 * @author Leo
 *
 */
public class DdzView extends View{
	public static String STR_SHENG = "";
	public static String STR_ZHANG = "";
	Skin skin;
	/**��¼��Ϸ��״̬��Ϣ*/
	private GameMsg gameMsg = null;
	private SoundPoolManager soundManager = null;
	RuleDefined ruleDefined;
	Config config;
	public void setConfig(Config config) {
		this.config = config;
	}
	MusicPlayer bgMusic;
	private boolean isPlaySounds = true;
	/** ���Ƶ�����*/ 
	public int sendPokeIndex = 0;
	/**�����Ƿ�й���*/
	public boolean hostHadCall = false;
	/**�Ƿ����Ʊ�ʾ*/
	public Boolean hasSendedPoke = false;
	/**�Ƿ�������*/
	public Boolean hasCallScore = false;
	
	/**�Ƿ�ʼ���Ʊ�ʾ*/
	public boolean hadPutPoke = false;
	/**�����Ƿ����*/
	public boolean hostHadChuPaing = false;
	/**�����Ƿ�ѡ�񵹸���*/
	public boolean hostChoosingDGF = false;
	/**ˢ�³�������*/
//	public boolean refrechChuPaiArea = false;

	/** ��¼���������λ��*/
	private int xPos;
	private int yPos;

	/**��ǰ����*/
	public PokeAnalyze currPa = null;
	/**�����ѡ�����*/
	PokeAnalyze hostChoosePoke = null;
	/**�����ѡ������Ƿ�ɳ�*/
	boolean canSendPoke = false;
	public boolean showBottomPoke = false;
	/**�����߳̿���*/
	Thread computerThread ;
	MessageHandler msgHandler ;
	Bitmap pokeBack;
	Paint gradeFontPaint;
	Paint userNamePaint;
	public void setPokeBack(Bitmap pokeBack) {
		this.pokeBack = pokeBack;
	}
	Map<Integer,MatrixAnimation> speakAnimationMap = new HashMap<Integer, MatrixAnimation>();
	public DdzView(Context context,GameMsg gameMsg) {
		super(context);
		this.gameMsg = gameMsg;
		this.soundManager = SoundPoolManager.getInstance(context);
		skin = Skin.getInstance(context);
		init(context);
	}
	private void init(Context context){
		msgHandler = new MessageHandler(gameMsg,this);
		ruleDefined = new RuleDefined(this,msgHandler,gameMsg);
		
		gradeFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		gradeFontPaint.setColor(Color.parseColor("#FFD700"));
		gradeFontPaint.setTextSize(15);
		
		userNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		userNamePaint.setColor(Color.WHITE);
		userNamePaint.setTextSize(13);
		STR_SHENG = context.getString(R.string.sheng);
		STR_ZHANG = context.getString(R.string.zhang);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		xPos = (int)event.getX();
		yPos = (int)event.getY();
		setPos(xPos,yPos);
		if(action == MotionEvent.ACTION_UP){
			if  (xPos >= Position.MUSIC_BN.x
					&& xPos <= (DdzConstants.MUSIC_BN_W + Position.MUSIC_BN.x)
					&& yPos >= Position.MUSIC_BN.y
					&& yPos <= DdzConstants.MUSIC_BN_W + Position.MUSIC_BN.y){
					isPlaySounds = !isPlaySounds;
					if(isPlaySounds && !config.isPlayBgSound() && !config.isPlaySound()){
						bgMusic.loopPlay(R.raw.game_backgroud);
						config.setPlaySound(true);
						config.setPlayBgSound(true);
						config.save(getContext());
						isPlaySounds = true;
					}else if(!isPlaySounds){
						bgMusic.pause();
					}else{
						bgMusic.resume();
					}
					if(isPlaySounds) soundManager.play(SoundPoolManager.CLICK_MUSIC_BTN);
					invalidate(xPos-DdzConstants.MUSIC_BN_W,yPos-DdzConstants.MUSIC_BN_W,xPos+DdzConstants.MUSIC_BN_W,yPos+DdzConstants.MUSIC_BN_W);
					return true;
			}
			if (!gameMsg.isGameOver) {
				if (gameMsg.getDiZhuSite() == DdzConstants.DIZHU_IS_NULL) {
					if (gameMsg.getTurnBanker() == DdzConstants.HOST_SITE) {
						int zhuRenCallScore = -1;
						if (gameMsg.getCurrCallScore() < 1
								&& xPos >= Position.ONE_BN.x
								&& xPos <= (DdzConstants.BN_WIDTH + Position.ONE_BN.x)
								&& yPos >= Position.ONE_BN.y
								&& yPos <= (DdzConstants.BN_HIGHT + Position.ONE_BN.y)) {
							zhuRenCallScore = 1;
						} else if (gameMsg.getCurrCallScore() < 2
								&& xPos >= Position.TWO_BN.x
								&& xPos <= (DdzConstants.BN_WIDTH + Position.TWO_BN.x)
								&& yPos >= Position.TWO_BN.y
								&& yPos <= (DdzConstants.BN_HIGHT + Position.TWO_BN.y)) {
							zhuRenCallScore = 2;
						} else if (gameMsg.getCurrCallScore() < 3
								&& xPos >= Position.THREE_BN.x
								&& xPos <= (DdzConstants.BN_WIDTH + Position.THREE_BN.x)
								&& yPos >= Position.THREE_BN.y
								&& yPos <= (DdzConstants.BN_HIGHT + Position.THREE_BN.y)) {
							zhuRenCallScore = 3;
						} else if (xPos >= Position.BJ_BN.x
								&& xPos <= (DdzConstants.BN_WIDTH + Position.BJ_BN.x)
								&& yPos >= Position.BJ_BN.y
								&& yPos <= (DdzConstants.BN_HIGHT + Position.BJ_BN.y)) {
							zhuRenCallScore = 0;
						}

						if (zhuRenCallScore != -1){
							gameMsg.callScore[0] = zhuRenCallScore;
							hostHadCall = true;
							computerThread = new Thread(new ComputerThread());
							computerThread.start();
							msgHandler.handleMessage(Message.obtain(msgHandler,
									MessageHandler.REFRESH_VIEW));
							return true;
						}
					}
				} else {
					int pokeCount = gameMsg.playersPoke[0].size();
					int length = (int) DdzConstants.POKE_WIDTH
							+ DdzConstants.HOST_SHOW_WIDTH * (pokeCount - 1);
					int xStart = 216 + (17 - gameMsg.playersPoke[0].size()) * 2;
					boolean disableBcBtn = gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE]
							.size() == 0
							&& gameMsg.turnPoke[DdzConstants.HOST_NEXT_SITE]
									.size() == 0;
					// ����������Ƶķ�Χ��
					if (yPos >= DdzConstants.POKE_Y_POS
							- DdzConstants.POP_HEIGHT
							&& yPos <= DdzConstants.POKE_Y_POS
									+ (int) DdzConstants.POKE_HIGHT
							&& xPos >= xStart && xPos <= xStart + length) {
						// ��¼��굥��ʱ,�����xStart��λ��
						int x_len = xPos - xStart;
						int index = pokeCount - 1 - x_len
								/ DdzConstants.HOST_SHOW_WIDTH;
						// ���������һ��
						if (index < 0)
							index = 0;
						// �����ڵ���������
						if (gameMsg.pokePop[index]) {
							if (yPos >= DdzConstants.POKE_Y_POS
									- DdzConstants.POP_HEIGHT
									&& yPos <= DdzConstants.POKE_Y_POS
											+ (int) DdzConstants.POKE_HIGHT
											- DdzConstants.POP_HEIGHT) {
								gameMsg.pokePop[index] = false;
							}
						} else {
							if (yPos >= DdzConstants.POKE_Y_POS) {
								gameMsg.pokePop[index] = true;
							} else
							// �����ĵ���������Ҫ�鿴ǰ�����Ƿ��е������еĻ�����Ҫ����ȥ
							if (yPos >= DdzConstants.POKE_Y_POS
									- DdzConstants.POP_HEIGHT) {
								for (int i = 1; i < pokeCount - index; i++) {
									if (gameMsg.pokePop[index + i] == true) {
										gameMsg.pokePop[index + i] = false;
										break;
									}
								}
							}
						}
						if(isPlaySounds) soundManager.play(SoundPoolManager.CHOSE);
						hostChoosePoke = new PokeAnalyze(hostChuPai());

						currPa = new PokeAnalyze(gameMsg.currentPoke);
						currPa.analyze();// TODO ���ظ�����
						if (hostChoosePoke.analyze()
								&& (disableBcBtn || (gameMsg.hadPut == 0
										|| (hostChoosePoke.getType() == PokeAnalyze.BOOM && hostChoosePoke.getLength() == 2)
										|| (currPa.getType() != PokeAnalyze.BOOM && hostChoosePoke.getType() == PokeAnalyze.BOOM) || (hostChoosePoke
										.getBigPoke().getPokeNum() > currPa.getBigPoke().getPokeNum()
										&& hostChoosePoke.getType() == currPa.getType() && hostChoosePoke.getLength() == currPa.getLength())))) {
							canSendPoke = true;
						} else {
							canSendPoke = false;
						}
						if (gameMsg.getOrder() == 2)
							this.invalidate();
						return true;
					}
					if (gameMsg.getOrder() == 2
							&& xPos >= Position.CP_BN.x
							&& xPos <= (skin.getCpImage()[0].getWidth() + Position.CP_BN.x)
							&& yPos >= Position.ONE_BN.y
							&& yPos <= (skin.getCpImage()[0].getHeight() + Position.CP_BN.y)) {

						ArrayList<Poke> popList = hostChuPai();

						// ��������Ҷ��ǲ��� ���µ�һ�������ȳ��Ƶ�Ϊ��ǰ�������
						if (gameMsg.hadPut > 2
								&& gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE]
										.size() == 0
								&& gameMsg.turnPoke[DdzConstants.HOST_NEXT_SITE]
										.size() == 0) {
							gameMsg.setTurnBanker(DdzConstants.HOST_SITE);
							gameMsg.hadPut = 0;
						}
						if (canSendPoke) {
							gameMsg.currentPoke = new ArrayList<Poke>(popList);
							// ����ʼ����������
							gameMsg.pokePop = new boolean[20];
							// ɾ����Ӧ����
							gameMsg.playersPoke[0].removeAll(popList);
							gameMsg.turnPoke[DdzConstants.HOST_SITE] = popList;

							gameMsg.hadPut++;
							ruleDefined.nextChuPai();
							hostHadChuPaing = false;
							if (gameMsg.playersPoke[DdzConstants.HOST_SITE]
									.size() != 0) {
								computerThread = new Thread(
										new ComputerThread());
								computerThread.start();
							}
							canSendPoke = false;

							// ����������ҷ�����ѡ���״̬ʱ һֱû�н���ѡ�� Ĭ���ڳ���һ���ƺ�ѡ��false
							if (hostChoosingDGF && gameMsg.dizhuHadPut == 1
									&& gameMsg.getCallScoreTimes() < 3) {
								hostChoosingDGF = false;
								new Thread(){
									public void run() {
										ruleDefined.nextDGF(
												DdzConstants.HOST_SITE, false);
									}
								}.start();
							}
							// �����������
							if (gameMsg.dizhuHadPut < 2
									&& DdzConstants.HOST_SITE == gameMsg.getDiZhuSite()) {
								gameMsg.dizhuHadPut++;
							}
							return true;
						}
					} else if (!disableBcBtn
							&& gameMsg.getOrder() == 2
							&& xPos >= Position.BC_BN.x
							&& xPos <= (skin.getBcImage()[0].getWidth() + Position.BC_BN.x)
							&& yPos >= Position.BC_BN.y
							&& yPos <= (skin.getBcImage()[0].getHeight() + Position.BC_BN.y)) {
						// ����ʼ����������
						gameMsg.pokePop = new boolean[20];
						gameMsg.turnPoke[DdzConstants.HOST_SITE] = new ArrayList<Poke>();
						ruleDefined.nextChuPai();
						hostHadChuPaing = false;
						gameMsg.hadPut++;
						computerThread = new Thread(new ComputerThread());
						computerThread.start();
						// ��ʾ��������
						showSpeakAnimation(HOST_SITE, skin
								.getPassAnim()[HOST_SITE]);

						// ����������ҷ�����ѡ���״̬ʱ һֱû�н���ѡ�� Ĭ���ڳ���һ���ƺ�ѡ��false
						if (hostChoosingDGF && gameMsg.dizhuHadPut == 1
								&& gameMsg.getCallScoreTimes() < 3) {
							hostChoosingDGF = false;
							new Thread() {
								public void run() {
									ruleDefined.nextDGF(DdzConstants.HOST_SITE,
											false);
								}
							}.start();
						}
						return true;
					} else if (gameMsg.getCallScoreTimes() < 3    // ��Ӧ�������¼�
							&& gameMsg.dGFCourse != DGFCourse.OVER
							&& xPos >= Position.DGF_BN.x
							&& xPos <= (DdzConstants.DGF_BN_W + Position.DGF_BN.x)
							&& yPos >= Position.DGF_BN.y
							&& yPos <= (DdzConstants.DGF_BN_W + +Position.DGF_BN.y)) {

						new Thread() {
							public void run() {
								ruleDefined.nextDGF(DdzConstants.HOST_SITE,
										true);
							}
						}.start();
						return true;
					}
				}
				if (xPos >= Position.SORT_BN.x
						&& xPos <= (DdzConstants.SORT_BN_W + Position.SORT_BN.x)
						&& yPos >= Position.SORT_BN.y
						&& yPos <= (DdzConstants.SORT_BN_H + +Position.SORT_BN.y)
						&& gameMsg.playersPoke[0].size() > 0) {
					if (gameMsg.SORT_BY_BTOS) {
						gameMsg.playersPoke[0] = AlgorithmUtil
								.sortCardByType(gameMsg.playersPoke[0]);
						gameMsg.SORT_BY_BTOS = false;
					} else {
						Collections.sort(gameMsg.playersPoke[0]);
						gameMsg.SORT_BY_BTOS = true;
					}

					if(isPlaySounds) soundManager.play(SoundPoolManager.SORT);
					invalidate();
					xPos = 0;
					yPos = 0;
					return true;
				}
				// ���пհ����� ����ȡ��ѡ�е���
				gameMsg.pokePop = new boolean[20];
				for (int i = 0; i < gameMsg.pokePop.length; i++) {
					if (!gameMsg.pokePop[i]) {
						canSendPoke = false;
						invalidate();
						return true;
					}
				}
			} else if (gameMsg.isGameOver) {
				if (xPos >= Position.CONTINUE_BN.x
						&& xPos <= (DdzConstants.BN_WIDTH + Position.CONTINUE_BN.x)
						&& yPos >= Position.CONTINUE_BN.y
						&& yPos <= (DdzConstants.BN_HIGHT + Position.CONTINUE_BN.y)) {
					// ����
					startGame();
					msgHandler.handleMessage(Message.obtain(msgHandler,
							MessageHandler.MESSAGE_SENDPOKE));
					if(isPlaySounds) soundManager.play(SoundPoolManager.ANJIAN_2);
				} else if (xPos >= Position.EXIT_BN.x
						&& xPos <= (DdzConstants.BN_WIDTH + Position.EXIT_BN.x)
						&& yPos >= Position.EXIT_BN.y
						&& yPos <= (DdzConstants.BN_HIGHT + Position.EXIT_BN.y)) {
					GameActivity gameActivity = (GameActivity) this
							.getContext();
					gameActivity.back();
					if(isPlaySounds) soundManager.play(SoundPoolManager.ANJIAN);
				}
			}
		}
		invalidate(xPos - 80, yPos - 80, xPos + 80, yPos + 80);
		return true;
	}
	
	/**
	 * ����Ļ��Ʒ���
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		
		try {
//			if(refrechChuPaiArea && !gameMsg.isGameOver){//ˢ�³�������
//				refrechChuPaiArea = false ;
//				return;
//			}
			canvas.drawBitmap(skin.getCalScoreBar(),275, 7, null);
			canvas.drawBitmap(skin.getMusicBtnImage()[isPlaySounds?1:0],Position.MUSIC_BN.x,Position.MUSIC_BN.y, null);
			for(int i=0;i<=2;i++){
				Point p = Position.getPos(i, Position.HEAD_SITE);
				if(gameMsg.getDiZhuSite()==i){
					canvas.drawBitmap(skin.getheadImage()[1],p.x,p.y, null);
				}else{
					canvas.drawBitmap(skin.getheadImage()[0],p.x,p.y, null);
				}
			}
			//�������Ϣ
			drawUserInfoView(canvas);
			/**��Ϸ��ʼ��*/	
			drawPokesView(canvas);
			if(hasSendedPoke){  
				/**������֮��  ��û��ȷ������--- ���зֻ���*/
				if(gameMsg.currCallScore<3 && gameMsg.getCallScoreTimes()<3 && gameMsg.getDiZhuSite()==DdzConstants.DIZHU_IS_NULL){
					drawCallScoreView(canvas);
				}else if(gameMsg.getDiZhuSite()!=DdzConstants.DIZHU_IS_NULL){//������ȷ��
					if(gameMsg.getCurrCallScore()!=0)canvas.drawBitmap(skin.getNumbers()[gameMsg.getCurrCallScore()-1],345, 10, null);
					if(gameMsg.boomCount!=0) canvas.drawBitmap(skin.getNumbers()[gameMsg.boomCount-1],450, 10, null);
					//���ƻ������
					if(!gameMsg.isGameOver){
						drawChuPaiView(canvas);
						drawDGFView(canvas);
					}
				}
				if (xPos >= Position.SORT_BN.x && xPos <= Position.SORT_BN.x + DdzConstants.SORT_BN_W
						&& yPos >= (int) Position.SORT_BN.y && yPos <= (int) Position.SORT_BN.y+ DdzConstants.SORT_BN_H) {
					canvas.drawBitmap(skin.getSortImage()[1],Position.SORT_BN.x, Position.SORT_BN.y, null);
				}else{
					canvas.drawBitmap(skin.getSortImage()[0],Position.SORT_BN.x, Position.SORT_BN.y, null);
				}
			}
			if(gameMsg.isGameOver && skin.getGameOverBitMap()!=null){
				canvas.drawBitmap(skin.getGameOverBitMap(),200, 80, null);
				if (xPos >= Position.CONTINUE_BN.x && xPos <= Position.CONTINUE_BN.x + DdzConstants.BN_WIDTH
						&& yPos >= (int) Position.CONTINUE_BN.y && yPos <= (int) Position.CONTINUE_BN.y+ DdzConstants.BN_HIGHT) {
					canvas.drawBitmap(skin.getContinueBtnImage()[1], Position.CONTINUE_BN.x, Position.CONTINUE_BN.y, null);
				}else{
					canvas.drawBitmap(skin.getContinueBtnImage()[0], Position.CONTINUE_BN.x, Position.CONTINUE_BN.y, null);
				}
				if (xPos >= Position.EXIT_BN.x && xPos <= Position.EXIT_BN.x + DdzConstants.BN_WIDTH
						&& yPos >= (int) Position.EXIT_BN.y && yPos <= (int) Position.EXIT_BN.y+ DdzConstants.BN_HIGHT) {
					canvas.drawBitmap(skin.getExitBtnImage()[1], Position.EXIT_BN.x, Position.EXIT_BN.y, null);
				}else{
					canvas.drawBitmap(skin.getExitBtnImage()[0], Position.EXIT_BN.x, Position.EXIT_BN.y, null);
				}
			}
			
			drawAnimation(canvas);
			xPos = 0;
			yPos = 0;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * ����Ч������
	 * @param canvas
	 */
	private void drawAnimation(Canvas canvas){
		if (speakAnimationMap.size() > 0) {
			Iterator<Entry<Integer, MatrixAnimation>> it = speakAnimationMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, MatrixAnimation> entry = it.next();
				MatrixAnimation sa = entry.getValue();
				if (!sa.completed()) {
					int site = (Integer)entry.getKey();
					Bitmap bm = sa.getCurrentView();
					Point p = getPos(site,SPEAKANIMARION_SITE);
					if(site == HOST_SITE){
						p.y = p.y - bm.getHeight();
					}else if(site == HOST_NEXT_SITE){
						p.x = p.x - bm.getWidth();
					}
					canvas.drawBitmap(bm,p.x,p.y, null);
				}
			}   
		}
	}
	/**
	 * �û���Ϣ
	 * @param canvas
	 */
	@SuppressWarnings("unchecked")
	private void drawUserInfoView(Canvas canvas) {
		for(int i=0;i<=2;i++){
			Point p = Position.getPos(i, Position.HEAD_SITE);
			int grade = RuleDefined.getGrade(gameMsg.allPlayer[i].getIntegral());
			canvas.drawBitmap(skin.getGradeNameImage()[grade],p.x,p.y+55,gradeFontPaint);
			canvas.drawText(gameMsg.allPlayer[i].getUserName(),p.x,p.y+100,userNamePaint);
			canvas.drawText(gameMsg.allPlayer[i].getIntegral()+"",p.x,p.y+115,gradeFontPaint);
			
		}
		Iterator dgf = gameMsg.playerDGFMap.entrySet().iterator();     
		while(dgf.hasNext()){     
			Map.Entry entry = (Map.Entry)dgf.next();   
			Integer site = (Integer)entry.getKey();   
			DGFCourse dgfStatus = (DGFCourse)entry.getValue();
			Point p = Position.getPos(site, Position.DGFSTATUS_SITE);
			Bitmap dgfImage = null;
			if(dgfStatus==DGFCourse.DAO){
				dgfImage = skin.getDaoStatus();
			}else if(dgfStatus==DGFCourse.GEN){
				dgfImage = skin.getGenStatus();
			}else if(dgfStatus==DGFCourse.FAN){
				dgfImage = skin.getFanStatus();
			}
			if(dgfImage!=null) canvas.drawBitmap(dgfImage,p.x,p.y,null);
		}
	}
	
	/**
	 * �����������������
	 * @param canvas
	 */
	public void drawDGFView(Canvas canvas){
		if(gameMsg.getCallScoreTimes()==3 || gameMsg.dGFCourse==DGFCourse.OVER) return ;
		Bitmap[] fanImage = skin.getFanImage();
		Bitmap[] daoImage = skin.getDaoImage();
		Bitmap[] genImage = skin.getGenImage();
		if(hostChoosingDGF && gameMsg.dizhuHadPut<2){
			if(gameMsg.dGFCourse==null ){
				if (xPos >= Position.DGF_BN.x && xPos <= Position.DGF_BN.x + DdzConstants.DGF_BN_W
						&& yPos >= (int) Position.DGF_BN.y && yPos <= (int) Position.DGF_BN.y+ DdzConstants.DGF_BN_W) {
					canvas.drawBitmap(daoImage[1],Position.DGF_BN.x, Position.DGF_BN.y, null);
				}else{
					canvas.drawBitmap(daoImage[0],Position.DGF_BN.x, Position.DGF_BN.y, null);
				}
			}else if(gameMsg.dGFCourse==DGFCourse.DAO && gameMsg.getDiZhuSite()!=DdzConstants.HOST_SITE){
				if (xPos >= Position.DGF_BN.x && xPos <= Position.DGF_BN.x + DdzConstants.DGF_BN_W
						&& yPos >= (int) Position.DGF_BN.y && yPos <= (int) Position.DGF_BN.y+ DdzConstants.DGF_BN_W) {
					canvas.drawBitmap(genImage[1], Position.DGF_BN.x, Position.DGF_BN.y, null);
				}else{
					canvas.drawBitmap(genImage[0], Position.DGF_BN.x, Position.DGF_BN.y, null);
				}
			}else if(gameMsg.dGFCourse==DGFCourse.GEN || (gameMsg.dGFCourse==DGFCourse.DAO && gameMsg.getDiZhuSite()==DdzConstants.HOST_SITE)){
				if (xPos >= Position.DGF_BN.x && xPos <= Position.DGF_BN.x + DdzConstants.DGF_BN_W
						&& yPos >= (int) Position.DGF_BN.y && yPos <= (int) Position.DGF_BN.y+ DdzConstants.DGF_BN_W) {
					canvas.drawBitmap(fanImage[1], Position.DGF_BN.x, Position.DGF_BN.y, null);
				}else{
					canvas.drawBitmap(fanImage[0], Position.DGF_BN.x, Position.DGF_BN.y, null);
				}
			}
		}
	}
	/**
	 * ���ƻ������
	 * @param canvas
	 */
	private void drawChuPaiView(final Canvas canvas){
		int hadPut = gameMsg.hadPut;
		int order = gameMsg.getOrder();
		boolean hostCurrViewHasShow = false;//502�� ������Ϊ��ʱorder ��û�ı��0��λ�� ����496��order==DdzConstants.HOST_LAST_SITE�Ŀ���
		Bitmap[] cpImage = skin.getCpImage();
		Bitmap[] bcImage = skin.getBcImage();
		Bitmap[][] pokeImage = skin.getPokeImage();
		if(order==DdzConstants.HOST_LAST_SITE && hostHadChuPaing){
			//������ ��������ť
			if(canSendPoke){
				if (xPos >= Position.CP_BN.x && xPos <= Position.CP_BN.x + DdzConstants.BN_WIDTH
						&& yPos >= (int) Position.CP_BN.y && yPos <= (int) Position.CP_BN.y+ DdzConstants.BN_HIGHT) {
					canvas.drawBitmap(cpImage[1], Position.CP_BN.x,Position.CP_BN.y, null);
				}else{
					canvas.drawBitmap(cpImage[0], Position.CP_BN.x, Position.CP_BN.y, null);
				}
			}else{
				canvas.drawBitmap(cpImage[2], Position.CP_BN.x, Position.CP_BN.y, null);
			}
			if(!(gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE].size()==0 && gameMsg.turnPoke[DdzConstants.HOST_NEXT_SITE].size()==0)){
				
				if (xPos >= Position.BC_BN.x && xPos <= Position.BC_BN.x + DdzConstants.BN_WIDTH
						&& yPos >= (int) Position.BC_BN.y && yPos <= (int) Position.BC_BN.y+ DdzConstants.BN_HIGHT) {
					canvas.drawBitmap(bcImage[1], Position.BC_BN.x,Position.BC_BN.y, null);
				}else{
					canvas.drawBitmap(bcImage[0], Position.BC_BN.x, Position.BC_BN.y, null);//���ȳ��ƵĲ�Ϊ�����²ſ���ѡ�񲻳�
				}
			}
			if(hadPut>0){
				Point point = Position.getPos(DdzConstants.HOST_LAST_SITE,Position.CHUPAI_SITE);
				if(gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE].size()!=0){
					for(int i=0;i<gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE].size();i++){
						Poke pp = gameMsg.turnPoke[DdzConstants.HOST_LAST_SITE].get(i);
						canvas.drawBitmap(pokeImage [pp.getPokeNum()][pp.getPokeColor()],  point.x + i * DdzConstants.SHOW_WIDTH, point.y, null);
					}
				}
				hostCurrViewHasShow = true;
			}
		}else if(hadPutPoke){
			Point point = Position.getPos(order,Position.CHUPAI_SITE);
			if(gameMsg.turnPoke[order].size()!=0){
				for(int i=0;i<gameMsg.turnPoke[order].size();i++){
					Poke po = gameMsg.turnPoke[order].get(i);
					canvas.drawBitmap(pokeImage[po.getPokeNum()][po.getPokeColor()],  point.x + i * DdzConstants.SHOW_WIDTH, point.y, null);
				}
			}
		}
		//�������ҳ���״̬
		if(hadPut>1){
			int lastOrder = getLastSite(order);
			Point point = Position.getPos(lastOrder,Position.CHUPAI_SITE);
			if(gameMsg.turnPoke[lastOrder].size()!=0 ){
				for(int i=0;i<gameMsg.turnPoke[lastOrder].size();i++){
					Poke poke = gameMsg.turnPoke[lastOrder].get(i);
					canvas.drawBitmap(pokeImage[poke.getPokeNum()][poke.getPokeColor()],  point.x + i * DdzConstants.SHOW_WIDTH, point.y, null);
				}
			}
			
			if(hadPut>2 && !hostCurrViewHasShow){
				int last2Order = getLastSite(lastOrder);
				point = Position.getPos(last2Order,Position.CHUPAI_SITE);
				if(gameMsg.turnPoke[last2Order].size()!=0){
					for(int i=0;i<gameMsg.turnPoke[last2Order].size();i++){
						Poke poke = gameMsg.turnPoke[last2Order].get(i);
						canvas.drawBitmap(pokeImage[poke.getPokeNum()][poke.getPokeColor()],  point.x + i * DdzConstants.SHOW_WIDTH, point.y, null);
					}
				}
			}
		}
	}
	/**
	 * ��ͼ���ƵĻ���
	 * @param canvas
	 */
	private void drawPokesView(Canvas canvas){
		int hostPokeLength = 0;
		int hostLastPokeLength = 0;
		int hostNextPokeLength = 0;
		Bitmap backPokeImage = pokeBack;
		Bitmap[][] pokeImage = skin.getPokeImage();
		if(hasSendedPoke){
			hostPokeLength = gameMsg.playersPoke[DdzConstants.HOST_SITE].size();
			hostLastPokeLength = gameMsg.playersPoke[DdzConstants.HOST_LAST_SITE].size();
			hostNextPokeLength = gameMsg.playersPoke[DdzConstants.HOST_NEXT_SITE].size();
		}else{
			hostPokeLength = sendPokeIndex;
			hostLastPokeLength = sendPokeIndex;
			hostNextPokeLength = sendPokeIndex;
		}
		
		//�����������������					
		for(int i=0;i<hostPokeLength;i++){
			Poke poke = gameMsg.playersPoke[0].get(hostPokeLength-1-i);
			// �ж����Ƿ���Ҫ��������
			if (gameMsg.pokePop[hostPokeLength-1-i]) {
				canvas.drawBitmap(pokeImage[poke.getPokeNum()][poke.getPokeColor()],
						(250 + i * DdzConstants.HOST_SHOW_WIDTH)-hostPokeLength*2,
								  DdzConstants.POKE_Y_POS - DdzConstants.POP_HEIGHT, null);
			}else{
				canvas.drawBitmap(pokeImage[poke.getPokeNum()][poke.getPokeColor()], (250 + i * DdzConstants.HOST_SHOW_WIDTH)-hostPokeLength*2, DdzConstants.POKE_Y_POS, null);
				Log.e("wuruize", "[" + poke.getPokeNum() + "][" + poke.getPokeColor() + "]," + String.valueOf((250 + i * DdzConstants.HOST_SHOW_WIDTH)-hostPokeLength*2) +"PX,"
				+ String.valueOf(DdzConstants.POKE_Y_POS) + "PX");
			}
		}		
		
		//��������ҵ���
		if(!gameMsg.isGameOver){
			for(int i=0;i<hostLastPokeLength;i++){
				canvas.drawBitmap(backPokeImage,15, 140+i * 4, null);
				if(i==hostLastPokeLength-1 && hasSendedPoke){
					float y = 150+i * 4+100;
					canvas.drawText(STR_SHENG+hostLastPokeLength+STR_ZHANG,20,y,skin.getPaiCountPaint());
				}
			}
			for(int i=0;i<hostNextPokeLength;i++){
				canvas.drawBitmap(backPokeImage, getWidth()-backPokeImage.getWidth()-15,140+i * 4, null);
				if(i==hostNextPokeLength-1 && hasSendedPoke){
					float y = 150+i * 4+100;
					canvas.drawText(STR_SHENG+hostNextPokeLength+STR_ZHANG,getWidth()-70,y,skin.getPaiCountPaint());
				}
			}
		}else{//������û�������
			for(int i=0;i<gameMsg.playersPoke[DdzConstants.HOST_LAST_SITE].size();i++){
				Poke p = gameMsg.playersPoke[DdzConstants.HOST_LAST_SITE].get(i);
				canvas.drawBitmap(pokeImage[p.getPokeNum()][p.getPokeColor()], 20+i*DdzConstants.SHOW_WIDTH, 130, null);
			}
			int size = gameMsg.playersPoke[DdzConstants.HOST_NEXT_SITE].size();
			for(int i=0;i<size;i++){
				Poke p = gameMsg.playersPoke[DdzConstants.HOST_NEXT_SITE].get(i);
				canvas.drawBitmap(pokeImage[p.getPokeNum()][p.getPokeColor()],
						(getWidth()-20-((size-1)*DdzConstants.SHOW_WIDTH+DdzConstants.POKE_WIDTH))+i*DdzConstants.SHOW_WIDTH, 130, null);
			}
		}
		//������  �Ѿ���ʼ������ �򲻻�����
		if(!hadPutPoke && hasSendedPoke){
			for (int i = 0; i < 3; i++) {
				//�����֮�����ʾ��������
				if(!showBottomPoke){
					canvas.drawBitmap(backPokeImage, 360 + i * DdzConstants.HOST_SHOW_WIDTH, 50, null);
				}else{
					Poke p = gameMsg.bottomPoke.get(i);
					canvas.drawBitmap(pokeImage[p.getPokeNum()][p.getPokeColor()],360 + i * DdzConstants.HOST_SHOW_WIDTH, 50, null);  
				}
			}
		}
	}
	/**
	 * ����зֽ׶εĻ������
	 * @param canvas
	 * @throws Exception
	 */
	private void drawCallScoreView(Canvas canvas) throws Exception{
		if(gameMsg.turnBanker==0 && !hostHadCall){
			// ��ǰ��ֵ���ڴ˷�ֵ ������ʾ���ͼƬ		
			int one = 0,two = 0,three = 0;
			if(gameMsg.currCallScore>=1){
				one = 2;
			}else if (xPos >= Position.ONE_BN.x && xPos <= Position.ONE_BN.x + DdzConstants.BN_WIDTH
					&& yPos >= (int) Position.ONE_BN.y && yPos <= (int) Position.ONE_BN.y+ DdzConstants.BN_HIGHT) {
				one = 1;
			}
			
			if(gameMsg.currCallScore>=2){
				two = 2;
			}else if (xPos >= Position.TWO_BN.x && xPos <= Position.TWO_BN.x + DdzConstants.BN_WIDTH
					&& yPos >= (int) Position.TWO_BN.y && yPos <= (int) Position.TWO_BN.y+ DdzConstants.BN_HIGHT) {
				two = 1;
			}
			
			if (xPos >= Position.THREE_BN.x && xPos <= Position.THREE_BN.x + DdzConstants.BN_WIDTH
					&& yPos >= (int) Position.THREE_BN.y && yPos <= (int) Position.THREE_BN.y+ DdzConstants.BN_HIGHT) {
				three = 1;
			}
			
			canvas.drawBitmap(skin.getOneImage()[one], Position.ONE_BN.x, Position.ONE_BN.y, null);
			canvas.drawBitmap(skin.getTwoImage()[two], Position.TWO_BN.x, Position.TWO_BN.y, null);
			canvas.drawBitmap(skin.getThreeImage()[three],Position.THREE_BN.x,Position.THREE_BN.y, null);
			if (xPos >= Position.BJ_BN.x && xPos <= Position.BJ_BN.x + DdzConstants.BN_WIDTH
					&& yPos >= (int) Position.BJ_BN.y && yPos <= (int) Position.BJ_BN.y+ DdzConstants.BN_HIGHT) {
				canvas.drawBitmap(skin.getBjImage()[1],Position.BJ_BN.x,Position.BJ_BN.y, null);
			}else{
				canvas.drawBitmap(skin.getBjImage()[0],Position.BJ_BN.x,Position.BJ_BN.y, null);
			}
		}
	}
	/**
	 * �����߳̿�����
	 * @author Leo
	 *
	 */
	class ComputerThread implements Runnable{
		public void run() {
			try {
				if(gameMsg.getDiZhuSite()==DdzConstants.DIZHU_IS_NULL){
					while(hasSendedPoke  && (gameMsg.turnBanker!=0 || hostHadCall)){
						if(gameMsg.currCallScore<3 && gameMsg.getCallScoreTimes()<3){
							int site = gameMsg.getTurnBanker();
							Thread.sleep(DdzConstants.JIAOFEN_DELAY);
							ruleDefined.callScore(site);
						}else{
							break;
						}
					} 
				}
				if(gameMsg.getDiZhuSite()!=DIZHU_IS_NULL){
					while(!gameMsg.isGameOver){
						int order = gameMsg.getOrder();
						if(gameMsg.getOrder()==-1){//�״γ���
							gameMsg.setOrder(getLastSite(gameMsg.getDiZhuSite()));
							gameMsg.setTurnBanker(gameMsg.getDiZhuSite());
							//��ʼ����
							if(gameMsg.getDiZhuSite()!=HOST_SITE){
								Thread.sleep(2500);
								msgHandler.handleMessage((Message.obtain(msgHandler,MessageHandler.FALL_NEXT_CHUPAI)));
							}else{
								hostHadChuPaing = true;
								msgHandler.handleMessage(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW));
								break;
							}
						}else if(order==HOST_LAST_SITE && !hostHadChuPaing){
							hostHadChuPaing = true;
							msgHandler.handleMessage(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW));
							break;
						}else{
							//��һ�����һ������
							if(!gameMsg.isGameOver){
//								if(!refrechChuPaiArea) msgHandler.sendMessageDelayed(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW,order),500);
//								Thread.sleep(DdzConstants.CHUPAI_DELAY);
//								msgHandler.handleMessage(Message.obtain(msgHandler,MessageHandler.FALL_NEXT_CHUPAI));
//								
								msgHandler.sendMessageDelayed(Message.obtain(msgHandler,MessageHandler.REFRESH_VIEW,order),1000);
								Thread.sleep(1200);
								msgHandler.handleMessage(Message.obtain(msgHandler,MessageHandler.FALL_NEXT_CHUPAI));
							}
						}
					}
				}
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ˵������
	 * @param currOrder
	 * @param images
	 */
	public void showSpeakAnimation(final int currOrder,Bitmap bitmap){
		final MatrixAnimation speakAnim = new MatrixAnimation(bitmap);
		speakAnimationMap.put(currOrder, speakAnim);
		
		class SpeakAnimationThread implements Runnable{
			public void run() {
				while(!speakAnim.completed()){
            		if(speakAnim==null) return;
                    try {
                    	if(speakAnim.getExtremeValue() != speakAnim.getProgress()){
                    		Thread.sleep(MatrixAnimation.INTERVALE);
                    	}else{
                    		Thread.sleep(900);
                    	}
                    	
                    }catch (InterruptedException e){
                    	Log.e("SpeakAnimation", e.getMessage());
                    }
                    speakAnim.next();
                    Point p = getPos(currOrder,SPEAKANIMARION_SITE);
                    int imageWidth = skin.getPassAnim()[0].getWidth();
                    int imageHeight = skin.getPassAnim()[0].getHeight();
//                    if(currOrder == HOST_SITE)
////                    	postInvalidate(p.x,p.y,p.x + imageWidth,p.y - imageHeight);
//                    else if(currOrder == HOST_NEXT_SITE)
////                    	postInvalidate(p.x,p.y,p.x - imageWidth,p.y + imageHeight);
//                    else if(currOrder == HOST_LAST_SITE)
////                    	 postInvalidate(p.x,p.y,p.x + imageWidth,p.y + imageHeight);
                    postInvalidate();
            	}
			}
		}
		
		new Thread(new SpeakAnimationThread()).start();
    }

	/**
	 * ��ʾ����������
	 * @param currDGFSite
	 * @param dGFStatus
	 * @throws InterruptedException
	 */
	public void toShowDGF(int currDGFSite,DGFCourse dGFStatus) throws InterruptedException{
		gameMsg.dGFCourse = dGFStatus;
		Bitmap[] images = null;
		if(dGFStatus==DGFCourse.DAO){
			images = skin.getDaoAnim();
		}else if(dGFStatus==DGFCourse.GEN){
			images = skin.getGenAnim();
		}else if(dGFStatus==DGFCourse.FAN){
			images = skin.getFanAnim();
		}
		if(images!=null) showSpeakAnimation(currDGFSite, images[currDGFSite]);
		
		if(dGFStatus == DGFCourse.FAN){						//�����ַ�  �������Ͷ���ɷ�
			for(Iterator<Integer> iter = gameMsg.playerDGFMap.keySet().iterator();iter.hasNext();){ 
				gameMsg.playerDGFMap.put(iter.next(), DGFCourse.FAN);
			} 
		}else{
			gameMsg.playerDGFMap.put(currDGFSite, dGFStatus);
		}
		if(isPlaySounds) soundManager.play(SoundPoolManager.DGF);
		Thread.sleep(500);
	}
	/**
	 * �ڹ��췽������� ��ʼ������ͼƬ������
	 */
	public void createGameOverView(){
		Player[] allPlayer = gameMsg.allPlayer;
		ruleDefined.toCalScoring();
		
		Bitmap gameOverBitMap =  Bitmap.createBitmap(420,270, android.graphics.Bitmap.Config.ARGB_8888);
		Canvas gameOverCanvas = new Canvas(gameOverBitMap);
		Matrix matrix = new Matrix();
		matrix.postScale(0.6f, 0.6f);
		
			Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			fontPaint.setColor(Color.RED);
			fontPaint.setTextSize(17);
			Typeface t = Typeface.create("",Typeface.BOLD_ITALIC);
			fontPaint.setTypeface(t);
			
			Paint bgPaint = new Paint();
			bgPaint.setColor(Color.GREEN);
			gameOverCanvas.drawRect(0, 0, 420,270, bgPaint);
		
			Paint p1 = new Paint();
			p1.setAlpha(220);
		for(int i=0;i<allPlayer.length;i++){
			gameOverCanvas.drawText(allPlayer[i].getUserName(), 70, 60+i*50, fontPaint);
			String plusMinus = "";
			if(allPlayer[i].getCurrGameScoring()>0){
				plusMinus = "+";
			}
			gameOverCanvas.drawText(plusMinus+allPlayer[i].getCurrGameScoring(),300, 60+i*50, fontPaint);
			
			String dgfFlag = "";
			if(gameMsg.playerDGFMap.get(i)==DGFCourse.DAO){
				dgfFlag = "�� x 2";
			}else if(gameMsg.playerDGFMap.get(i)==DGFCourse.GEN){
				dgfFlag = "�� x 2";
			}else if(gameMsg.playerDGFMap.get(i)==DGFCourse.FAN){
				dgfFlag = "��x 4";
			}
			gameOverCanvas.drawText(dgfFlag, 150, 65+i*50, fontPaint);
		}
		
		gameOverCanvas.drawText("�з�: "+gameMsg.getCurrCallScore(), 70, 210, fontPaint);
		gameOverCanvas.drawText("ը��: "+gameMsg.getBoomCount(), 200, 210, fontPaint);
		if(gameMsg.isSpring){
			gameOverCanvas.drawText("����", 260, 210, fontPaint);
		}
		skin.setGameOverBitMap(gameOverBitMap);
	}
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void startGame(){
		sendPokeIndex = 0;
		hostHadCall = false;
		hasSendedPoke = false;
		hasCallScore = false;
		hadPutPoke = false;
		hostHadChuPaing = false;
		hostChoosingDGF = false;
//		refrechChuPaiArea = false;
		currPa = null;
		hostChoosePoke = null;
		canSendPoke = false;
		showBottomPoke = false;
		gameMsg.sendOutPai();
		msgHandler.sendMessageDelayed(Message.obtain(msgHandler,MessageHandler.MESSAGE_SENDPOKE),500);
		invalidate();
	}
	/**
	 * ��������׼��������
	 * @return
	 */
	private ArrayList<Poke> hostChuPai(){
		ArrayList<Poke> list = new ArrayList<Poke>();
		for (int i= 0 ;i < gameMsg.playersPoke[DdzConstants.HOST_SITE].size() ; i++){
			//���Ҫ������
			if(gameMsg.pokePop[i])
				list.add(gameMsg.playersPoke[DdzConstants.HOST_SITE].get(i));
		}
		return list;
	}
	public boolean isPlaySounds(){
		return isPlaySounds;
	}
	public void setPos(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	private int getLastSite(int site){
		return site==0?2:site-1;
	}
	public void setBgMusic(MusicPlayer bgMusic) {
		this.bgMusic = bgMusic;
	}
	public void setPlaySounds(boolean isPlaySounds) {
		this.isPlaySounds = isPlaySounds;
	}
	
}


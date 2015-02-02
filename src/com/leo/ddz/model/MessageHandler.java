package com.leo.ddz.model;

import static com.leo.ddz.DdzConstants.BN_HIGHT;
import static com.leo.ddz.DdzConstants.BN_WIDTH;
import static com.leo.ddz.DdzConstants.POKE_HIGHT;
import static com.leo.ddz.DdzConstants.POKE_WIDTH;
import static com.leo.ddz.DdzConstants.SHOW_WIDTH;

import java.util.ArrayList;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import com.leo.ddz.DdzConstants;
import com.leo.ddz.ui.Position;
import com.leo.ddz.ui.SoundPoolManager;

public class MessageHandler extends Handler {
	/**发牌消息标识*/
	public static final int MESSAGE_SENDPOKE = 1;
	/**刷新界面消息标识*/
	public static final int REFRESH_VIEW = 2;
	/**轮下一家出牌消息*/  public static final int FALL_NEXT_CHUPAI = 3;
	/**胜负已分 游戏结束*/
	public static final int GAMEOVER = 4;
	/**显示得分界面*/
	public static final int SHOW_SCORING_VIEW = 5;
	/**
	/**当前消息类型*/
	private int currMessageType;
	SoundPoolManager soundManager;
	GameMsg gameMsg;
	DdzView ddzView;
	
	public MessageHandler(GameMsg gameMsg,DdzView ddzView) {
		super();
		this.ddzView = ddzView;
		this.gameMsg = gameMsg;
		soundManager = SoundPoolManager.getInstance(ddzView.getContext());
	}
    public void handleMessage(Message msg) {
    	currMessageType = msg.what;
		
		switch(msg.what){
        	case REFRESH_VIEW:
        		if(msg.obj!=null && (msg.obj instanceof Boolean) &&(Boolean)msg.obj==true){
        			if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.SHOWBOTTOM);
        			ddzView.showBottomPoke = true;
        		}else if(msg.obj!=null && (msg.obj instanceof Integer)){
//        			int currOrder = getNextSite((Integer)msg.obj);
//        			Point p = Position.getPos(currOrder, Position.CHUPAI_SITE);
//        			int size = gameMsg.turnPoke[currOrder].size();
//        			if(size>0){
//        				ddzView.invalidate(p.x,p.y,p.x+(size-1)*DdzConstants.SHOW_WIDTH+DdzConstants.POKE_WIDTH,p.y+DdzConstants.POKE_HIGHT+2);
//        			}else{
//        				ddzView.invalidate(p.x,p.y,p.x+DdzConstants.BN_WIDTH+30,p.y+DdzConstants.BN_HIGHT+30);
//        			}
////        			ddzView.refrechChuPaiArea = true;
//        			return;
//        		}
        					int currOrder = getNextSite((Integer) msg.obj);
        					Point p = Position.getPos(currOrder, Position.CHUPAI_SITE);
        					int size = gameMsg.turnPoke[currOrder].size();
        					gameMsg.turnPoke[currOrder].clear();
        					if (size > 0) {
        						ddzView.invalidate(p.x, p.y,
        								p.x + (size - 1) * DdzConstants.SHOW_WIDTH
        										+ DdzConstants.POKE_WIDTH, p.y
        										+ DdzConstants.POKE_HIGHT + 2);
        					} else {
        						ddzView.invalidate(p.x, p.y, p.x + DdzConstants.BN_WIDTH
        								+ 30, p.y + DdzConstants.BN_HIGHT + 30);
        					}
        					return;
        				}
        				ddzView.postInvalidate();
        		
        		ddzView.postInvalidate();
    			break;
    		case MESSAGE_SENDPOKE:
    			if(ddzView.sendPokeIndex>=17){
    				ddzView.hasSendedPoke = true;
    				Thread cThread = new Thread(ddzView.new ComputerThread()) ;
	        	    cThread.start();
	        		this.removeMessages(currMessageType);
	        	}else{
	        		if(ddzView.isPlaySounds())soundManager.play(SoundPoolManager.FAPAI);
	        		this.sleep(DdzConstants.SEND_POKE_DELAY);
	        		ddzView.sendPokeIndex ++;
	        	}
    			ddzView.postInvalidate();
    			break;
    		case FALL_NEXT_CHUPAI:
    			if(!gameMsg.isGameOver) ddzView.ruleDefined.nextChuPai();
    			break;
    		case GAMEOVER:
    			gameMsg.isGameOver = true;
    			if(gameMsg.getOrder()==gameMsg.getDiZhuSite()){
    				gameMsg.dizhuWin = true;
    				if(gameMsg.getDiZhuSite()==DdzConstants.HOST_SITE){
    					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.WIN);
    				}else{
    					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.LOSE);
    				}
    			}else{
    				gameMsg.dizhuWin = false;
    				if(gameMsg.getDiZhuSite()==DdzConstants.HOST_SITE){
    					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.LOSE);
    				}else{
    					if(ddzView.isPlaySounds()) soundManager.play(SoundPoolManager.WIN);
    				}
    			}
    			ddzView.createGameOverView();
    			ddzView.postInvalidate();
    			break;
    		default:
    			break;
    	}
    }
    public void sleep(long delayMillis) {
    	this.removeMessages(currMessageType);
        sendMessageDelayed(obtainMessage(currMessageType), delayMillis);
    }
	private int getNextSite(int site){
		return site==2?0:site+1;
	}
}
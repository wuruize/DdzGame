package com.leo.ddz.ui;
import android.graphics.Point;

public class Position {

	/**开始按钮的位置*/
	public final static Point START_BN = new Point(360 , 300);
	
	/**一分按钮的位置*/
	public final static Point ONE_BN = new Point(240,300);
	/**两分按钮的位置*/
	public final static Point TWO_BN = new Point(320,300);
	/**三分按钮的位置*/
	public final static Point THREE_BN = new Point(400,300);
	/**不叫按钮的位置*/
	public final static Point BJ_BN = new Point(480,300);
	
	/**出牌按钮的位置*/
	public final static Point CP_BN = new Point(400,200);
	/**取消按钮的位置*/
	public final static Point CANCEL_BN = new Point(400,300);
	/**倒跟反按钮出现的位置*/
	public final static Point DGF_BN = new Point(200,290);
	/**不出按钮的位置*/
	public final static Point BC_BN = new Point(600,300);
	/**继续按钮位置*/
	public final static Point CONTINUE_BN = new Point(400, 310);
	/**退出按钮位置*/
	public final static Point EXIT_BN = new Point(500, 310);
	/**地主头像位置*/
	public static final int DGFSTATUS_SITE = 1;
	/**叫分位置*/
	public static final int CALLSCORE_SITE = 2;
	/**叫分位置*/
	public static final int DGF_SITE =3;
	/** 说话动画位置 */
	public static final int SPEAKANIMARION_SITE = 4;
	/**出出的牌位置*/
	public static final int CHUPAI_SITE = 5;
	/**排序按钮位置*/ 
	public final static Point SORT_BN = new Point(730,340);
	/** 音乐按钮位置*/ 
	public final static Point MUSIC_BN = new Point(730,420);
	/**出出的牌位置*/
	public static final int HEAD_SITE = 6;
	public static final int GAME_VIEW_W = 800;
	public static final int GAME_VIEW_H = 480;
	
	public static Point getPos(int site, int type) {
		if (type == HEAD_SITE) {
			switch (site) {
			case 2:
				return new Point(15,10);
			case 1:
				return new Point(700,10);
			default:
				return new Point(15,330);
			}
		}else if (type == SPEAKANIMARION_SITE) {
			
			switch (site) {
			case 2:
				return new Point(90, 90);
			case 1:
				return new Point(GAME_VIEW_W - 280 + 180, 90);
			default:
				return new Point(90, GAME_VIEW_H - 220 + 90);
			}
		}else if (type == DGF_SITE) {
			switch (site) {
			case 1:
				return new Point(550, 100);
			case 2:
				return new Point(200, 100);
			default:
				return new Point(200, 300);
			}
		} else if (type == CALLSCORE_SITE) {
			switch (site) {
			case 1:
				return new Point(500, 150);
			case 2:
				return new Point(250, 150);
			default:
				return new Point(300, 220);
			}
		} else if (type == CHUPAI_SITE) {
			switch (site) {
			case 1:
				return new Point(530, 130);
			case 2:
				return new Point(190, 130);
			default:
				return new Point(350, 220);
			}
		} else if (type == DGFSTATUS_SITE) {
			switch (site) {
			case 2:
				return new Point(100, 15);
			case 1:
				return new Point(630, 15);
			default:
				return new Point(100, GAME_VIEW_H - 60);
			}
		} else {
			switch (site) {
			case 1:
				return new Point(500, 150);
			case 2:
				return new Point(220, 150);
			default:
				return new Point(350, 220);
			}
		}
	}
}

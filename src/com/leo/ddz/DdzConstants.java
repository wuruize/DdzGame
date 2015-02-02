package com.leo.ddz;

/**
 * 此类作为存放项目中使用到的常量
 * @author Leo
 *
 */
public class DdzConstants
{
	/**牌的宽度*/
	public final static int POKE_WIDTH = 73;
	/**牌的高度*/
	public final static int POKE_HIGHT = 108;
	
	/**按钮的宽度 如叫分、开始按钮*/
	public final static int BN_WIDTH = 60;
	/**按钮的高度*/
	public final static int BN_HIGHT = 30;
	/**倒跟反按钮宽度*/
	public final static int DGF_BN_W = 40;
	public final static int SORT_BN_W = 35;
	public final static int SORT_BN_H = 65;
	/**主玩家扑克y轴位置*/
	public static final int POKE_Y_POS = 350;
	public final static int MUSIC_BN_W = 35;
	
	/**成排的扑克露出显示的宽度*/
	public static final int SHOW_WIDTH = 14;
	/**主人的扑克露出显示的宽度*/
	public static final int HOST_SHOW_WIDTH = 22;
	/**选择牌时弹出的高度*/
	public static final int POP_HEIGHT = 15;
	
	/**玩家初始所持牌张数*/
	public static final int INIT_POKE_COUNT = 17;
	/**主人玩家位置*/
	public static final int HOST_SITE = 0;	
	/**主人玩家下手玩家位置*/
	public static final int HOST_NEXT_SITE = 1;
	/**主人玩家上手玩家位置*/
	public static final int HOST_LAST_SITE = 2;
	/**还没有确定地主**/
	public static final int DIZHU_IS_NULL = -1;
	/**发牌的间隔时间**/
	public static final int SEND_POKE_DELAY = 150;
	/**出牌间隔时间**/
	public static final int CHUPAI_DELAY = 1200;
	/**叫分间隔时间**/
	public static final int JIAOFEN_DELAY = 500;
	/**
	 * 每张牌在程序里实际对应得num
	 */
	public static final int Poke_3 = 0;
	public static final int Poke_4 = 1;
	public static final int Poke_5 = 2;
	public static final int Poke_6 = 3;
	public static final int Poke_7 = 4;
	public static final int Poke_8 = 5;
	public static final int Poke_9 = 6;
	public static final int Poke_10 = 7;
	public static final int Poke_J = 8;
	public static final int Poke_Q = 9;
	public static final int Poke_K = 10;
	public static final int Poke_A = 11;
	public static final int Poke_2 = 12;
	/**小王*/
	public static final int Poke_SG = 13;
	/**大王*/
	public static final int Poke_BG = 14;
	
}
package com.leo.ddz.rule;

import java.util.*;

import android.util.Log;

import com.leo.ddz.DdzConstants;
import com.leo.ddz.model.Poke;

/**
 * 牌型分析类
 * 
 * @author Leo
 * 
 */
public class PokeAnalyze {

	/** 炸弹 --- 四条 一对鬼 */
	public static final int BOOM = 1;
	/** 四条带两个(单或者对) */
	public static final int FOUR_TWO = 2;
	/** 飞机 --- 连着的三条 （三个的连） */
	public static final int PLANE = 3;
	/** 飞机带翅膀 */
	public static final int PLANE_WING = 4;
	/** 双顺 （两个的连） */
	public static final int CONN_PAIR = 5;
	/** 顺子 （单个的连） */
	public static final int SHUNZI = 6;
	/** 三条 （包含三带一类型） */
	public static final int THREE = 7;
	/** 一对 */
	public static final int PAIR = 8;
	/** 一个 */
	public static final int SINGAL = 9;

	/**
	 * 用于记录每种牌型中的主牌 比如顺子就记录最大的牌
	 */
	private Poke bigPoke = new Poke();
	/** 记录牌型 */
	private int type = 0;
	/** 记录牌的长度 */
	private int length = 0;

	/** 初始化数据 */
	List<Poke> poke;

	public PokeAnalyze(List<Poke> poke) {
		this.poke = poke;
		init();
	}

	public PokeAnalyze() {
	}

	/**
	 * 初始变量
	 */
	private void init() {
		length = 0;
		type = 0;
		bigPoke = new Poke();
	}

	/**
	 * 返回是否符合规则,如果符合,保存主牌 和 牌型
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean analyze() {
		Collections.sort(poke);
		length = poke.size();
		if (length == 0)
			return false;

		if (length == 1) {
			type = SINGAL;
			bigPoke = poke.get(0);
			return true;
		}
		if (isPair() || isShunZi() || isBoom() || isThree() || isConn_Pair()
				|| isPlaneWithWing() || isFour_Two() || isPlaneNoWing()) {
			Log.i("PokeType",type+"");
			return true;
		}
		return false;
	}

	/**
	 * 对子
	 * 
	 * @return
	 */
	private boolean isPair() {
		if (length != 2)
			return false;
		// 一对鬼 或者 对子
		if (poke.get(0).getPokeNum() == poke.get(1).getPokeNum()) {
			bigPoke = poke.get(0);
			type = PAIR;
			return true;
		} else if (poke.get(0).getPokeNum() == DdzConstants.Poke_SG
				&& poke.get(1).getPokeNum() == DdzConstants.Poke_BG) {
			bigPoke = new Poke(DdzConstants.Poke_BG, 0);
			type = BOOM;
			return true;
		} else
			return false;
	}

	/**
	 * 检查是否相同
	 * 
	 * @param poke
	 * @return
	 */
	private boolean isSame(List<Poke> poke) {
		int pokeNum = poke.get(0).getPokeNum();
		for (int i = 1; i < poke.size(); i++) {
			if (pokeNum != poke.get(i).getPokeNum())
				return false;
		}
		return true;
	}

	/**
	 * 炸弹
	 * 
	 * @return
	 */
	private boolean isBoom() {
		if (!(length == 2 || length == 4))
			return false; // 检测符合三带一的牌数量

		if (length == 2 && poke.get(0).getPokeNum() == DdzConstants.Poke_SG
				&& poke.get(1).getPokeNum() == DdzConstants.Poke_BG) {
			bigPoke = new Poke(DdzConstants.Poke_BG, 0);
			type = BOOM;
			return true;
		} else if (isSame(poke)) {
			bigPoke = poke.get(0);
			type = BOOM;
			return true;
		}
		return false;
	}

	/**
	 * 三条带一个 或者带一对
	 * 
	 * @param size
	 * @return
	 */
	private boolean isThree() {
		if (!(length == 3 || length == 4 || length == 5))
			return false; // 检测符合三带一的牌数量

		if (length == 3 && isSame(poke.subList(0, 3))) { // 三条
			bigPoke = poke.get(0);
			type = THREE;
			return true;
		} else if (length == 4) { // 三条带一个
			boolean flag = isSame(poke.subList(0, 3));
			if (flag) {
				bigPoke = poke.get(0);
				type = THREE;
				return true;
			} else {
				flag = isSame(poke.subList(1, 4));
				if (flag) {
					bigPoke = poke.get(1);
					type = THREE;
					return true;
				} else
					return false;
			}
		} else if (length == 5) { // 三条带一对
			boolean flag1 = isSame(poke.subList(0, 3));
			boolean flag2 = isSame(poke.subList(3, 5));
			if (flag1 && flag2) {
				bigPoke = poke.get(0);
				type = THREE;
				return true;
			} else {
				flag1 = isSame(poke.subList(0, 2));
				flag2 = isSame(poke.subList(2, 5));
				if (flag1 && flag2) {
					bigPoke = poke.get(2);
					type = THREE;
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	/**
	 * 顺子(2 与 鬼不能作为顺子)
	 * 
	 * @return
	 */
	private boolean isShunZi() {
		if (length < 5)
			return false; // 检测符合顺子的牌数量

		Poke p = poke.get(length - 1);  
		//不可以存在相同的两张牌
		for(int i=0;i<poke.size()-1;i++){
			if(poke.get(i).getPokeNum()==poke.get(i+1).getPokeNum()){
				return false;
			}
		}
		/**
		 * 经过排序+poke.get(0).getPokeNum() + length - 1 == p.getPokeNum()便可判断出顺子
		 */
		if (p.getPokeNum() < 12
				&& ((poke.get(0).getPokeNum() + length - 1) == p.getPokeNum())) {
			bigPoke = poke.get(length - 1);
			type = SHUNZI;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 四条带牌(带一对,带两对)
	 * 
	 * @param length
	 * @return
	 */
	private boolean isFour_Two() {
		if (!(length == 6 || length == 8))
			return false; // 检测符合四带二的牌数量
		if (length == 6) {
			boolean flag1 = isSame(poke.subList(0, 4));
			boolean flag2 = isSame(poke.subList(1, 5));
			boolean flag3 = isSame(poke.subList(2, 6));
			if (flag1) {
				bigPoke = poke.get(0);
			} else if (flag2) {
				bigPoke = poke.get(1);
			} else if (flag3) {
				bigPoke = poke.get(2);
			} else {
				return false;
			}
			type = FOUR_TWO;
			return true;
		} else {
			boolean flag1 = isSame(poke.subList(0, 4));
			boolean flag2 = isSame(poke.subList(2, 6));
			boolean flag3 = isSame(poke.subList(4, 8));

			if (flag1 && isSame(poke.subList(4, 6))
					&& isSame(poke.subList(6, 8))) {
				bigPoke = poke.get(0);
				type = FOUR_TWO;
				return true;
			} else if (flag2 && isSame(poke.subList(0, 2))
					&& isSame(poke.subList(6, 8))) {
				bigPoke = poke.get(2);
				type = FOUR_TWO;
				return true;
			} else if (flag3 && isSame(poke.subList(0, 2))
					&& isSame(poke.subList(2, 4))) {
				bigPoke = poke.get(4);
				type = FOUR_TWO;
				return true;
			} else
				return false;
		}
	}

	/**
	 * 判断是否连对
	 * 
	 * @param length
	 * @return
	 */
	private boolean isConn_Pair() {
		if (length < 6 || length % 2 != 0)
			return false; // 检测符合连队的牌数量

		boolean flag = true;
		// 判断是否是都是一对一对的
		for (int i = 0; i < length / 2; i++) {
			flag = flag && isSame(poke.subList(i * 2, i * 2 + 2));
		}
		if (flag) {
			if (poke.get(0).getPokeNum() == poke.get(length - 1).getPokeNum()
					- length / 2 + 1
					&& poke.get(length - 1).getPokeNum() < 12) {
				bigPoke = poke.get(length - 1);
				type = CONN_PAIR;
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * 牌型：飞机不带牌(包括单个三个)
	 * 
	 * @param length
	 * @return
	 */
	private boolean isPlaneNoWing() {
		if (length < 6 && length % 3 != 0)
			return false; // 检测符合飞机的牌数量

		for (int i = 0; i <= length - 3; i++) {
			if (!isSame(poke.subList(i, i + 3))) {
				return false;
			} else {
				i = i + 2;
				if (length - i > 1 && length - i < 4)
					return false;
			}
		}
		bigPoke = poke.get(poke.size() - 1);
		type = PLANE;
		return true;
	}

	private boolean isPlaneWithWing() {
		if (!(length == 8 || length == 10 || length == 12 || length == 15
				|| length == 16 || length == 20))
			return false; // 检测符合飞机带翅膀的牌数量

		List<Poke> plan = new ArrayList<Poke>();
		for (int i = 0; i <= poke.size() - 3; i++) { // 提取出飞机牌
			List<Poke> subList = poke.subList(i, i + 3);
			if (plan.size() == 0 && isSame(subList)) {
				plan.addAll(subList);
				i = i + 2;
				continue;
			} else if (plan.size() != 0 && isSame(subList)) {
				plan.addAll(subList);
				i = i + 2;
				continue;
			} else if (plan.size() != 0
					&& poke.get(i).getPokeNum() != plan.get(plan.size() - 1)
							.getPokeNum()) {
				break;
			}
		}
		if (plan.size() <= 3)
			return false;

		List<Poke> wing = new ArrayList<Poke>(poke); // 提取出带的翅膀牌
		wing.removeAll(plan);
		if (wing.size() == plan.size() / 3) {
			bigPoke = plan.get(plan.size() - 1);
			type = PLANE_WING;
			return true;
		} else if (wing.size() == (plan.size() / 3) * 2) {
			for (int i = 0; i < wing.size(); i = i + 2) {
				if (wing.get(i).getPokeNum() != wing.get(i + 1).getPokeNum()) {
					return false;
				}
			}
			bigPoke = plan.get(plan.size() - 1);
			type = PLANE_WING;
			return true;
		}
		return false;
	}

	public Poke getBigPoke() {
		return bigPoke;
	}

	public int getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

}

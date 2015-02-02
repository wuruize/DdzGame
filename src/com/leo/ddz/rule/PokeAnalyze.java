package com.leo.ddz.rule;

import java.util.*;

import android.util.Log;

import com.leo.ddz.DdzConstants;
import com.leo.ddz.model.Poke;

/**
 * ���ͷ�����
 * 
 * @author Leo
 * 
 */
public class PokeAnalyze {

	/** ը�� --- ���� һ�Թ� */
	public static final int BOOM = 1;
	/** ����������(�����߶�) */
	public static final int FOUR_TWO = 2;
	/** �ɻ� --- ���ŵ����� ������������ */
	public static final int PLANE = 3;
	/** �ɻ������ */
	public static final int PLANE_WING = 4;
	/** ˫˳ ������������ */
	public static final int CONN_PAIR = 5;
	/** ˳�� ������������ */
	public static final int SHUNZI = 6;
	/** ���� ����������һ���ͣ� */
	public static final int THREE = 7;
	/** һ�� */
	public static final int PAIR = 8;
	/** һ�� */
	public static final int SINGAL = 9;

	/**
	 * ���ڼ�¼ÿ�������е����� ����˳�Ӿͼ�¼������
	 */
	private Poke bigPoke = new Poke();
	/** ��¼���� */
	private int type = 0;
	/** ��¼�Ƶĳ��� */
	private int length = 0;

	/** ��ʼ������ */
	List<Poke> poke;

	public PokeAnalyze(List<Poke> poke) {
		this.poke = poke;
		init();
	}

	public PokeAnalyze() {
	}

	/**
	 * ��ʼ����
	 */
	private void init() {
		length = 0;
		type = 0;
		bigPoke = new Poke();
	}

	/**
	 * �����Ƿ���Ϲ���,�������,�������� �� ����
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
	 * ����
	 * 
	 * @return
	 */
	private boolean isPair() {
		if (length != 2)
			return false;
		// һ�Թ� ���� ����
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
	 * ����Ƿ���ͬ
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
	 * ը��
	 * 
	 * @return
	 */
	private boolean isBoom() {
		if (!(length == 2 || length == 4))
			return false; // ����������һ��������

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
	 * ������һ�� ���ߴ�һ��
	 * 
	 * @param size
	 * @return
	 */
	private boolean isThree() {
		if (!(length == 3 || length == 4 || length == 5))
			return false; // ����������һ��������

		if (length == 3 && isSame(poke.subList(0, 3))) { // ����
			bigPoke = poke.get(0);
			type = THREE;
			return true;
		} else if (length == 4) { // ������һ��
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
		} else if (length == 5) { // ������һ��
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
	 * ˳��(2 �� ������Ϊ˳��)
	 * 
	 * @return
	 */
	private boolean isShunZi() {
		if (length < 5)
			return false; // ������˳�ӵ�������

		Poke p = poke.get(length - 1);  
		//�����Դ�����ͬ��������
		for(int i=0;i<poke.size()-1;i++){
			if(poke.get(i).getPokeNum()==poke.get(i+1).getPokeNum()){
				return false;
			}
		}
		/**
		 * ��������+poke.get(0).getPokeNum() + length - 1 == p.getPokeNum()����жϳ�˳��
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
	 * ��������(��һ��,������)
	 * 
	 * @param length
	 * @return
	 */
	private boolean isFour_Two() {
		if (!(length == 6 || length == 8))
			return false; // �������Ĵ�����������
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
	 * �ж��Ƿ�����
	 * 
	 * @param length
	 * @return
	 */
	private boolean isConn_Pair() {
		if (length < 6 || length % 2 != 0)
			return false; // ���������ӵ�������

		boolean flag = true;
		// �ж��Ƿ��Ƕ���һ��һ�Ե�
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
	 * ���ͣ��ɻ�������(������������)
	 * 
	 * @param length
	 * @return
	 */
	private boolean isPlaneNoWing() {
		if (length < 6 && length % 3 != 0)
			return false; // �����Ϸɻ���������

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
			return false; // �����Ϸɻ�������������

		List<Poke> plan = new ArrayList<Poke>();
		for (int i = 0; i <= poke.size() - 3; i++) { // ��ȡ���ɻ���
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

		List<Poke> wing = new ArrayList<Poke>(poke); // ��ȡ�����ĳ����
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

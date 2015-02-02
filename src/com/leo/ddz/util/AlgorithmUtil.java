package com.leo.ddz.util;

import java.util.ArrayList;
import android.util.Log;

import com.leo.ddz.model.Poke;

public class AlgorithmUtil {
	/**
	 * 根据牌的类型排序
	 * 
	 * @param list
	 * @return
	 */
	public static ArrayList<Poke> sortCardByType(ArrayList<Poke> list) {
		ArrayList<Poke> newList = new ArrayList<Poke>();
		for (int i = list.size() - 1; i >= 3; i--) {
			int pokeNum = list.get(i).getPokeNum();
			if (pokeNum == list.get(i - 1).getPokeNum()
					&& pokeNum == list.get(i - 2).getPokeNum()
					&& pokeNum == list.get(i - 3).getPokeNum()) {
				newList.add(0, list.remove(i));
				newList.add(0, list.remove(i - 1));
				newList.add(0, list.remove(i - 2));
				newList.add(0, list.remove(i - 3));
				i =  list.size();
			}
		}
		for (int i = list.size() - 1; i >= 2; i--) {
			int pokeNum = 0;
			try {
				pokeNum = list.get(i).getPokeNum();
			} catch (IndexOutOfBoundsException e) {
				Log.e("IndexOutOf&&", i + "-------" + list.size());
			}
			if (pokeNum == list.get(i - 1).getPokeNum()
					&& pokeNum == list.get(i - 2).getPokeNum()) {
				newList.add(0, list.remove(i));
				newList.add(0, list.remove(i - 1));
				newList.add(0, list.remove(i - 2));
				i =  list.size();
			}
		}
		for (int i = list.size() - 1; i >= 1; i--) {
			int pokeNum = 0;
			try {
				pokeNum = list.get(i).getPokeNum();
			} catch (IndexOutOfBoundsException e) {
				Log.e("IndexOutOf&&", i + "-------" + list.size());
			}
			if (pokeNum == list.get(i - 1).getPokeNum()){
				newList.add(0, list.remove(i));
				newList.add(0, list.remove(i - 1));
				i =  list.size();
			}
		}
		newList.addAll(0, list);
		list.clear();
		list = newList;
		return list;
	}
}

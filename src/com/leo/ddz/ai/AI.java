package com.leo.ddz.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.leo.ddz.model.Poke;
import com.leo.ddz.rule.PokeAnalyze;

@SuppressWarnings("unchecked")
public class AI {

	/**
	 * 结构类  内部分为 被抽出部分和剩下部分
	 * @author Leo
	 *
	 */
	public static class StructObject{
		//被抽出部分
		public ArrayList split = new ArrayList();
		//出后所剩下部分
		public ArrayList less = new ArrayList();
		
		StructObject(ArrayList split,ArrayList less){
			this.split = split;
			this.less = less;
		}
		
		StructObject(){}
	}
	
	/**
	 * 描述顺子的对象
	 * @author Leo
	 *
	 */
	static class SunObject implements Comparable{
		int max;
		int min;
		int len;
		int nlen;
		int val;
		SunObject(int max,int min,int len,int nlen,int val){
			this.max = max;
			this.min = min;
			this.len = len;
			this.nlen = nlen;
			this.val = val;
		}
		SunObject(){}
		
		public int compareTo(Object obj)
		{
			SunObject s = (SunObject)obj;
			return this.max -  s.max ;
		}
	}
	
	/**
	 * 基本牌型 带牌类型
	 * @author Leo
	 *
	 */
	static class BasicPokeType{
		boolean huo = false;
		List<Integer> singleList = new ArrayList<Integer>();
		List<Integer> pairList = new ArrayList<Integer>();
		List<Integer> threeList = new ArrayList<Integer>();
		List<Integer> fourList = new ArrayList<Integer>();
		List sun = new ArrayList();
		List sun2 = new ArrayList();
		List sun3 = new ArrayList();
	}
	/**
	 * 单个牌型
	 * @author Leo
	 *
	 */
	static class PokeTypeItem{
		List data = new ArrayList();
		int type;
		int max;
		int len;
		PokeTypeItem(List data,int type,int max,int len){
			this.data = data;
			this.type = type;
			this.max = max;
			this.len = len;
		}
		PokeTypeItem(){}
	}
	/**
	 * 电脑接牌
	 * @param currPa
	 * @param pokes
	 * @return
	 */
	public static StructObject jiePai(PokeAnalyze currPa,ArrayList<Poke> pokes){
		BasicPokeType bpt = AI.searchPokeType(new ArrayList<Poke>(pokes));
		switch(currPa.getType()){
    	case PokeAnalyze.BOOM:
    		if(currPa.getLength()==2){//双王
    			return null;
    		}else{//四个
    			if(bpt.fourList.size()>0){
    				for (Iterator i = bpt.fourList.iterator(); i.hasNext();) {
    					Integer num = (Integer)i.next();
						if(num>currPa.getBigPoke().getPokeNum()){
							return AI.chouPai(new int[]{num,num,num,num}, pokes);
						}
					}
    			}
    		}
			break;
    	case PokeAnalyze.FOUR_TWO:
    		if(currPa.getLength()==6){//4444 7 8
    			if(bpt.fourList.size()>0){
    				for (Iterator i = bpt.fourList.iterator(); i.hasNext();) {
    					Integer num = (Integer)i.next();
						if(num>currPa.getBigPoke().getPokeNum() && bpt.singleList.size()>=2){
							return AI.chouPai(new int[]{num,num,num,num,bpt.singleList.get(0),bpt.singleList.get(1)}, pokes);
						}
					}
    			}
    		}else{//4444 77 88
    			if(bpt.fourList.size()>0){
    				for (Iterator i = bpt.fourList.iterator(); i.hasNext();) {
    					Integer num = (Integer)i.next();
						if(num>currPa.getBigPoke().getPokeNum() && bpt.pairList.size()>=2){
							int pair1 = bpt.pairList.get(0);
							int pair2 = bpt.pairList.get(1);
							return AI.chouPai(new int[]{num,num,num,num,pair1,pair1,pair2,pair2}, pokes);
						}
					}
    			}
    		}
			break;
    	case PokeAnalyze.PLANE:
    		if(currPa.getLength()==6){//333 444
    			if(bpt.sun3.size()>0){
    				for (Iterator i = bpt.sun3.iterator(); i.hasNext();) {
    					SunObject sunObj = (SunObject)i.next();
						if(sunObj.max>currPa.getBigPoke().getPokeNum()){
							int sunItem =currPa.getBigPoke().getPokeNum()+1;
							return AI.chouPai(new int[]{sunItem,sunItem,sunItem,sunItem-1,sunItem-1,sunItem-1}, pokes);
						}
					}
    			}
    		}else if(currPa.getLength()==8){//333 444 - 5 8
    			if(bpt.sun3.size()>0){
    				for (Iterator i = bpt.sun3.iterator(); i.hasNext();) {
    					SunObject sunObj = (SunObject)i.next();
						if(sunObj.max>currPa.getBigPoke().getPokeNum() && bpt.singleList.size()>=2){
							int pair1 = bpt.singleList.get(0);
							int pair2 = bpt.singleList.get(1);
							int sunItem =currPa.getBigPoke().getPokeNum()+1;
							return AI.chouPai(new int[]{sunItem,sunItem,sunItem,sunItem-1,sunItem-1,sunItem-1,pair1,pair2}, pokes);
						}
					}
    			}
    		}else if(currPa.getLength()==10){//333 444 - 55 88
    			if(bpt.sun3.size()>0){
    				for (Iterator i = bpt.sun3.iterator(); i.hasNext();) {
    					SunObject sunObj = (SunObject)i.next();
						if(sunObj.max>currPa.getBigPoke().getPokeNum() && bpt.pairList.size()>=2){
							int pair1 = bpt.pairList.get(0);
							int pair2 = bpt.pairList.get(1);
							int sunItem =currPa.getBigPoke().getPokeNum()+1;
							return AI.chouPai(new int[]{sunItem,sunItem,sunItem,sunItem-1,sunItem-1,sunItem-1,pair1,pair1,pair2,pair2}, pokes);
						}
					}
    			}
    		}else if(currPa.getLength()==9){//333 444 555
    			
    		}else if(currPa.getLength()==12){//333 444 555 - 6 8 9
    			
    		}else if(currPa.getLength()==15){//333 444 555 - 66 88 99
    			
    		}else if(currPa.getLength()==12){//333 444 555 666
    			
    		}else if(currPa.getLength()==16){//333 444 555 666- 7 8 9 10
    			
    		}else if(currPa.getLength()==20){//333 444 555 666- 77 88 99 1010
    			
    		}
			break;
    	case PokeAnalyze.CONN_PAIR:
    		if(bpt.sun2.size()>0){
    			for (Iterator i = bpt.sun2.iterator(); i.hasNext();) {
    				SunObject sunObj = (SunObject)i.next();
    				if(sunObj.max>currPa.getBigPoke().getPokeNum() && sunObj.nlen>=currPa.getLength()){
    					int[] arr = new int[currPa.getLength()];
    					for(int k=0;k<currPa.getLength()/2;k++){
    						arr[2*k] = currPa.getBigPoke().getPokeNum()+1-k;
    						arr[2*k+1] = currPa.getBigPoke().getPokeNum()+1-k;
    					}
    					return AI.chouPai(arr, pokes);
    				}
    			}
    		}
			break;
    	case PokeAnalyze.SHUNZI:
    		if(bpt.sun.size()>0){
    			for (Iterator i = bpt.sun.iterator(); i.hasNext();) {
    				SunObject sunObj = (SunObject)i.next();
    				if(sunObj.max>currPa.getBigPoke().getPokeNum() && sunObj.nlen>=currPa.getLength()){
    					ArrayList<Integer> arr = new ArrayList<Integer>();
    					int maxPokeNum = currPa.getBigPoke().getPokeNum()+1;
    					for(int k=maxPokeNum;k>maxPokeNum-currPa.getLength();k--){
    						arr.add(k);
    					}
    					Integer[] intArr = new Integer[arr.size()];
    					if(arr.size()>0) return AI.chouPai(arr.toArray(intArr), pokes);
    				}
    			}
    		}
			break;
    	case PokeAnalyze.THREE:
    		if(bpt.threeList.size()>0){
    			for (Iterator i = bpt.threeList.iterator(); i.hasNext();) {
    				Integer num = (Integer)i.next();
    				if(num>currPa.getBigPoke().getPokeNum()){
    					int[] arr = null;
    					if(currPa.getLength()==4 && bpt.singleList.size()>0){
    						arr = new int[]{num,num,num,bpt.singleList.get(0)};
    					}else if(currPa.getLength()==5 && bpt.pairList.size()>0){
    						arr = new int[]{num,num,num,bpt.pairList.get(0),bpt.pairList.get(0)};
    					}
    					return AI.chouPai(arr, pokes);
    				}
    			}
    		}
			break;
    	case PokeAnalyze.PAIR:
    		if(bpt.pairList.size()>0){
    			for (Iterator i = bpt.pairList.iterator(); i.hasNext();) {
    				Integer num = (Integer)i.next();
    				if(num>currPa.getBigPoke().getPokeNum()){
    					return AI.chouPai(new int[]{num,num}, pokes);
    				}
    			}
    		}
			break;
    	case PokeAnalyze.SINGAL:
    		if(bpt.singleList.size()>0){
    			for (Iterator i = bpt.singleList.iterator(); i.hasNext();) {
    				Integer num = (Integer)i.next();
    				if(num>currPa.getBigPoke().getPokeNum()){
    					return AI.chouPai(new int[]{num}, pokes);
    				}
    			}
    		}
			break;
		default:
			break;
		}
		return null;
	}
	
	/**
	 * 电脑主动出牌
	 * @param pokes
	 * @return
	 */
	public static StructObject zhuDongChuPai(ArrayList<Poke> pokes){
		BasicPokeType bpt = AI.searchPokeType(pokes);
		//TODO  加入带牌 及 更多AI处理
		if(bpt.sun.size()!=0){
			SunObject sunObj = (SunObject)bpt.sun.get(0);
			int[] arr = new int[sunObj.len];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = sunObj.max - i;
			}
			return AI.chouPai(arr, pokes);
		}else if(bpt.sun2.size()!=0){
			SunObject sunObj = (SunObject)bpt.sun2.get(0);
			int[] arr = new int[sunObj.nlen];
			for (int i = 0; i < sunObj.len; i++) {
				arr[2*i] = sunObj.max - i;
				arr[2*i+1] = sunObj.max - i;
			}
			return AI.chouPai(arr, pokes);
		}else if(bpt.sun3.size()!=0){
			SunObject sunObj = (SunObject)bpt.sun3.get(0);
			int[] arr = new int[sunObj.nlen];
			for (int i = 0; i < sunObj.len; i++) {
				arr[3*i] = sunObj.max - i;
				arr[3*i+1] = sunObj.max - i;
				arr[3*i+2] = sunObj.max - i;
			}
			return AI.chouPai(arr, pokes);
		}else if(bpt.threeList.size()!=0){
			int num = bpt.threeList.get(0);
			return AI.chouPai(new int[]{num,num,num}, pokes);
		}else if(bpt.pairList.size()!=0){
			int num = bpt.pairList.get(0);      
			return AI.chouPai(new int[]{num,num}, pokes);
		}else if(bpt.singleList.size()!=0){
			int num = bpt.singleList.get(0);
			return AI.chouPai(new int[]{num}, pokes);
		}
		return null;
	}
	/**
	 * 抽牌--不干涉进入数组----------------   
	 *	主要功能 按值 Num 从牌中抽出一组牌 然后再得到剩下的牌    
	 * @param arr
	 * @param pokes
	 * @return
	 */
	public static StructObject chouPai(int[] arr,ArrayList<Poke> pokes){
		if(arr==null||arr.length==0) return null;   
	    if(arr.length>pokes.size())return null;   
	    ArrayList<Poke> less = new ArrayList<Poke>(pokes);
	    ArrayList<Poke> split = new ArrayList<Poke>();
	    for(int i=0;i<arr.length;i++){
	    	for(int k=0;k<less.size();k++){
	    		if(arr[i]==less.get(k).getPokeNum()){
	    			split.add(less.get(k));
	    			less.remove(k);
	    			break;
	    		}
	    	}
	    }
	    if(split.size()!=arr.length||(split.size()+less.size())!=pokes.size())return null;   //防错处理  不满足此条件 则呈程序出错了
	    return new StructObject(split,less);
	}
	public static StructObject chouPai(Integer[] arr,ArrayList<Poke> pokes){
		int[] newArr = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i].intValue();
		}
		return chouPai(newArr, pokes);
	}
	//TODO  检查排序问题
	public static BasicPokeType searchPokeType(ArrayList<Poke> pokes){
    	if(pokes==null) return null;
    	
    	int len = pokes.size();
    	Collections.sort(pokes,new Comparator<Poke>() {
    		@Override
    		public int compare(Poke o1, Poke o2) {
    			if(o1.getPokeNum()>o2.getPokeNum()){
    				return -1;
    			}
    			return 0;
    		}
		});//按大到小排序
    	boolean huo = false;       //是否存在火箭---   
        List<Integer> singleList = new ArrayList<Integer>(); //单牌   
        List<Integer> pairList = new ArrayList<Integer>(); //对子   
        List<Integer> threeList = new ArrayList<Integer>(); //三张   
        List<Integer> fourList = new ArrayList<Integer>(); //四张   
        List sun = new ArrayList(); //单顺   
        List sun2 = new ArrayList();//双顺   
        List sun3 = new ArrayList();//三顺     
        
        if(len>1 && pokes.get(0).getPokeNum()==13 && pokes.get(1).getPokeNum()==14){
        	huo = true; //牌面中已经排序过了 所以 如果有火箭 就在最后两张
        }
    	//加入单子
        for(int i=0;i<len;i++){
        	//如果没有加入 和 加入的不重复 就吧单牌加入进去 
        	if(singleList.size()==0 || pokes.get(i).getPokeNum()!=singleList.get(singleList.size()-1)){
        		//如果是对王则不为单
        		if(!(huo&&pokes.get(i).getPokeNum()==13))singleList.add(pokes.get(i).getPokeNum()); 
        	}
        }

        ///类似加入 对子
        for(int i=0;i<len-1;i++){
        	if(pokes.get(i).getPokeNum()==pokes.get(i+1).getPokeNum()){
        		if(pairList.size()==0 || pokes.get(i).getPokeNum()!=pairList.get(pairList.size()-1)){
        			 pairList.add(pokes.get(i).getPokeNum()); 
        			i++;
        		}
        	}
        }
        //加入三张
        for(int i=0;i<len-2;i++){
        	if(pokes.get(i).getPokeNum()==pokes.get(i+1).getPokeNum() 
        			&& pokes.get(i).getPokeNum()==pokes.get(i+2).getPokeNum()){
        		if(threeList.size()==0 || pokes.get(i).getPokeNum()!=threeList.get(threeList.size()-1)){
        			threeList.add(pokes.get(i).getPokeNum());
        			i+= 2;
        		}
        	}
        }
        //加入四张
        for(int i=0;i<len-3;i++){
        	if(pokes.get(i).getPokeNum()==pokes.get(i+1).getPokeNum() 
        			&& pokes.get(i).getPokeNum()==pokes.get(i+2).getPokeNum()
        			&& pokes.get(i).getPokeNum()==pokes.get(i+3).getPokeNum()){
        		if(fourList.size()==0 || pokes.get(i).getPokeNum()!=fourList.get(fourList.size()-1)){
        			fourList.add(pokes.get(i).getPokeNum());
        			i+= 3;
        		}
        	}
        }
        //找单顺
		for (int i = 0; i < singleList.size(); i++) {
			if (singleList.get(i) > 11)	continue;//2不不能做顺子
			if (singleList.get(i) < 4)	break;//顺子最大的不能小于7
			for (int k = i; k < singleList.size(); k++) {
				int n = k + 1;
				if (n == singleList.size() || singleList.get(i) - singleList.get(n) != n - i) {
					int length = n -i;
					if(length>4){
						int val = 5; //GetSun1List(Dan[i],Dan[k]);
						SunObject sunObj = new SunObject();
						sunObj.max = singleList.get(i);
						sunObj.min = singleList.get(k);
						sunObj.len = length;
						sunObj.nlen = length;
						sunObj.val = val;
						sun.add(sunObj);
					}
					i = k;
					break;
				}
			}
		}
		
		//找双顺
		for (int i = 0; i < pairList.size(); i++) {
			if (pairList.get(i) > 11)	continue;
			if (pairList.get(i) < 2)	break;
			for (int k = i; k < pairList.size(); k++) {
				int n = k + 1;
				if (n == pairList.size() || pairList.get(i) - pairList.get(n) != n - i) {
					int length = n -i;
					if(length>2){
						int val = 5; //GetSun1List(Dan[i],Dan[k]);
						SunObject sun2Obj = new SunObject();
						sun2Obj.max = pairList.get(i);
						sun2Obj.min = pairList.get(k);
						sun2Obj.len = length;
						sun2Obj.nlen = length*2;
						sun2Obj.val = val;
						sun2.add(sun2Obj);
					}
					i = k;
					break;
				}
			}
		}
		
		//找三顺
		for (int i = 0; i < threeList.size(); i++) {
			if (threeList.get(i) > 11)	continue;
			if (threeList.get(i) < 1)	break;
			for (int k = i; k < threeList.size(); k++) {
				int n = k + 1;
				if (n == threeList.size() || threeList.get(i) - threeList.get(n) != n - i) {
					int length = n -i;
					if(length>1){
						int val = 5; //GetSun1List(Dan[i],Dan[k]);
						SunObject sun3Obj = new SunObject();
						sun3Obj.max = threeList.get(i);
						sun3Obj.min = threeList.get(k);
						sun3Obj.len = length;
						sun3Obj.nlen =  length*3;
						sun3Obj.val = val;
						sun3.add(sun3Obj);
					}
					i = k;
					break;
				}
			}
		}
		BasicPokeType basicPokeType = new BasicPokeType();
		basicPokeType.huo = huo;
		basicPokeType.singleList = singleList;
		basicPokeType.pairList = pairList;
		basicPokeType.threeList = threeList;
		basicPokeType.fourList = fourList;
		basicPokeType.sun = sun;
		basicPokeType.sun2 = sun2;
		basicPokeType.sun3 = sun3;
		
		Collections.sort(singleList);   
		Collections.sort(pairList);
		Collections.sort(threeList);
		Collections.sort(fourList);
		Collections.sort(sun);
		Collections.sort(sun2);
		Collections.sort(sun3);
		return basicPokeType;
    }
}

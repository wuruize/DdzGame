package com.leo.ddz.model;


/**
 * �˿˶���
 * @author Leo
 *
 */
@SuppressWarnings("unchecked")
public class Poke implements Comparable
{
	//��ֵ(�ƵĴ�С�������� 0 - 14 )
	private int pokeNum;
	//�ƵĻ�ɫ(�ӷ��鿪ʼ����� 0 - 3)
	private int pokeColor;
	public Poke()
	{
	}
	public Poke(int pokeNum, int pokeColor)
	{
		this.pokeNum = pokeNum;
		this.pokeColor = pokeColor;
	}
	public void setPokeNum(int pokeNum)
	{
		this.pokeNum = pokeNum;
	}
	public int getPokeNum()
	{
		return this.pokeNum;
	}
	public void setPokeColor(int pokeColor)
	{
		this.pokeColor = pokeColor;
	}
	public int getPokeColor()
	{
		return this.pokeColor;
	}
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;
		if(obj != null && obj.getClass() == Poke.class)
		{
			Poke poke = (Poke) obj;
			if(poke.getPokeNum() == pokeNum && poke.getPokeColor() == pokeColor)
			{
				return true;
			}
		}
		return false;
	}
	public int compareTo(Object obj)
	{
		Poke p = (Poke)obj;
		return pokeNum -  p.getPokeNum() ;
	}

}

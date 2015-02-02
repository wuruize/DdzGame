package com.leo.ddz.ui.skin;

import com.leo.ddz.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class DefaultSkin extends Skin {
	
	private static DefaultSkin defaultSkin;
	private DefaultSkin(Context context){
		super();
		super.context = context;
		init();
	}
	public static Skin getInstance(Context context) {
		if(defaultSkin==null){
			defaultSkin = new DefaultSkin(context);
		}
		 return defaultSkin;
	}

	@Override
	public void init() {
		Resources res = context.getResources();
		try {
			// 桌面背景
			daoStatus = BitmapFactory.decodeResource(res, R.drawable.dao_status);
			genStatus = BitmapFactory.decodeResource(res, R.drawable.gen_status);
			fanStatus = BitmapFactory.decodeResource(res, R.drawable.fan_status);
			// 牌
			pokeImage = new Bitmap[15][4];
			
			pokeImage[0][0] = BitmapFactory.decodeResource(res, R.drawable.pai_30);
			pokeImage[0][1] = BitmapFactory.decodeResource(res, R.drawable.pai_31);
			pokeImage[0][2] = BitmapFactory.decodeResource(res, R.drawable.pai_32);
			pokeImage[0][3] = BitmapFactory.decodeResource(res, R.drawable.pai_33);
			
			pokeImage[1][0] = BitmapFactory.decodeResource(res, R.drawable.pai_40);
			pokeImage[1][1] = BitmapFactory.decodeResource(res, R.drawable.pai_41);
			pokeImage[1][2] = BitmapFactory.decodeResource(res, R.drawable.pai_42);
			pokeImage[1][3] = BitmapFactory.decodeResource(res, R.drawable.pai_43);
			
			pokeImage[2][0] = BitmapFactory.decodeResource(res, R.drawable.pai_50);
			pokeImage[2][1] = BitmapFactory.decodeResource(res, R.drawable.pai_51);
			pokeImage[2][2] = BitmapFactory.decodeResource(res, R.drawable.pai_52);
			pokeImage[2][3] = BitmapFactory.decodeResource(res, R.drawable.pai_53);
			
			pokeImage[3][0] = BitmapFactory.decodeResource(res, R.drawable.pai_60);
			pokeImage[3][1] = BitmapFactory.decodeResource(res, R.drawable.pai_61);
			pokeImage[3][2] = BitmapFactory.decodeResource(res, R.drawable.pai_62);
			pokeImage[3][3] = BitmapFactory.decodeResource(res, R.drawable.pai_63);
			
			pokeImage[4][0] = BitmapFactory.decodeResource(res, R.drawable.pai_70);
			pokeImage[4][1] = BitmapFactory.decodeResource(res, R.drawable.pai_71);
			pokeImage[4][2] = BitmapFactory.decodeResource(res, R.drawable.pai_72);
			pokeImage[4][3] = BitmapFactory.decodeResource(res, R.drawable.pai_73);
			
			pokeImage[5][0] = BitmapFactory.decodeResource(res, R.drawable.pai_80);
			pokeImage[5][1] = BitmapFactory.decodeResource(res, R.drawable.pai_81);
			pokeImage[5][2] = BitmapFactory.decodeResource(res, R.drawable.pai_82);
			pokeImage[5][3] = BitmapFactory.decodeResource(res, R.drawable.pai_83);
			
			pokeImage[6][0] = BitmapFactory.decodeResource(res, R.drawable.pai_90);
			pokeImage[6][1] = BitmapFactory.decodeResource(res, R.drawable.pai_91);
			pokeImage[6][2] = BitmapFactory.decodeResource(res, R.drawable.pai_92);
			pokeImage[6][3] = BitmapFactory.decodeResource(res, R.drawable.pai_93);
			
			pokeImage[7][0] = BitmapFactory.decodeResource(res, R.drawable.pai_100);
			pokeImage[7][1] = BitmapFactory.decodeResource(res, R.drawable.pai_101);
			pokeImage[7][2] = BitmapFactory.decodeResource(res, R.drawable.pai_102);
			pokeImage[7][3] = BitmapFactory.decodeResource(res, R.drawable.pai_103);
			
			pokeImage[8][0] = BitmapFactory.decodeResource(res, R.drawable.pai_j0);
			pokeImage[8][1] = BitmapFactory.decodeResource(res, R.drawable.pai_j1);
			pokeImage[8][2] = BitmapFactory.decodeResource(res, R.drawable.pai_j2);
			pokeImage[8][3] = BitmapFactory.decodeResource(res, R.drawable.pai_j3);
			
			pokeImage[9][0] = BitmapFactory.decodeResource(res, R.drawable.pai_q0);
			pokeImage[9][1] = BitmapFactory.decodeResource(res, R.drawable.pai_q1);
			pokeImage[9][2] = BitmapFactory.decodeResource(res, R.drawable.pai_q2);
			pokeImage[9][3] = BitmapFactory.decodeResource(res, R.drawable.pai_q3);
			
			pokeImage[10][0] = BitmapFactory.decodeResource(res, R.drawable.pai_k0);
			pokeImage[10][1] = BitmapFactory.decodeResource(res, R.drawable.pai_k1);
			pokeImage[10][2] = BitmapFactory.decodeResource(res, R.drawable.pai_k2);
			pokeImage[10][3] = BitmapFactory.decodeResource(res, R.drawable.pai_k3);
			
			pokeImage[11][0] = BitmapFactory.decodeResource(res, R.drawable.pai_a0);
			pokeImage[11][1] = BitmapFactory.decodeResource(res, R.drawable.pai_a1);
			pokeImage[11][2] = BitmapFactory.decodeResource(res, R.drawable.pai_a2);
			pokeImage[11][3] = BitmapFactory.decodeResource(res, R.drawable.pai_a3);
			
			pokeImage[12][0] = BitmapFactory.decodeResource(res, R.drawable.pai_20);
			pokeImage[12][1] = BitmapFactory.decodeResource(res, R.drawable.pai_21);
			pokeImage[12][2] = BitmapFactory.decodeResource(res, R.drawable.pai_22);
			pokeImage[12][3] = BitmapFactory.decodeResource(res, R.drawable.pai_23);
			
			pokeImage[13][0] = BitmapFactory.decodeResource(res, R.drawable.pai_g0);
			pokeImage[14][0] = BitmapFactory.decodeResource(res, R.drawable.pai_g1);
			// 不叫按钮
			bjImage = new Bitmap[2];
			bjImage[0] = BitmapFactory.decodeResource(res, R.drawable.bj0);
			bjImage[1] = BitmapFactory.decodeResource(res, R.drawable.bj1);
			// 不出按钮
			bcImage = new Bitmap[2];
			bcImage[0] = BitmapFactory.decodeResource(res, R.drawable.bc0);
			bcImage[1] = BitmapFactory.decodeResource(res, R.drawable.bc1);
			// 出牌
			cpImage = new Bitmap[3];
			cpImage[0] = BitmapFactory.decodeResource(res, R.drawable.cp0);
			cpImage[1] = BitmapFactory.decodeResource(res, R.drawable.cp1);
			cpImage[2] = BitmapFactory.decodeResource(res, R.drawable.cp2);
			// 一分
			oneImage = new Bitmap[3];
			oneImage[0] = BitmapFactory.decodeResource(res, R.drawable.one0);
			oneImage[1] = BitmapFactory.decodeResource(res, R.drawable.one1);
			oneImage[2] = BitmapFactory.decodeResource(res, R.drawable.one2);

			twoImage = new Bitmap[3];
			twoImage[0] = BitmapFactory.decodeResource(res, R.drawable.two0);
			twoImage[1] = BitmapFactory.decodeResource(res, R.drawable.two1);
			twoImage[2] = BitmapFactory.decodeResource(res, R.drawable.two2);
			
			threeImage = new Bitmap[2];
			threeImage[0] = BitmapFactory.decodeResource(res, R.drawable.three0);
			threeImage[1] = BitmapFactory.decodeResource(res, R.drawable.three1);
			
			daoImage = new Bitmap[3];
			daoImage[0] = BitmapFactory.decodeResource(res, R.drawable.dao0);
			daoImage[1] = BitmapFactory.decodeResource(res, R.drawable.dao1);
			
			genImage = new Bitmap[3];
			genImage[0] = BitmapFactory.decodeResource(res, R.drawable.gen0);
			genImage[1] = BitmapFactory.decodeResource(res, R.drawable.gen1);
			
			fanImage = new Bitmap[3];
			fanImage[0] = BitmapFactory.decodeResource(res, R.drawable.fan0);
			fanImage[1] = BitmapFactory.decodeResource(res, R.drawable.fan1);

			
			calScoreBar = BitmapFactory.decodeResource(res, R.drawable.calscorebar);
			sortImage = new Bitmap[2];
			sortImage[0] = BitmapFactory.decodeResource(res, R.drawable.sort0);
			sortImage[1] = BitmapFactory.decodeResource(res, R.drawable.sort1);
			
			continueBtnImage = new Bitmap[2];
			continueBtnImage[0] = BitmapFactory.decodeResource(res, R.drawable.button_continue_0);
			continueBtnImage[1] = BitmapFactory.decodeResource(res, R.drawable.button_continue_1);
			
			exitBtnImage = new Bitmap[2];
			exitBtnImage[0] = BitmapFactory.decodeResource(res, R.drawable.button_exit_0);
			exitBtnImage[1] = BitmapFactory.decodeResource(res, R.drawable.button_exit_1);
			
			headImage = new Bitmap[2];
			headImage[0] = BitmapFactory.decodeResource(res, R.drawable.farmer_head);
			headImage[1] = BitmapFactory.decodeResource(res, R.drawable.dz_head);
			 passAnim = new Bitmap[3];
				passAnim[0] = BitmapFactory.decodeResource(res, R.drawable.by_0_1,null);
				passAnim[1] = BitmapFactory.decodeResource(res, R.drawable.by_1_1,null);
				passAnim[2] = BitmapFactory.decodeResource(res, R.drawable.by_2_1,null);
				
				daoAnim = new Bitmap[3];
				daoAnim[0] = BitmapFactory.decodeResource(res, R.drawable.dao_0_1,null);
				daoAnim[1] = BitmapFactory.decodeResource(res, R.drawable.dao_1_1,null);
				daoAnim[2] = BitmapFactory.decodeResource(res, R.drawable.dao_2_1,null);
				
				genAnim = new Bitmap[3];
				genAnim[0] = BitmapFactory.decodeResource(res, R.drawable.gen_0_1,null);
				genAnim[1] = BitmapFactory.decodeResource(res, R.drawable.gen_1_1,null);
				genAnim[2] = BitmapFactory.decodeResource(res, R.drawable.gen_2_1,null);
				
				fanAnim = new Bitmap[3];
				fanAnim[0] = BitmapFactory.decodeResource(res, R.drawable.fan_0_1,null);
				fanAnim[1] = BitmapFactory.decodeResource(res, R.drawable.fan_1_1,null);
				fanAnim[2] = BitmapFactory.decodeResource(res, R.drawable.fan_2_1,null);
				
				onePointAnim = new Bitmap[3];
				onePointAnim[0] = BitmapFactory.decodeResource(res, R.drawable.point1_1_1,null);
				onePointAnim[1] = BitmapFactory.decodeResource(res, R.drawable.point1_2_1,null);
				onePointAnim[2] = BitmapFactory.decodeResource(res, R.drawable.point1_3_1,null);
				
				twoPointAnim = new Bitmap[3];
				twoPointAnim[0] = BitmapFactory.decodeResource(res, R.drawable.point2_1_1,null);
				twoPointAnim[1] = BitmapFactory.decodeResource(res, R.drawable.point2_2_1,null);
				twoPointAnim[2] = BitmapFactory.decodeResource(res, R.drawable.point2_3_1,null);
				
				threePointAnim = new Bitmap[3];
				threePointAnim[0] = BitmapFactory.decodeResource(res, R.drawable.point3_1_1,null);
				threePointAnim[1] = BitmapFactory.decodeResource(res, R.drawable.point3_2_1,null);
				threePointAnim[2] = BitmapFactory.decodeResource(res, R.drawable.point3_3_1,null);
				
				noCallAnim = new Bitmap[3];
				noCallAnim[0] = BitmapFactory.decodeResource(res, R.drawable.point0_1_1,null);
				noCallAnim[1] = BitmapFactory.decodeResource(res, R.drawable.point0_2_1,null);
				noCallAnim[2] = BitmapFactory.decodeResource(res, R.drawable.point0_3_1,null);
			Bitmap numberImage = BitmapFactory.decodeResource(res,R.drawable.numbers);	
			numbers = new Bitmap[12];
	        for (int i = 0; i < 12; i++) {
	        	numbers[i] = Bitmap.createBitmap(numberImage,i * 70,0,70,40);
	        }
	        
	        Bitmap gradeNames = BitmapFactory.decodeResource(res,R.drawable.grade_names);	
	        gradeNameImage = new Bitmap[8];
	        for (int i = 0; i < 8; i++) {
	        	gradeNameImage[i] = Bitmap.createBitmap(gradeNames,0,i * 30,85,30);
	        }
	        musicBtnImage = new Bitmap[2];
	        musicBtnImage[0] = BitmapFactory.decodeResource(res, R.drawable.music_pause);
	        musicBtnImage[1] = BitmapFactory.decodeResource(res, R.drawable.music_resume);

	        paiCountPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paiCountPaint.setColor(Color.WHITE);
			paiCountPaint.setTextSize(15);
			
			remainPokeCount = new Paint(Paint.ANTI_ALIAS_FLAG);
			remainPokeCount.setColor(Color.WHITE);
			remainPokeCount.setTextSize(20);
			
			
		} catch (Exception ex) {
			Log.i("Exception","load image failed");
			ex.printStackTrace();
		}
	}

}

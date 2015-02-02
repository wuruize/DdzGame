package com.leo.ddz.ui;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class MatrixAnimation {

	public static final int DEFUALT_PROGRESS = 11;
	public static final int INTERVALE = 20;								//动画间隔时间   \毫秒数
	
	private Bitmap primaryBitmap = null;
	private int totalProgress = 0;										//指定为奇数
	private final int extremeValue;
	private int progress = 1;

	public MatrixAnimation(Bitmap primaryBitmap) {
		this.primaryBitmap = primaryBitmap;
		
		if(totalProgress == 0)
			totalProgress = DEFUALT_PROGRESS;
		if (totalProgress % 2 == 0)
			throw new RuntimeException(
					"The property totalProgress value invalid");
		
		extremeValue = totalProgress / 2 + 1;
	}

	public Bitmap getCurrentView() {
		float multiple = progress > extremeValue ? 
				(2 * extremeValue - progress): progress;
		float scal = multiple * (1 / ((float)extremeValue));
		Matrix matrix = new Matrix();
		matrix.postScale(scal,scal);
		Bitmap newBitmap = Bitmap.createBitmap(primaryBitmap, 0, 0,
				primaryBitmap.getWidth(), primaryBitmap.getHeight(), matrix,
				true);
		return newBitmap;
	}

	public boolean next() {
		progress++;
		return true;
	}
	public boolean completed() {
		return progress > totalProgress;
	}

	public Bitmap getPrimaryBitmap() {
		return primaryBitmap;
	}

	public void setPrimaryBitmap(Bitmap primaryBitmap) {
		this.primaryBitmap = primaryBitmap;
	}
	
	public void setTotalProgress(int totalProgress) {
		this.totalProgress = totalProgress;
	}
	public final int getExtremeValue() {
		return extremeValue;
	}
	public int getProgress() {
		return progress;
	}

}

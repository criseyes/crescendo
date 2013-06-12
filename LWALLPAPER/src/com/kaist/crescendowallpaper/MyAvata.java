package com.kaist.crescendowallpaper;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MyAvata {
	public int startX, startY; // Á¡ÀÇ ÁÂÇ¥
	private static final int MAX_ICON_SIZE_X = 150;
	private static final int MAX_ICON_SIZE_Y = 180;
	private static final int MAX_COUNT = 150;
	private static final int MIN_COUNT = 50;
	private static final Random rand = new Random();
	
	private Paint paint;
	private Bitmap me;
	private int countX;
	private int countY;
	private int directX;
	private int directY;
	
	public MyAvata(Bitmap img) {
        startX = rand.nextInt(StarWallpaper.Width - MyAvata.MAX_ICON_SIZE_X) + MyAvata.MAX_ICON_SIZE_X;
        startY = rand.nextInt(StarWallpaper.Height - MyAvata.MAX_ICON_SIZE_Y) + MyAvata.MAX_ICON_SIZE_Y;

        paint = new Paint();
        me = Bitmap.createScaledBitmap(img, MAX_ICON_SIZE_X, MAX_ICON_SIZE_Y, true);
   }
	
	public void moveBySelf() {
		if(countX == 0) {
			countX = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directX = rand.nextBoolean() == true? 1:-1;
		}
		
		if(countY == 0) {
			countY = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directY = rand.nextBoolean() == true? 1:-1;
		}
		
		startX += directX;
		startY += directY;
		
		if(startX < 0 || startX > StarWallpaper.Width) /* it's dangerous, don't cross the line */ 
		{
			directX *= -1;
			countX = MAX_COUNT; /* I'll give you the POWER to escape */
		}
		
		if(startY < 0 || startY > StarWallpaper.Height)
		{
			directY *= -1;
			countY = MAX_COUNT;
		}
		
		countX--;
		countY--;
	}
	
	public void setPosition(int x, int y) {
		startX = x;
		startY = y;
	}
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(me, startX, startY, paint);
	}

	public boolean onTouch(MotionEvent event) {
		
		/* check to consume touch event */
		
		return false;
	}
}

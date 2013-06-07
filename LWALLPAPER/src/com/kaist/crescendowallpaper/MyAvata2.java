package com.kaist.crescendowallpaper;

import java.util.ArrayList;
import java.util.Random;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class MyAvata2 {
	private static final int MAX_COUNT = 150;
	private static final int MIN_COUNT = 50;
	private static final int BODY_SX = 100;
	private static final int BODY_SY = 100;
	private static final int HEAD_SY = 70;
	private static final int HEAD_SX = 70;
	private static final int HEAD_SXOFFSET = (BODY_SX - HEAD_SY)/2;
	
	private static final Random rand = new Random();
	private static final long DOUBLE_TAP_INTV = 500;
	
	public int startX, startY; // Á¡ÀÇ ÁÂÇ¥
	private int countX;
	private int countY;
	private int directX = 1;
	private int directY = 1;

	private int headIndex = 0;
	private int bodyIndex = 0;
	private ArrayList<Bitmap> headImgs = new ArrayList<Bitmap>();
	private ArrayList<Bitmap> bodyImgs = new ArrayList<Bitmap>();
	
	private boolean isStickyMode;
	private int waitToStickyMode;
	private long lastTapEventTime;
	private boolean isNeedDrawDirecty;
	private Context mContext;
	
	public MyAvata2(Context context, int type) {
        startX = rand.nextInt(StarWallpaper.Width - BODY_SX);
        startY = rand.nextInt(StarWallpaper.Height - (HEAD_SY + BODY_SY));

        headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop), HEAD_SX, HEAD_SY, true));
        headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop_press), HEAD_SX, HEAD_SY, true));
          
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_0), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_1), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_2), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_3), BODY_SX, BODY_SY, true));
        
        mContext = context;

   }
	
	public void moveBySelf() {
		if(isNeedDrawDirecty) {
			isNeedDrawDirecty = false;
			return;
		}
		if(countX == 0) {
			countX = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directX = rand.nextBoolean() == true? 1:-1;
			
			headIndex++;
		}
		
		if(countY == 0) {
			countY = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directY = rand.nextBoolean() == true? 1:-1;
			
			bodyIndex++;
		}
		
		
		
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
		
		startX += directX;
		startY += directY;
		
		countX--;
		countY--;
	}
	
	private Bitmap getHeadBitmap() 
	{
		if(headIndex >= headImgs.size())
			headIndex = 0;
		return headImgs.get(headIndex);
	}
	
	private Bitmap getBodyBitmap()
	{
		if(bodyIndex >= bodyImgs.size())
			bodyIndex = 0;
		return bodyImgs.get(bodyIndex);
	}
	
	public void setPosition(int x, int y) {
		startX = x;
		startY = y;
		isNeedDrawDirecty = true;
	}
	
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(getHeadBitmap(), startX + HEAD_SXOFFSET, startY, null);
		canvas.drawBitmap(getBodyBitmap(), startX, startY + HEAD_SY , null);
	}

	public boolean onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		//v.dispatchTouchEvent(event);
		//v.
		//head.dispatchTouchEvent(event);
		//Log.d("MyTag", "onTouch : " + event.getAction()+"  x= "+ event.getX()+"y= "+ event.getY());

		if(event.getAction() == MotionEvent.ACTION_MOVE) /* to catch drag gesture */
		{
			if(isStickyMode == true || waitToStickyMode > 0) {
				isStickyMode = true;
				setPosition((int) event.getX()-50, (int) event.getY()-50);
				return true;
			}
			else 
				{
					isStickyMode = false;
					return false;
				}
		}
		
		if(isMyPosition(event)) /* I want to know the event */
		{
			
			
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				waitToStickyMode = 1;
			else
				waitToStickyMode = 0;
			return true;
		}
		else {
			waitToStickyMode = 0;
			isStickyMode = false;
		}
		return false;
	}
	
	private boolean isMyPosition(MotionEvent event)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();
		

		
		if(x > startX && x < startX + BODY_SX)
			if(y > startY && y < startY + HEAD_SY + BODY_SY)
				return true;
		
		return false;
	}
	
	private boolean isMyPosition(int x, int y)
	{
		
		if(x > startX && x < startX + BODY_SX)
			if(y > startY && y < startY + HEAD_SY + BODY_SY)
				return true;
		
		return false;
	}
	
	private void wowDoubleTap()
	{
		Intent intent = new Intent();
		intent.setAction("android.intent.action.callcrescendo");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		mContext.startActivity(intent );
		
	}

	/* to get the user's double tap event */
	public boolean onCommand(String action, int x, int y, int z, long tapTime) {
		
		if(action.equals(WallpaperManager.COMMAND_TAP.toString()) && isMyPosition(x,y))
		{
			isStickyMode = false;
			
			if(tapTime - lastTapEventTime < 500 )
				wowDoubleTap();
			lastTapEventTime = tapTime;
		}
		
		return false;
	}
}

package com.kaist.crescendowallpaper;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;

public class MyAvata2 {
	private static final int MAX_COUNT = 150;
	private static final int MIN_COUNT = 50;
	private static final int BODY_SX = 100;
	private static final int BODY_SY = 100;
	private static final int HEAD_SY = 90;
	private static final int HEAD_SX = 90;
	private static final int HEAD_SXOFFSET = (BODY_SX - HEAD_SY)/2;
	private static final int INDICATOR_SY = 20;
	private static final int WORDBALLON_WIDTH = 150;
	private static final int MAX_LINENUMBER = 10;
	
	private static final Random rand = new Random();
	private static final long DOUBLE_TAP_INTV = 600;
	
	public int startX, startY; // 점의 좌표
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
	private boolean isShowBalloon = true;
	private Paint	paint;
	private ArrayList<String> words = new ArrayList<String>();
	private int lineHeight;
	
	public static final String AVATA_FILNENAME = "myAvata.png";
	
	public MyAvata2(Context context, Context appContext, int type, String name, boolean isMe) {
        startX = rand.nextInt(StarWallpaper.Width - BODY_SX);
        startY = rand.nextInt(StarWallpaper.Height - (HEAD_SY + BODY_SY));

//        headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop), HEAD_SX, HEAD_SY, true));
//        headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop_press), HEAD_SX, HEAD_SY, true));
        
        appContext.getFileStreamPath(AVATA_FILNENAME);
        File file;
        if(isMe)
        	file = appContext.getFileStreamPath(AVATA_FILNENAME);
        else
        	file = appContext.getFileStreamPath(AVATA_FILNENAME);
        
		if(file.exists() == false)
			headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop), HEAD_SX, HEAD_SY, true));
		else {
			Bitmap bit = BitmapFactory.decodeFile(file.getPath().toString());
			if(bit==null)
				headImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.man_shop), HEAD_SX, HEAD_SY, true));
			else
				headImgs.add(Bitmap.createScaledBitmap(bit, HEAD_SX, HEAD_SY, true));
		}
          
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_0), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_1), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_2), BODY_SX, BODY_SY, true));
        bodyImgs.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.sbody0_3), BODY_SX, BODY_SY, true));
        
        mContext = context;

        paint = new Paint();
        //paint.setColor(Color.YELLOW); 
        paint.setStyle(Style.FILL); 
        

        
        paint.setTextSize(30); 
        
        /* test
         * TODO remove this code.
         */
        setText("바보야 바보야.. 너는 살을 언제나 뺄려고 그러니.. 밥 좀 그만 먹어 ");
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
		
		
		
		if(startX  < 0 || startX > StarWallpaper.Width - BODY_SX) /* it's dangerous, don't cross the line */ 
		{
			directX *= -1;
			countX = MAX_COUNT; /* I'll give you the POWER to escape */
		}
		
		if(startY < INDICATOR_SY || startY > StarWallpaper.Height - (HEAD_SY+BODY_SY+20))
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
	
	private void setText(String text) {
		
		breakText(paint, text, WORDBALLON_WIDTH);
		Rect bounds = new Rect();
		
		if(words.size() > 1 && words.get(0) != null)
		{
			paint.getTextBounds(words.get(0), 0, words.get(0).length(), bounds);
			lineHeight = bounds.height() + 5;
		}
	}
	
	public void draw(Canvas canvas) {
		
		canvas.drawBitmap(getBodyBitmap(), startX, startY + HEAD_SY , null);
		
		
		if(isShowBalloon)
		{
			Rect rect = new Rect(startX + HEAD_SXOFFSET + HEAD_SX, 
					startY + HEAD_SY -10 - lineHeight , 
					startX + HEAD_SXOFFSET + HEAD_SX+WORDBALLON_WIDTH + 3, 
					startY + HEAD_SY + lineHeight*words.size() -10);
			//canvas.drawRoundRect(rect, 1, 1, paint);
			paint.setColor(Color.YELLOW); 
			canvas.drawRect(rect, paint);

			//canvas.drawPaint(paint);

			paint.setColor(Color.BLACK);
			for(int i = 0 ; i < words.size() ; i++) {
				
				canvas.drawText(words.get(i), 
						startX + HEAD_SXOFFSET + HEAD_SX + 5, 
						startY + HEAD_SY  + (i*lineHeight) - 7 , 
						paint);
			}
		}
		
		canvas.drawBitmap(getHeadBitmap(), startX + HEAD_SXOFFSET  , startY+ 4 /* overlap */, null);
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
			
			if(tapTime - lastTapEventTime < DOUBLE_TAP_INTV )
				wowDoubleTap();
			lastTapEventTime = tapTime;
		}
		
		return false;
	}
	
	/** 
     * 줄바꿈 
     * @param textPaint        TextView의 Paint 객체 
     * @param strText        문자열 
     * @param breakWidth    줄바꿈 하고 싶은 width값 지정     
     * @return 
     */ 
    public int breakText(Paint textPaint, String strText, int breakWidth) { 
        //StringBuilder sb = new StringBuilder(); 
        int endValue = 0; 
        int totalLine = 0;
        do{ 
        	
            endValue = textPaint.breakText(strText, true, breakWidth, null); 
            if(endValue > 0) {
            	
            	words.add(strText.substring(0, endValue));
            	
            	
                //sb.append(strText.substring(0, endValue)).append("\n"); 
                strText = strText.substring(endValue); 
            } 
            totalLine ++;
        }while(endValue > 0 && totalLine < MAX_LINENUMBER);
       // sb.toString().substring(0, sb.length()-1);  // 마지막 "\n"를 제거
        return totalLine;
        		 
    } 
     

}

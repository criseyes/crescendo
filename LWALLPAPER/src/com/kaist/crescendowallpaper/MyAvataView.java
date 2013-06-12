package com.kaist.crescendowallpaper;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MyAvataView {
	
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
	private View v;
	private ImageView head;
	private ImageView body;
	private Bitmap headImg[];
	private Bitmap bodyImg[];
	private boolean isStickyMode;
	private long lastDownEventActionTime;
	private boolean isNeedDrawDirecty;
	
	public MyAvataView(Context context) {
        startX = rand.nextInt(StarWallpaper.Width - BODY_SX);
        startY = rand.nextInt(StarWallpaper.Height - (HEAD_SY + BODY_SY));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		v = inflater.inflate(R.layout.avata, null);
		
		head = (ImageView) v.findViewById(R.id.head);
		body = (ImageView) v.findViewById(R.id.body);
				
		head.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.common_icon11), HEAD_SX, HEAD_SY, true));
		
		body.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), 
						R.drawable.setting_screenedit), BODY_SX, BODY_SY, true));
		
		head.setLeft(startX + HEAD_SXOFFSET);
		head.setRight(startX + HEAD_SXOFFSET + HEAD_SX);
		head.setTop(startY);
		head.setBottom(startY + HEAD_SY);
		
		body.setLeft(startX);
		body.setRight(startX + BODY_SX);
		body.setTop(startY + HEAD_SY);
		body.setBottom(startY + HEAD_SY + BODY_SY);
		
		/* Sadly, It's not work at all */
		head.setActivated(true);
		head.setEnabled(true);
		head.setClickable(true);
		head.setLongClickable(true);
		v.setActivated(true);
		v.setEnabled(true);
		v.setClickable(true);
		v.setLongClickable(true);
		
		/* Sadly, It's not work at all */
		head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("MyTag", "head click");
				
			}
		});
		
		/* Sadly, It's not work at all */
				OnTouchListener _gestureListener = new View.OnTouchListener()
	        {
	            public boolean onTouch(View v, MotionEvent e)
	            {
	            	Log.d("MyTag", "_gestureListener");
					return false;
	            }
	        };
	        
	        head.setOnTouchListener(_gestureListener);
		
	        v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d("MyTag", "v click");
				
			}
		}); /* Sadly, It's not work at all */
		
		
		v.setWillNotDraw( true ); /* what for? I don't know yet. */
		
   }
	
	public void moveBySelf() {
		if(isNeedDrawDirecty) {
			isNeedDrawDirecty = false;
			return;
		}
		if(countX == 0) {
			countX = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directX = rand.nextBoolean() == true? 1:-1;
		}
		
		if(countY == 0) {
			countY = rand.nextInt(MAX_COUNT - MIN_COUNT) + MIN_COUNT;
			directY = rand.nextBoolean() == true? 1:-1;
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
		
		setLayer(startX,startY);
		
		countX--;
		countY--;
		
		
		
		//Log.d("layout ","head.getLeft(): "+head.getLeft()+ "head.getRight(): " + head.getRight() + "head.getTop(): " + head.getTop() + "head.getBottom(): " + head.getBottom() );
		//Log.d("layout ","body.getLeft(): "+body.getLeft()+ "body.getRight(): " + body.getRight() + "body.getTop(): " + body.getTop() + "body.getBottom(): " + body.getBottom() );
	}
	
	public void setPosition(int x, int y) {
		
		setLayer(x,y);
		isNeedDrawDirecty = true;
	}
	
	private void setLayer(int x, int y) {
		startX = x;
		startY = y;
		
		head.setLeft(startX + HEAD_SXOFFSET);
		head.setRight(startX + HEAD_SXOFFSET + HEAD_SX);
		head.setTop(startY);
		head.setBottom(startY + HEAD_SY);
		
		body.setLeft(startX);
		body.setRight(startX + BODY_SX);
		body.setTop(startY + HEAD_SY);
		body.setBottom(startY + HEAD_SY + BODY_SY);
		
	}
	
	public void draw(Canvas canvas) {
		/* 
		 * TODO change bitmap image from arrays
		 */
		//v.layout(100, 100, 500, 500);
		//v.layout
		//head.measure(MAX_HEAD_SIZE, MAX_HEAD_SIZE);
		//head.measure(MAX_BODY_SIZE, MAX_BODY_SIZE);
		//head.layout((int)head.getX(), (int)head.getY(), 300, 300);
		//body.layout(300, 300, 500, 500);
		
		//head.draw(canvas);
		//body.draw(canvas);
		head.layout(head.getLeft(), head.getRight(), head.getTop(), head.getBottom());
		body.layout(body.getLeft(), body.getRight(), body.getTop(), body.getBottom());
		
		v.draw(canvas);
	}

	public boolean onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		//v.dispatchTouchEvent(event);
		//v.
		//head.dispatchTouchEvent(event);
		//Log.d("MyTag", "onTouch :" + event.getAction()+"x= "+ event.getX()+"y= "+ event.getY());
		long time = event.getEventTime();
		if(isStickyMode == true || time - lastDownEventActionTime < (DOUBLE_TAP_INTV*2)) /* to catch drag gesture */
		{
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				isStickyMode = true;
				setPosition((int) event.getX(), (int) event.getY());
				return true;
			}
			else 
				{
					isStickyMode = false;
					lastDownEventActionTime = MotionEvent.ACTION_OUTSIDE;
					return true;
				}
		}
		
		if(isMyPosition(head, event) || isMyPosition(body, event)) /* I want to know the event */
		{
			
			
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				
				
				if(time - lastDownEventActionTime < DOUBLE_TAP_INTV) {  
					wowDoubleTap();
				}
				
				lastDownEventActionTime = time;
			}
			return true;
		}
		lastDownEventActionTime = MotionEvent.ACTION_OUTSIDE;
		return false;
	}
	
	private boolean isMyPosition(View v, MotionEvent event)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();
		

		
		if(x > v.getLeft() && x < v.getRight())
			if(y > v.getTop() && y < v.getBottom())
				return true;
		
		return false;
	}
	
	private void wowDoubleTap()
	{
		
	}
}

package com.kaist.crescendowallpaper;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class StarWallpaper extends WallpaperService {
    public static int Width, Height;
	private static Context mCrescendo;
	
	private static boolean isAvataEnabled;
	private static boolean isFriendEnabled;
	
	private static int myAvataType;
	private static int friendAvataType;
	
	private static String myAvataName;
	private static String friendAvataName;
	
	Engine myEngine; 
	
	
 
     //-----------------------------------
     // onCreate   
     //-----------------------------------
     @Override
     public void onCreate() {
          super.onCreate();
          /* just for debugging wait to sync*/
          /* TODO should remove before commercial release */
          //android.os.Debug.waitForDebugger();
          
          try {
			mCrescendo = createPackageContext("com.kaist.crescendo", Context.MODE_PRIVATE);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
          loadSettings();
          IntentFilter filter = new IntentFilter("android.intent.action.crescendo.changesettings");
          registerReceiver(mReceiver, filter);
     }
     
     BroadcastReceiver mReceiver = new BroadcastReceiver()
     {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				mCrescendo = createPackageContext("com.kaist.crescendo", Context.MODE_PRIVATE);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadSettings();
			((StarEngine) myEngine).restart();
		}
    	 
     };
     
   
 	private void loadSettings()
 	{
 		
 		SharedPreferences prefs = mCrescendo.getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
 		isAvataEnabled = prefs.getBoolean(MyPref.AVATA_ENABLED, false);
 		isFriendEnabled = prefs.getBoolean(MyPref.FRIEND_ENABLED, false);
 		
 		myAvataType = prefs.getInt(MyPref.MY_AVATA_TYPE, 0);
 		friendAvataType = prefs.getInt(MyPref.FRIEND_AVATA_TYPE, 0);
 		
 		myAvataName = prefs.getString(MyPref.MY_AVATA_NAME, "");
 		friendAvataName = prefs.getString(MyPref.FRIEND_AVATA_NAME, "");
 		
 		Log.d("loadSettings", friendAvataName + myAvataName + myAvataType + friendAvataType + isFriendEnabled +  isAvataEnabled);
 	
 	}
 	
     //-----------------------------------
     // onDestroy
     //-----------------------------------
     @Override
     public void onDestroy() {
          super.onDestroy();
     }
     //-----------------------------------
     // onCreateEngine
     //-----------------------------------
     @Override
     public Engine onCreateEngine() {
          return myEngine = new StarEngine();
 }
     

//----------------  StarEngine  ----------------------------------------------------
 class StarEngine extends Engine {
      private MyThread mThread;
	private SurfaceHolder mholder;
  
  //-----------------------------------
  // Constructor
  //-----------------------------------
  StarEngine() {
       //SurfaceHolder holder = getSurfaceHolder();
  }
  
  public void restart(){
		  mThread.pausePainting();
		  mThread.stopPainting();
		  mThread.end();
	       
	   mThread = new MyThread(mholder, getApplicationContext(), mCrescendo);
	   mThread.start();
	   mThread.resumePainting();
  }


@Override
	public Bundle onCommand(String action, int x, int y, int z, Bundle extras,
			boolean resultRequested) {

	  	Log.d("My", "onCommand action" + action.toString());
	  	if(mThread.onCommand(action, x, y, z, extras, resultRequested) == true)
	  		return null;
		return super.onCommand(action, x, y, z, extras, resultRequested);
	}
  
  
  //-----------------------------------
  // onCreate
  //-----------------------------------
  @Override
  public void onCreate(SurfaceHolder holder) {
       super.onCreate(holder);
       setTouchEventsEnabled(true);
       mholder = holder; 
       mThread = new MyThread(holder, getApplicationContext(),mCrescendo );
  }
  
  //-----------------------------------
  // onDestroy
  //-----------------------------------
  @Override
  public void onDestroy() {
       super.onDestroy();
       mThread.stopPainting();
  }
  
  //-----------------------------------
  // onVisibilityChanged
  //-----------------------------------
  @Override
  public void onVisibilityChanged(boolean visible) {
       if (visible) {
    	   mThread.resumePainting();
       }
       else
       {
    		 mThread.pausePainting();
       }
  }
  
  
  //-----------------------------------
  // onSurfaceCreated
  //-----------------------------------
  @Override
  public void onSurfaceCreated(SurfaceHolder holder) {
       super.onSurfaceCreated(holder);
       mThread.start();
  }
  //-----------------------------------
  // onSurfaceChanged
  //-----------------------------------
  @Override
  public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
       super.onSurfaceChanged(holder, format, width, height);
       mThread.setSurfaceSize(width, height);
  }
  
  //------------------------------
  // surfaceDestroyed
  //------------------------------
  @Override
  public void onSurfaceDestroyed(SurfaceHolder holder) {
       super.onSurfaceDestroyed(holder);
       boolean retry = true;
       mThread.stopPainting();
         while (retry) {
              try {
                   mThread.join();
                   retry = false;
              } catch (InterruptedException e) {      
                   // nothing
              } 
         } // while
  }
  
  //-----------------------------------
  // onOffsetsChanged
  //-----------------------------------
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep,
                                       int xPixels, int yPixels) {
   
  }
  
  //-----------------------------------
  // onTouchEvent
  //-----------------------------------
  @Override
  public void onTouchEvent(MotionEvent event) {
       super.onTouchEvent(event);
       mThread.doTouchEvent(event);
  }

 } // end of Engine
 
//------------------  thread  -----------------------------------------------------
 class MyThread extends Thread {
  public SurfaceHolder mHolder;
  private Context mContext;
  private boolean wait = true;
  private boolean canRun = true;
  
  public int bx, by;
  //private ArrayList<Star> stars = new ArrayList<Star>();
  //private ArrayList<MyAvataView> avatas = new ArrayList<MyAvataView>();
  private ArrayList<MyAvata2> avatas = new ArrayList<MyAvata2>();
  
  private Bitmap imgBack;
  
  
  
  public boolean onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested)
  {
	  for (MyAvata2 avata : avatas) {
     	   if(avata.onCommand(action, x, y, z, System.currentTimeMillis()))
     		   return true;
	  }
	  return false;
	  
  }
  public void end() {
	  
	try {
		finalize();
	} catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
//------------------------------
  // Constructor
  //------------------------------
  public MyThread(SurfaceHolder holder, Context context, Context appContext) {
       mHolder = holder;
       mContext = context;
   
       Display display = ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
       Width  = display.getWidth();   // 화면의 폭
       Height = display.getHeight();   // 화면의 높이
       //imgBack = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.backgroud_black);
       
       //imgBack = Bitmap.createScaledBitmap(imgBack, (int) (Height * 1.2), (int) (Height * 1.2), true);
       
       imgBack = ((BitmapDrawable) getWallpaper()).getBitmap();
       imgBack = Bitmap.createScaledBitmap(imgBack, (int) Height, (int) Height, true);
      
       //bx = by = (int) (Height * 0.6);      // 이미지 중심
   
       
//       for (int i = 1; i <= 50; i++)  //최초의 별의 갯수
//            stars.add(new Star());
       if(isAvataEnabled == true)
       {
    	   avatas.add(new MyAvata2(mContext, appContext, myAvataType, myAvataName, true));
       }
       
       if(isFriendEnabled == true)
       {
    	   avatas.add(new MyAvata2(mContext, appContext, friendAvataType, friendAvataName, false ));
       }
   
  }
  //------------------------------
  // pause Painting
  //------------------------------
    public void pausePainting() {
         wait = true;
         synchronized (this) {
              this.notify();
          }
   }
  
  //------------------------------
  // resume Painting
  //------------------------------
    public void resumePainting() {
         wait = false;
         synchronized (this) {
              this.notify();
       }
    }
  
  //------------------------------
  // stop Painting
  //------------------------------
   public void stopPainting() {
         canRun = false;
         synchronized (this) {
              this.notify();
       }
   }
  
  //------------------------------
  // set SurfaceSize
  //------------------------------
   public void setSurfaceSize(int width, int height) {
       Width = width;
       Height = height;
        synchronized (this) {
              this.notify();
         }
    }
  
  //------------------------------
  // do TouchEvent
  //------------------------------
   public void doTouchEvent(MotionEvent event) {
         wait = false;
         synchronized (this) {
             for (MyAvata2 avata : avatas) {
          	   if(avata.onTouch(event))
          		   break;
             }
              this.notify();
          }
         
   }

  //------------------------------
  // run
  //------------------------------
   public void run() {
         canRun = true;
         Canvas canvas = null;          // canvas를 만든다
         
          while (canRun) {
               canvas = mHolder.lockCanvas();       // canvas를 잠그고 버퍼 할당
               try {
                    synchronized (mHolder) {       // 동기화 유지
                         canvas.drawBitmap(imgBack, 0, 0, null);  // 버퍼에 그리기
                         Draw(canvas);
                    }
               } finally {
                if (canvas != null)
                      mHolder.unlockCanvasAndPost(canvas);    
               } // try

             synchronized (this) {
                  if (wait) {
                       try {
                            wait();
                       } catch (Exception e) {
                               // nothing
                      }
                  }
           } // sync
          } // while
        } // run
  //
  private void Draw(Canvas canvas) {
	   for (MyAvata2 avata : avatas) {
		   avata.moveBySelf();
		   avata.draw(canvas);
	   }  
 } // Draw
} // thread
} // end of wallpaper

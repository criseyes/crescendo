package com.kaist.crescendo.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.AvataData;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class AvataEditorActivity extends UpdateActivity {
	
	private Bitmap img;
	private ImageView imgView;
	private String name;
	private boolean isEnabled;
	private int planType;
	private int planUid;
	private String title;
	
	private Context mContext;
	//private int asyncTaskState;
	//private boolean asynTaskResult;
	
	private static final int RESIZE_WIDTH = 640;
	private static final int RESIZE_HEIGHT = 480;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_avataeditor);
		setTitle(R.string.str_setAvata);
		
		loadSettings(); /* load saved values */
		
				
		imgView = (ImageView) findViewById(R.id.avataImage);
		imgView.setOnClickListener(mClickListener);
		
		((EditText) findViewById(R.id.avataName)).setText(name); /* avata name */
		
		if(planType == MyStaticValue.PLANTYPE_DIET) /* avata type */
		{
			((ImageView) findViewById(R.id.avataIcon)).setImageDrawable(this.getResources().getDrawable(R.drawable.icon_diet));
		}
		else if(planType == MyStaticValue.PLANTYPE_READING_BOOK)
		{
			((EditText) findViewById(R.id.avataType)).setText(getResources().getString(R.string.str_plantype_reading));
			((ImageView) findViewById(R.id.avataIcon)).setImageDrawable(this.getResources().getDrawable(R.drawable.icono_reading));
		}
		
		((EditText) findViewById(R.id.avataType)).setText(title);
		/* avata image */
			
		File file = getApplicationContext().getFileStreamPath(MyStaticValue.AVATA_FILNENAME);
		if(file.exists() == false) {
			img = BitmapFactory.decodeResource(getResources(), R.drawable.man_shop);
			((ImageView) findViewById(R.id.avataImage)).setImageBitmap(img);
		}
		else {
			String x = file.getPath().toString();
			Log.d("me", x + file.canRead() + file.canWrite() + file.length() + " " + file.isFile());
			//Bitmap y = BitmapFactory.decodeFile(file.getPath().toString());
			img = BitmapFactory.decodeFile(file.getPath().toString());
			((ImageView) findViewById(R.id.avataImage)).setImageBitmap(img);
		}
		
		findViewById(R.id.button_save).setOnClickListener(mClickListener);
		
		mContext = this;
	}
	
	private void loadSettings()
	{
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		name = prefs.getString(MyPref.MY_AVATA_NAME, "");
		title = prefs.getString(MyPref.MY_AVATA_TITLE, "");
		
		isEnabled = prefs.getBoolean(MyPref.AVATA_ENABLED, false);
		
		planType = prefs.getInt(MyPref.MY_AVATA_TYPE, MyStaticValue.PLANTYPE_DIET);
		
		planUid = prefs.getInt(MyPref.MY_AVATA_UID, 0);

	}
	
	private void saveSettings(boolean isEnable, String name)
	{
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(MyPref.AVATA_ENABLED, isEnable);
		editor.commit();
		
		editor.putString(MyPref.MY_AVATA_NAME, name);
		editor.commit();
		
		sendBroadCasetIntent();
	}
	
	private void sendBroadCasetIntent()
	{
		Intent intent = new Intent(MyStaticValue.ACTION_UPDATEWALLPAPER);

		sendBroadcast(intent);
	}
	

	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId())  {
           		case R.id.avataImage:
           			Intent intent = new Intent();
           			intent.setAction( Intent.ACTION_GET_CONTENT );  
           			intent.setType( "image/*" );
           			startActivityForResult( Intent.createChooser(intent, "Select Picture"), MyStaticValue.REQUESTCODE_GETAVATAIMAGE );
           			
           			break;
           		case R.id.button_save:
           			/* make avata instance */
           			String name = ((EditText)findViewById(R.id.avataName)).getText().toString();
           			FileOutputStream fo = null;
           			
           			File file = getApplicationContext().getFileStreamPath(MyStaticValue.AVATA_FILNENAME);
           			file.deleteOnExit();
           			
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
           				
           			try {
           				try {
							fo = new FileOutputStream(file);
							img.compress(CompressFormat.PNG, 100, fo);
							fo.flush();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
           				
           			}
           			finally 
           			{     
           				
           				
           				if(fo != null)
							try {
								fo.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
           				
           				     
           			}
           
          			
           			AvataData avata = new AvataData(name, planType, planUid, img, isEnabled);
           			/* send data to server */
           			sendAvataData(avata);
           			/* save info. to preference */
           			saveSettings(isEnabled, name);
           			finish();
           			
           			break;
           		default:
           			break;
           
           }
		
		}
	};
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch(requestCode)
		{
		case MyStaticValue.REQUESTCODE_GETAVATAIMAGE:
			if(resultCode==RESULT_OK){
				Uri selPhotoUri = data.getData();				
				new FaceDector().execute(selPhotoUri);
				
				/*
				while(asyncTaskState == -1) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
				}
				*/
			}
			break;
		}
	}
	
	private boolean doFaceDetect(Uri selPhotoUri) {
		boolean isOK = false;
		
		try {
	    	Bitmap oriImg = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), RESIZE_WIDTH, RESIZE_HEIGHT, true);
	    	Bitmap maskBitmap = Bitmap.createBitmap(oriImg.getWidth(), oriImg.getHeight(), Bitmap.Config.RGB_565);
	    	Canvas c = new Canvas();
	    	c.setBitmap(maskBitmap);
	    	Paint p = new Paint();
	    	p.setFilterBitmap(true);
	    	c.drawBitmap(oriImg, 0, 0, p);
	    	oriImg.recycle();
	    	
	    	oriImg = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), RESIZE_WIDTH, RESIZE_HEIGHT, true);
	    	
	    	//FaceDetector faceDetector = new FaceDetector(oriImg.getWidth(),oriImg.getHeight(), 2);
	    	FaceDetector faceDetector = new FaceDetector(maskBitmap.getWidth(),maskBitmap.getHeight(), 1);
	    	FaceDetector.Face[] detectedFace = new FaceDetector.Face[1];
	    	
	    	int detected_num = faceDetector.findFaces(maskBitmap, detectedFace);
	    	
	    	Log.v("faceDetect", "width:" + maskBitmap.getWidth() + "height:" + maskBitmap.getHeight() + "config:" + maskBitmap.getConfig());
	    	
	    	//float confidence = detectedFace[0].confidence();
	    	
	    	if(detected_num > 0 ) {
	    		PointF point = new PointF();
	    		int eye_distance = (int) detectedFace[0].eyesDistance();
	    		detectedFace[0].getMidPoint(point);
	    		int x = (int) (point.x - eye_distance);
	    		int y = (int) (point.y - (float)eye_distance * 2.0f);	    		
	    		int yOffset = 0;
	    		if(x < 0) x = 0;
	    		if(y < 0) {
	    			yOffset = y;
	    			y = 0;
	    		}
	    		int width = (int)((float)eye_distance * 2.2f);
	    		int height = (int)(((float)eye_distance*2.0f - (float)yOffset) * 2.0f);
	    		if((x + width) >= RESIZE_WIDTH) width = RESIZE_WIDTH - x;
	    		if((y + height) >= RESIZE_HEIGHT) height = RESIZE_HEIGHT - y;
	    		Bitmap cropImg = Bitmap.createBitmap(oriImg, x, y, width, height);
	    		// make bitmap image to rounded rect
	    		/*
	    		Bitmap output = Bitmap.createBitmap(cropImg.getWidth(), cropImg.getHeight(), Config.ARGB_8888);
	    		Canvas canvas = new Canvas(output);
	    		final int color = 0xff424242;
	    		final Paint paint = new Paint();
	    		final Rect rect = new Rect(0,0,cropImg.getWidth(),cropImg.getHeight());
	    		final RectF rectF = new RectF(rect);
	    		final float roundPx = 35;
	    		paint.setAntiAlias(true);
	    		canvas.drawARGB(0,0,0,0);
	    		paint.setColor(color);
	    		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    		canvas.drawBitmap(cropImg, rect, rect, paint);
	    		*/
	    		
	    		BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.face);
	    		Bitmap faceImg = drawable.getBitmap();
	    		Bitmap output = Bitmap.createScaledBitmap(faceImg, cropImg.getWidth(), cropImg.getHeight(), true);
	    		Canvas canvas = new Canvas(output);
	    		final int color = 0xff424242;
	    		final Paint paint = new Paint();
	    		final Rect rect = new Rect(0,0,cropImg.getWidth(),cropImg.getHeight());
	    		//final RectF rectF = new RectF(rect);
	    		//final float roundPx = 35;
	    		paint.setAntiAlias(true);
	    		//canvas.drawARGB(0,0,0,0);
	    		paint.setColor(color);
	    		//canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    		canvas.drawBitmap(cropImg, rect, rect, paint);
	    		
	    		//add hair image
	    		BitmapDrawable drawable2 = (BitmapDrawable) getResources().getDrawable(R.drawable.hair);
	    		Bitmap hairImg = drawable2.getBitmap();
	    		Bitmap hairImg2 = Bitmap.createScaledBitmap(hairImg, cropImg.getWidth(), cropImg.getHeight(), true);
	    		Canvas canvas2 = new Canvas(output);
	    		paint.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
	    		canvas2.drawBitmap(hairImg2, rect, rect, paint);
	    		
	    		img = Bitmap.createScaledBitmap(output, MyStaticValue.AVATA_WIDTH, MyStaticValue.AVATA_HIGHT, true);
	    		isOK = true;
	    	} else {
	    		img = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), MyStaticValue.AVATA_WIDTH, MyStaticValue.AVATA_HIGHT, true);
	    	}
	    	
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		return isOK;
	}
	
	public class FaceDector extends AsyncTask<Uri, Integer, Boolean> {

		private ProgressDialog dialog;
		

		
		@Override
		protected void onPreExecute() {
			//asyncTaskState = -1;
			dialog = new ProgressDialog(mContext, android.R.style.Widget_DeviceDefault);
			dialog.setTitle("처리중");
			dialog.setMessage("잠시만 기다려주세요.");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result == false) {
				Toast.makeText(mContext, R.string.str_err_cantfindface, Toast.LENGTH_LONG).show();
			}
			imgView.setImageBitmap(img);
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(Uri... params) {
			boolean result = false;
			Uri uri = params[0];
			
			result = doFaceDetect(uri);
			
			dialog.dismiss();
			
			//asyncTaskState = 0;
			
			return result;
		}
	}
}

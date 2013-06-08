package com.kaist.crescendo.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
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
			((EditText) findViewById(R.id.avataType)).setText(getResources().getString(R.string.str_plantype_diet));
			((ImageView) findViewById(R.id.avataIcon)).setImageDrawable(this.getResources().getDrawable(R.drawable.icon_diet));
		}
		else if(planType == MyStaticValue.PLANTYPE_READING_BOOK)
		{
			((EditText) findViewById(R.id.avataType)).setText(getResources().getString(R.string.str_plantype_reading));
			((ImageView) findViewById(R.id.avataIcon)).setImageDrawable(this.getResources().getDrawable(R.drawable.icono_reading));
		}
		
		/* avata image */
			
		File file = getApplicationContext().getFileStreamPath(MyStaticValue.AVATA_FILNENAME);
		if(file.exists() == false)
			((ImageView) findViewById(R.id.avataImage)).setImageResource(R.drawable.man_shop);
		else {
			String x = file.getPath().toString();
			Log.d("me", x + file.canRead() + file.canWrite() + file.length() + " " + file.isFile());
			Bitmap y = BitmapFactory.decodeFile(file.getPath().toString());
			img = BitmapFactory.decodeFile(file.getPath().toString());
			((ImageView) findViewById(R.id.avataImage)).setImageBitmap(img);
		}
		
		findViewById(R.id.button_save).setOnClickListener(mClickListener);

		
	}
	
	private void loadSettings()
	{
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_PRIVATE);
		name = prefs.getString(MyPref.MY_AVATA_NAME, "");
		
		isEnabled = prefs.getBoolean(MyPref.AVATA_ENABLED, false);
		
		planType = prefs.getInt(MyPref.MY_AVATA_TYPE, MyStaticValue.PLANTYPE_DIET);
		
		planUid = prefs.getInt(MyPref.MY_AVATA_UID, 0);

	}
	
	private void saveSettings(boolean isEnable, String name)
	{
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(MyPref.AVATA_ENABLED, isEnable);
		editor.commit();
		
		editor.putString(MyPref.MY_AVATA_NAME, name);
		editor.commit();
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
						    
			    try {
			    	Uri selPhotoUri = data.getData();
			    	Bitmap oriImg = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), 640, 480, true);
			    	Bitmap maskBitmap = Bitmap.createBitmap(oriImg.getWidth(), oriImg.getHeight(), Bitmap.Config.RGB_565);
			    	Canvas c = new Canvas();
			    	c.setBitmap(maskBitmap);
			    	Paint p = new Paint();
			    	p.setFilterBitmap(true);
			    	c.drawBitmap(oriImg, 0, 0, p);
			    	oriImg.recycle();
			    	
			    	oriImg = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), 640, 480, true);
			    	
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
			    		int y = (int) (point.y - eye_distance);
			    		if(x < 0) x = 0;
			    		if(y < 0) y = 0;
			    		Bitmap cropImg = Bitmap.createBitmap(oriImg, x, y, eye_distance*2, eye_distance*3);
			    		img = Bitmap.createScaledBitmap(cropImg, MyStaticValue.AVATA_WIDTH, MyStaticValue.AVATA_HIGHT, true);
			    	} else {
			    		img = Bitmap.createScaledBitmap(Images.Media.getBitmap( getContentResolver(), selPhotoUri ), MyStaticValue.AVATA_WIDTH, MyStaticValue.AVATA_HIGHT, true);
			    		Toast.makeText(this, "���� ã�� ���Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
			    	}
			    	
			    } catch (FileNotFoundException e) {
			    	e.printStackTrace();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    }
			    imgView.setImageBitmap(img);
			}
		}
	}
}

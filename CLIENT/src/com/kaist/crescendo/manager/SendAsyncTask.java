package com.kaist.crescendo.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SendAsyncTask extends AsyncTask<String, Integer, Boolean>{
	private ProgressDialog dialog;
	private Context mContext;
	
	public SendAsyncTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(mContext);
		dialog.setTitle("Interminate");
		dialog.setMessage("Please wait while sending");
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
		super.onPostExecute(result);
	}

	public void execute(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		long sum = 0;
		for( int i = 0 ; i < 1000000; i++) {
				sum = sum + i;
		}
		dialog.dismiss();
		return null;
	}
}

package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaist.crescendo.R;

public class PlanListAdapter extends BaseAdapter {

	private List<PlanData> mItems = new ArrayList<PlanData>();
	private Context mContext;

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItems.get(position);
	}
	
	public void setListItems(List<PlanData> lit) {
		mItems = lit;
	}
	
	public PlanListAdapter(Context context) {
		super();
		mContext = context;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		
		if (v == null) {
			// Layout Inflation
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
			v = inflater.inflate(R.layout.list_plans,null);
			
			// set tag to improve performance... it's likely view holder pattern.
			
			v.setTag(R.id.icon, v.findViewById(R.id.icon));
			v.setTag(R.id.toptext, v.findViewById(R.id.toptext));
			v.setTag(R.id.bottomtext, v.findViewById(R.id.bottomtext));

		} else {
			
			// set value by tagging information
			ImageView icon = (ImageView)v.getTag(R.id.icon);
			TextView title = (TextView)v.getTag(R.id.toptext);
			TextView date = (TextView)v.getTag(R.id.bottomtext);
			
			/*
			 *  TODO give some value, mItems
			 */
		}

		return v;
	}

	public void addItem(PlanData it) {
		mItems.add(it);
	}
}

package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyStaticValue;

public class CandidateListAdapter extends BaseAdapter {
	private List<FriendData> mItems = new ArrayList<FriendData>();
	private Context mContext;

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItems.get(position);
	}
	
	public void setListItems(List<FriendData> lit) {
		mItems = lit;
	}
	
	public CandidateListAdapter(Context context) {
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
			v = inflater.inflate(R.layout.list_selectfriends,null);
			
			// set tag to improve performance... it's likely view holder pattern.
			v.setTag(R.id.icon, v.findViewById(R.id.icon));
			v.setTag(R.id.toptext, v.findViewById(R.id.toptext));
			v.setTag(R.id.bottomtext, v.findViewById(R.id.bottomtext));
			v.setTag(R.id.frinedsel, v.findViewById(R.id.frinedsel));
		} 
			
		// set value by tagging information
		ImageView icon = (ImageView)v.getTag(R.id.icon);
		TextView title = (TextView)v.getTag(R.id.toptext);
		TextView date = (TextView)v.getTag(R.id.bottomtext);
		CheckBox isSelected = (CheckBox)v.getTag(R.id.frinedsel);
		
		/*
		 *  TODO give some value, mItems
		 */
		title.setText(mItems.get(position).id);
		date.setText(mItems.get(position).plan.title /* + " ~ " + mItems.get(position).plan.end */);
		isSelected.setChecked(mItems.get(position).isSelected);
		//isSelected.setChecked(((ListView)parent).isItemChecked(position));
		if(mItems.get(position).plan.type == MyStaticValue.PLANTYPE_DIET)
			icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_diet));
		return v;
	}

	public void addItem(FriendData it) {
		mItems.add(it);
	}
}

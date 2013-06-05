package com.kaist.crescendo.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.CandidateListAdapter;
import com.kaist.crescendo.data.FriendListAdapter;

public class CandidateListActivity extends UpdateActivity {

	private CandidateListAdapter adapter;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendselector);
		setTitle(R.string.str_add_newfriend);
	}
}
package com.kaist.crescendo.activity;

import java.util.ArrayList;

import com.kaist.crescendo.chart.Chart;
import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import android.os.Bundle;

public class StatusActivity extends UpdateActivity {
	
	private Chart mChart;
	private ArrayList<PlanData> planArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_status);
		setTitle(R.string.str_show_status);
		
		mChart = (Chart)findViewById(R.id.chart01);
		
		mChart.setChartType(Chart.Chart_Type_Line);
		
		mChart.setYAxisMaximum(true, 2000);
		
		mChart.invalidate();
	}
}

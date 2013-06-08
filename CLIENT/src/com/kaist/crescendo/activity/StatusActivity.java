package com.kaist.crescendo.activity;

import java.util.ArrayList;

import com.essence.chart.Chart;
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
		
		planArrayList = new ArrayList<PlanData>();
		
		String result = getPlanList(planArrayList);
		
		if(result.equals("good")) {
			PlanData plan = null;
			for(int i = 0; i < planArrayList.size() ; i++) {
				if(planArrayList.get(i).isSelected == true) {
					plan = planArrayList.get(i);
					break;
				}
			}
			
			if(plan != null) {
				mChart.setChartType(Chart.Chart_Type_3D_Line, plan);
				mChart.setYAxisMaximum(true, 2000);
				mChart.invalidate();
			}
		}
	}
}

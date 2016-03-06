package com.example.kds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.kds.action.ExpressModel;
import com.example.kds.bean.HistoryExpress;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HistoryActivity extends BaseActivity {

	private ListView list;
	private ArrayList<HistoryExpress> expresses;
	private SimpleAdapter adapter;
	private TextView tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);

		setContentView(R.layout.search_history);

		tip = (TextView) findViewById(R.id.tip);
		list = (ListView) findViewById(R.id.list);

		expresses = new ExpressModel(context).queryAll();

		tip.setText("" + expresses.size());

		adapter = new SimpleAdapter(context, getData(), R.layout.history_item,
				new String[] { "expTextName", "mailNo" }, new int[] {
						R.id.tvCp, R.id.tvEp });
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(HistoryActivity.this,
						ResultActivity.class);
				intent.putExtra("json", expresses.get(position).json);
				startActivity(intent);

			}
		});

	}

	private List<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (HistoryExpress express : expresses) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("expTextName", express.expTextName);
			map.put("mailNo", express.mailNo);
			data.add(map);
		}
		return data;
	}

}

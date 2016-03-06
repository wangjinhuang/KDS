package com.example.kds;

import java.util.ArrayList;

import com.example.kds.adapter.ResultAdapter;
import com.example.kds.bean.Data;
import com.example.kds.bean.Express;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ResultActivity extends BaseActivity {

	private ArrayList<Data> datas;
	private ListView list;
	private ResultAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);

		setContentView(R.layout.result_list);
		list = (ListView) findViewById(R.id.result_list);

		Intent intent = getIntent();
		if (intent != null) {
			String str = intent.getStringExtra("json");
			Express express = new Gson().fromJson(str, Express.class);
			datas = express.data;
		}

		adapter = new ResultAdapter(context, datas);
		list.setAdapter(adapter);
	}

}

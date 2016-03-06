package com.example.kds.adapter;

import java.util.ArrayList;

import com.example.kds.R;
import com.example.kds.bean.Data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Data> datas;

	public ResultAdapter(Context context, ArrayList<Data> datas) {
		super();
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {

		return datas.size();
	}

	@Override
	public Object getItem(int position) {

		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = LayoutInflater.from(context).inflate(R.layout.result_list_item, null);
		TextView time = (TextView) view.findViewById(R.id.time);
		TextView content = (TextView) view.findViewById(R.id.context);
		Data data = datas.get(position);

		time.setText(data.time);
		content.setText(data.context);

		return view;
	}

}

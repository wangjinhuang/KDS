package com.example.kds.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.kds.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyListAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, Object>> companyList;
	Context context;

	public CompanyListAdapter(ArrayList<HashMap<String, Object>> companyList, Context context) {
		super();
		this.companyList = companyList;
		this.context = context;
	}

	@Override
	public int getCount() {

		return companyList.size();
	}

	@Override
	public Object getItem(int position) {

		return companyList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = LayoutInflater.from(context).inflate(R.layout.compan_list_item, null);

		ImageView logo;
		TextView name;
		logo = (ImageView) view.findViewById(R.id.company_logo);
		name = (TextView) view.findViewById(R.id.company_name);

		HashMap<String, Object> map = companyList.get(position);

		logo.setImageResource((Integer) map.get("logo"));

		name.setText((CharSequence) map.get("name"));
		return view;
	}

}
